package br.com.fiap.gsproject.controllers;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.gsproject.exceptions.BadRequestException;
import br.com.fiap.gsproject.exceptions.RestNotFoundException;
import br.com.fiap.gsproject.models.CentroDistribuicao;
import br.com.fiap.gsproject.models.Endereco;
import br.com.fiap.gsproject.models.Login;
import br.com.fiap.gsproject.models.Pessoa;
import br.com.fiap.gsproject.models.Receita;
import br.com.fiap.gsproject.repository.CentroDistribuicaoRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/centrodistribuicao")
public class CentroDistribuicaoController {
	Logger log = LoggerFactory.getLogger(CentroDistribuicaoController.class);

	@Autowired
	CentroDistribuicaoRepository repository;

	@Autowired
	PagedResourcesAssembler<Object> assembler;

	@GetMapping
	public PagedModel<EntityModel<Object>> index(@PageableDefault(size = 5) Pageable pageable) {
		Page<CentroDistribuicao> cds = repository.findAll(pageable);

		return assembler.toModel(cds.map(CentroDistribuicao::toEntityModel));
	}

	@GetMapping("{id}")
	public EntityModel<CentroDistribuicao> show(@PathVariable Long id) {
		return getCentroDistribuicao(id).toEntityModel();
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Valid CentroDistribuicao centroDistribuicao) {
		repository.save(centroDistribuicao);

		return ResponseEntity
				.created(centroDistribuicao.toEntityModel().getRequiredLink("self").toUri())
				.body(centroDistribuicao.toEntityModel());
	}

	@DeleteMapping("{id}")
	public ResponseEntity<CentroDistribuicao> destroy(@PathVariable Long id) {
		var centroDistribuicao = getCentroDistribuicao(id);

		repository.delete(centroDistribuicao);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("{id}")
	public EntityModel<CentroDistribuicao> update(@PathVariable Long id,
			@Valid @RequestBody CentroDistribuicao centroDistribuicao) {
		CentroDistribuicao centroDistribuicaoEncontrado = getCentroDistribuicao(id);
		centroDistribuicaoEncontrado.setNome_centro_distribuicao(centroDistribuicao.getNome_centro_distribuicao());
		centroDistribuicaoEncontrado.setNumero_vagas(centroDistribuicao.getNumero_vagas());

		repository.save(centroDistribuicaoEncontrado);

		return centroDistribuicao.toEntityModel();
	}

	// Métodos relacionados a pessoas no centro de distribuicao

	// lista pessoas de um centro de distribuicao
	@GetMapping("{id}/pessoas")
	public ArrayList<EntityModel<Pessoa>> showPessoasFromCentro(@PathVariable Long id) {
		var centroDistribuicao = getCentroDistribuicao(id);

		var pessoas = centroDistribuicao.getPessoas();
		var entityModelPessoas = new ArrayList<EntityModel<Pessoa>>();

		pessoas.forEach((Pessoa p) -> {
			entityModelPessoas.add(p.toEntityModel());
		});

		return entityModelPessoas;
	}

	// adicionar uma pessoa ao centro de distribuicao
	@PostMapping("{id}/pessoas")
	public ResponseEntity<Object> insertPessoaIntoCentro(@PathVariable Long id,
			@RequestBody @Valid Pessoa pessoa) {
		CentroDistribuicao centroDistribuicao = getCentroDistribuicao(id);
		var listaPessoas = centroDistribuicao.getPessoas();
		listaPessoas.add(pessoa);

		centroDistribuicao.setPessoas(listaPessoas);
		repository.save(centroDistribuicao);

		return ResponseEntity
				.created(centroDistribuicao.toEntityModel().getRequiredLink("self").toUri())
				.body(centroDistribuicao.toEntityModel());
	}

	// Métodos relacionados a endereço no centro de distribuição

	// retorna endereco do centro de distribuicao
	@GetMapping("{id}/endereco")
	public EntityModel<Endereco> showEnderecoFromCentro(@PathVariable Long id) {
		var centroDistribuicao = getCentroDistribuicao(id);

		return centroDistribuicao.getEndereco().toEntityModel();
	}

	// adicionar um endereco ao centro de distribuicao
	@PostMapping("{id}/endereco")
	public ResponseEntity<Object> insertEnderecoIntoCentro(@PathVariable Long id,
			@RequestBody @Valid Endereco endereco) {
		CentroDistribuicao centroDistribuicao = getCentroDistribuicao(id);

		if (centroDistribuicao.getEndereco() != null) {
			throw new BadRequestException("Endereço ja cadastrado para esse centro de distribuição");
		}

		centroDistribuicao.setEndereco(endereco);

		repository.save(centroDistribuicao);

		return ResponseEntity
				.created(centroDistribuicao.toEntityModel().getRequiredLink("self").toUri())
				.body(centroDistribuicao.toEntityModel());
	}

	// remover o endereco do centro de distribuicao
	@DeleteMapping("{id}/endereco")
	public ResponseEntity<CentroDistribuicao> destroyEndereco(@PathVariable Long id) {
		CentroDistribuicao centroDistribuicao = getCentroDistribuicao(id);

		centroDistribuicao.setEndereco(null);
		repository.save(centroDistribuicao);

		return ResponseEntity.noContent().build();
	}

	// Métodos relacionados a receitas no centro de distribuicao

	// lista receitas de um centro de distribuicao
	@GetMapping("{id}/receitas")
	public ArrayList<EntityModel<Receita>> showReceitasFromCentro(@PathVariable Long id) {
		var centroDistribuicao = getCentroDistribuicao(id);

		var receitas = centroDistribuicao.getReceitas();
		var entityModelReceitas = new ArrayList<EntityModel<Receita>>();

		receitas.forEach((Receita r) -> {
			entityModelReceitas.add(r.toEntityModel());
		});

		return entityModelReceitas;
	}

	// adiciona uma receita ao centro de distribuicao
	@PostMapping("{id}/receitas")
	public EntityModel<CentroDistribuicao> insertReceitaIntoCentro(@PathVariable Long id,
			@RequestBody @Valid Receita receita) {
		CentroDistribuicao centroDistribuicao = getCentroDistribuicao(id);
		var listaReceitas = centroDistribuicao.getReceitas();
		if (listaReceitas.size() == 2) {
			throw new BadRequestException("Lista de receitas diárias está lotada, nao é possivel adicionar uma nova");
		}

		listaReceitas.add(receita);

		centroDistribuicao.setReceitas(listaReceitas);
		repository.save(centroDistribuicao);

		return centroDistribuicao.toEntityModel();
	}

	// limpa as receitas do centro de distribuicao
	@DeleteMapping("{id}/receitas")
	public ResponseEntity<CentroDistribuicao> destroyReceitas(@PathVariable Long id) {
		CentroDistribuicao centroDistribuicao = getCentroDistribuicao(id);

		centroDistribuicao.setReceitas(null);
		repository.save(centroDistribuicao);

		return ResponseEntity.noContent().build();
	}

	// Métodos relacionados a login no centro de distribuicao

	// retorna login de um centro de distribuicao
	@GetMapping("{id}/login")
	public EntityModel<Login> showLoginFromCentro(@PathVariable Long id) {
		var CentroDistribuicao = getCentroDistribuicao(id);

		return CentroDistribuicao.getLogin().toEntityModel();
	}

	// adicionar um login ao centro de distribuicao
	@PostMapping("{id}/login")
	public EntityModel<CentroDistribuicao> insertLoginIntoCentro(@PathVariable Long id,
			@RequestBody @Valid Login login) {
		CentroDistribuicao centroDistribuicao = getCentroDistribuicao(id);

		if (centroDistribuicao.getLogin() != null) {
			throw new BadRequestException("Login ja cadastrado para esse centro de distribuição");
		}

		centroDistribuicao.setLogin(login);

		repository.save(centroDistribuicao);

		return centroDistribuicao.toEntityModel();
	}

	private CentroDistribuicao getCentroDistribuicao(Long id) {
		return repository.findById(id).orElseThrow(() -> new RestNotFoundException("CentroDistribuicao nao encontrado"));
	}
}