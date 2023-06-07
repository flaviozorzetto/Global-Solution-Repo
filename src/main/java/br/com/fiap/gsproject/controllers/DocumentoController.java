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
import br.com.fiap.gsproject.models.Documento;
import br.com.fiap.gsproject.repository.DocumentoRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/documento")
public class DocumentoController {
	Logger log = LoggerFactory.getLogger(DocumentoController.class);

	@Autowired
	DocumentoRepository repository;

	@Autowired
	PagedResourcesAssembler<Object> assembler;

	@GetMapping
	public PagedModel<EntityModel<Object>> index(@PageableDefault(size = 5) Pageable pageable) {
		Page<Documento> documentos = repository.findAll(pageable);

		return assembler.toModel(documentos.map(Documento::toEntityModel));
	}

	@GetMapping("{id}")
	public EntityModel<Documento> show(@PathVariable Long id) {
		return getDocumento(id).toEntityModel();
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Valid Documento documento) {
		repository.save(documento);

		return ResponseEntity
				.created(documento.toEntityModel().getRequiredLink("self").toUri()).body(documento.toEntityModel());
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Documento> destroy(@PathVariable Long id) {
		var documento = getDocumento(id);

		repository.delete(documento);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("{id}")
	public EntityModel<Documento> update(@PathVariable Long id, @Valid @RequestBody Documento documento) {
		getDocumento(id);

		documento.setId(id);
		repository.save(documento);

		return documento.toEntityModel();
	}

	private Documento getDocumento(Long id) {
		return repository.findById(id).orElseThrow(() -> new RestNotFoundException("Documento nao encontrado"));
	}
}
