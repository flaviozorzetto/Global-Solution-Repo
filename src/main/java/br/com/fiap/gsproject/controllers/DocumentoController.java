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

	@GetMapping
	public List<Documento> index() {
		return repository.findAll();
	}

	@PostMapping
	public ResponseEntity<Documento> create(@RequestBody @Valid Documento documento) {
		repository.save(documento);

		return ResponseEntity.status(HttpStatus.CREATED).body(documento);
	}

	@GetMapping("{id}")
	public ResponseEntity<Documento> show(@PathVariable Long id) {
		var Documento = getDocumento(id);

		return ResponseEntity.ok(Documento);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Documento> destroy(@PathVariable Long id) {
		var documento = getDocumento(id);

		repository.delete(documento);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("{id}")
	public ResponseEntity<Documento> update(@PathVariable Long id, @Valid @RequestBody Documento documento) {
		getDocumento(id);

		documento.setId(id);
		repository.save(documento);

		return ResponseEntity.ok(documento);
	}

	private Documento getDocumento(Long id) {
		return repository.findById(id).orElseThrow(() -> new RestNotFoundException("Documento nao encontrado"));
	}
}
