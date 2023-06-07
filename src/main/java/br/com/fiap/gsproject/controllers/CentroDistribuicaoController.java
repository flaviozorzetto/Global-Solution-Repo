package br.com.fiap.gsproject.controllers;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

	@GetMapping
	public List<CentroDistribuicao> index() {
		return repository.findAll();
	}

	@GetMapping("{id}")
	public ResponseEntity<CentroDistribuicao> show(@PathVariable Long id) {
		var CentroDistribuicao = getCentroDistribuicao(id);

		return ResponseEntity.ok(CentroDistribuicao);
	}

	@PostMapping
	public ResponseEntity<CentroDistribuicao> create(@RequestBody @Valid CentroDistribuicao centroDistribuicao) {
		repository.save(centroDistribuicao);

		return ResponseEntity.status(HttpStatus.CREATED).body(centroDistribuicao);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<CentroDistribuicao> destroy(@PathVariable Long id) {
		var centroDistribuicao = getCentroDistribuicao(id);

		repository.delete(centroDistribuicao);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("{id}")
	public ResponseEntity<CentroDistribuicao> update(@PathVariable Long id,
			@Valid @RequestBody CentroDistribuicao centroDistribuicao) {
		getCentroDistribuicao(id);

		centroDistribuicao.setId(id);
		repository.save(centroDistribuicao);

		return ResponseEntity.ok(centroDistribuicao);
	}

	// Métodos relacionados a pessoas no centro de distribuicao

	// lista pessoas de um centro de distribuicao
	@GetMapping("{id}/pessoas")
	public ResponseEntity<List<Pessoa>> showPessoasFromCentro(@PathVariable Long id) {
		var CentroDistribuicao = getCentroDistribuicao(id);

		return ResponseEntity.ok(CentroDistribuicao.getPessoas());
	}

	// adicionar uma pessoa ao centro de distribuicao
	@PostMapping("{id}/pessoas")
	public ResponseEntity<CentroDistribuicao> insertPessoaIntoCentro(@PathVariable Long id,
			@RequestBody @Valid Pessoa pessoa) {
		CentroDistribuicao centroDistribuicao = getCentroDistribuicao(id);
		var listaPessoas = centroDistribuicao.getPessoas();
		listaPessoas.add(pessoa);

		centroDistribuicao.setPessoas(listaPessoas);
		repository.save(centroDistribuicao);

		return ResponseEntity.status(HttpStatus.CREATED).body(centroDistribuicao);
	}

	// Métodos relacionados a endereço no centro de distribuição

	// retorna endereco do centro de distribuicao
	@GetMapping("{id}/endereco")
	public ResponseEntity<Endereco> showEnderecoFromCentro(@PathVariable Long id) {
		var CentroDistribuicao = getCentroDistribuicao(id);

		return ResponseEntity.ok(CentroDistribuicao.getEndereco());
	}

	// adicionar um endereco ao centro de distribuicao
	@PostMapping("{id}/endereco")
	public ResponseEntity<CentroDistribuicao> insertEnderecoIntoCentro(@PathVariable Long id,
			@RequestBody @Valid Endereco endereco) {
		CentroDistribuicao centroDistribuicao = getCentroDistribuicao(id);

		if (centroDistribuicao.getEndereco() != null) {
			throw new BadRequestException("Endereço ja cadastrado para esse centro de distribuição");
		}

		centroDistribuicao.setEndereco(endereco);

		repository.save(centroDistribuicao);

		return ResponseEntity.status(HttpStatus.CREATED).body(centroDistribuicao);
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
	public ResponseEntity<List<Receita>> showReceitasFromCentro(@PathVariable Long id) {
		var CentroDistribuicao = getCentroDistribuicao(id);

		return ResponseEntity.ok(CentroDistribuicao.getReceitas());
	}

	// adiciona uma receita ao centro de distribuicao
	@PostMapping("{id}/receitas")
	public ResponseEntity<CentroDistribuicao> insertReceitaIntoCentro(@PathVariable Long id,
			@RequestBody @Valid Receita receita) {
		CentroDistribuicao centroDistribuicao = getCentroDistribuicao(id);
		var listaReceitas = centroDistribuicao.getReceitas();
		if (listaReceitas.size() == 2) {
			throw new BadRequestException("Lista de receitas diárias está lotada, nao é possivel adicionar uma nova");
		}

		listaReceitas.add(receita);

		centroDistribuicao.setReceitas(listaReceitas);
		repository.save(centroDistribuicao);

		return ResponseEntity.status(HttpStatus.CREATED).body(centroDistribuicao);
	}

	// limpa as receitas do centro de distribuicao
	@DeleteMapping("{id}/receitas")
	public ResponseEntity<CentroDistribuicao> destroyReceitas(@PathVariable Long id) {
		CentroDistribuicao centroDistribuicao = getCentroDistribuicao(id);

		centroDistribuicao.setReceitas(null);
		repository.save(centroDistribuicao);

		return ResponseEntity.noContent().build();
	}

	private CentroDistribuicao getCentroDistribuicao(Long id) {
		return repository.findById(id).orElseThrow(() -> new RestNotFoundException("CentroDistribuicao nao encontrado"));
	}
}
