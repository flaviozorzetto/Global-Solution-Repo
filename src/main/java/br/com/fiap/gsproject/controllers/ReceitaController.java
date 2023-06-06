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
import br.com.fiap.gsproject.models.Receita;
import br.com.fiap.gsproject.repository.ReceitaRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/receita")
public class ReceitaController {
	Logger log = LoggerFactory.getLogger(ReceitaController.class);

	@Autowired
	ReceitaRepository repository;

	@GetMapping
	public List<Receita> index() {
		return repository.findAll();
	}

	@PostMapping
	public ResponseEntity<Receita> create(@RequestBody @Valid Receita receita) {
		repository.save(receita);

		return ResponseEntity.status(HttpStatus.CREATED).body(receita);
	}

	@GetMapping("{id}")
	public ResponseEntity<Receita> show(@PathVariable Long id) {
		var Receita = getReceita(id);

		return ResponseEntity.ok(Receita);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Receita> destroy(@PathVariable Long id) {
		var receita = getReceita(id);

		repository.delete(receita);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("{id}")
	public ResponseEntity<Receita> update(@PathVariable Long id, @Valid @RequestBody Receita receita) {
		getReceita(id);

		receita.setId(id);
		repository.save(receita);

		return ResponseEntity.ok(receita);
	}

	private Receita getReceita(Long id) {
		return repository.findById(id).orElseThrow(() -> new RestNotFoundException("Receita nao encontrada"));
	}
}
