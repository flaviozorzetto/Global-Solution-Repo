package br.com.fiap.gsproject.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import br.com.fiap.gsproject.models.Receita;
import br.com.fiap.gsproject.repository.ReceitaRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/receita")
public class ReceitaController {
	Logger log = LoggerFactory.getLogger(ReceitaController.class);

	@Autowired
	ReceitaRepository repository;

	@Autowired
	PagedResourcesAssembler<Object> assembler;

	@GetMapping
	public PagedModel<EntityModel<Object>> index(@PageableDefault(size = 5) Pageable pageable) {
		Page<Receita> receitas = repository.findAll(pageable);

		return assembler.toModel(receitas.map(Receita::toEntityModel));
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Valid Receita receita) {
		repository.save(receita);

		return ResponseEntity
				.created(receita.toEntityModel().getRequiredLink("self").toUri()).body(receita.toEntityModel());
	}

	@GetMapping("{id}")
	public EntityModel<Receita> show(@PathVariable Long id) {
		return getReceita(id).toEntityModel();
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Receita> destroy(@PathVariable Long id) {
		var receita = getReceita(id);

		repository.delete(receita);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("{id}")
	public EntityModel<Receita> update(@PathVariable Long id, @Valid @RequestBody Receita receita) {
		getReceita(id);

		receita.setId(id);
		repository.save(receita);

		return receita.toEntityModel();
	}

	private Receita getReceita(Long id) {
		return repository.findById(id).orElseThrow(() -> new RestNotFoundException("Receita nao encontrada"));
	}
}
