package br.com.fiap.gsproject.controllers;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import br.com.fiap.gsproject.exceptions.RestNotFoundException;
import br.com.fiap.gsproject.models.Endereco;
import br.com.fiap.gsproject.repository.EnderecoRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/endereco")
public class EnderecoController {
	Logger log = LoggerFactory.getLogger(EnderecoController.class);

	@Autowired
	EnderecoRepository repository;

	@Autowired
	PagedResourcesAssembler<Object> assembler;

	@GetMapping
	public PagedModel<EntityModel<Object>> index(@PageableDefault(size = 5) Pageable pageable) {
		Page<Endereco> enderecos = repository.findAll(pageable);

		return assembler.toModel(enderecos.map(Endereco::toEntityModel));
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Valid Endereco endereco) {
		repository.save(endereco);

		return ResponseEntity
				.created(endereco.toEntityModel().getRequiredLink("self").toUri()).body(endereco.toEntityModel());
	}

	@GetMapping("{id}")
	public EntityModel<Endereco> show(@PathVariable Long id) {
		return getEndereco(id).toEntityModel();
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Endereco> destroy(@PathVariable Long id) {
		var endereco = getEndereco(id);

		repository.delete(endereco);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("{id}")
	public EntityModel<Endereco> update(@PathVariable Long id,
			@Valid @RequestBody Endereco endereco) {
		getEndereco(id);

		endereco.setId(id);
		repository.save(endereco);

		return endereco.toEntityModel();
	}

	private Endereco getEndereco(Long id) {
		return repository.findById(id).orElseThrow(() -> new RestNotFoundException("Endereco nao encontrado"));
	}
}
