package br.com.fiap.gsproject.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import br.com.fiap.gsproject.exceptions.BadRequestException;
import br.com.fiap.gsproject.exceptions.RestNotFoundException;
import br.com.fiap.gsproject.models.Documento;
import br.com.fiap.gsproject.models.Pessoa;
import br.com.fiap.gsproject.repository.PessoaRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/pessoa")
public class PessoaController {
	Logger log = LoggerFactory.getLogger(PessoaController.class);

	@Autowired
	PessoaRepository repository;

	@Autowired
	PagedResourcesAssembler<Object> assembler;

	@GetMapping
	public PagedModel<EntityModel<Object>> index(@PageableDefault(size = 5) Pageable pageable) {
		Page<Pessoa> pessoas = repository.findAll(pageable);

		return assembler.toModel(pessoas.map(Pessoa::toEntityModel));
	}

	@GetMapping("{id}")
	public EntityModel<Pessoa> show(@PathVariable Long id) {
		return getPessoa(id).toEntityModel();
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Valid Pessoa pessoa) {
		repository.save(pessoa);

		return ResponseEntity
				.created(pessoa.toEntityModel().getRequiredLink("self").toUri()).body(pessoa.toEntityModel());
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Pessoa> destroy(@PathVariable Long id) {
		var pessoa = getPessoa(id);

		repository.delete(pessoa);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("{id}")
	public EntityModel<Pessoa> update(@PathVariable Long id, @Valid @RequestBody Pessoa pessoa) {
		Pessoa pessoaEncontrada = getPessoa(id);
		pessoaEncontrada.setNomePessoa(pessoa.getNomePessoa());
		pessoaEncontrada.setValorAltura(pessoa.getValorAltura());
		pessoaEncontrada.setValorPeso(pessoa.getValorPeso());
		pessoaEncontrada.setValorIdade(pessoa.getValorIdade());

		repository.save(pessoaEncontrada);

		return pessoaEncontrada.toEntityModel();
	}

	// metodos relacionados ao documento da pessoa

	// adicionar um documento a pessoa
	@PostMapping("{id}/documento")
	public ResponseEntity<Object> insertDocumento(@PathVariable Long id, @RequestBody @Valid Documento documento) {
		Pessoa pessoa = getPessoa(id);
		if (pessoa.getDocumento() != null) {
			throw new BadRequestException("Documento ja cadastrado para essa pessoa");
		}

		pessoa.setDocumento(documento);

		repository.save(pessoa);

		return ResponseEntity
				.created(pessoa.toEntityModel().getRequiredLink("self").toUri()).body(pessoa.toEntityModel());
	}

	// remover o documento da pessoa
	@DeleteMapping("{id}/documento")
	public ResponseEntity<Pessoa> destroyDocumento(@PathVariable Long id) {
		var pessoa = getPessoa(id);

		pessoa.setDocumento(null);
		repository.save(pessoa);

		return ResponseEntity.noContent().build();
	}

	private Pessoa getPessoa(Long id) {
		return repository.findById(id).orElseThrow(() -> new RestNotFoundException("Pessoa nao encontrada"));
	}
}
