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
import br.com.fiap.gsproject.models.Endereco;
import br.com.fiap.gsproject.repository.EnderecoRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/endereco")
public class EnderecoController {
	Logger log = LoggerFactory.getLogger(EnderecoController.class);

	@Autowired
	EnderecoRepository repository;

	@GetMapping
	public List<Endereco> index() {
		return repository.findAll();
	}

	@PostMapping
	public ResponseEntity<Endereco> create(@RequestBody @Valid Endereco endereco) {
		repository.save(endereco);

		return ResponseEntity.status(HttpStatus.CREATED).body(endereco);
	}

	@GetMapping("{id}")
	public ResponseEntity<Endereco> show(@PathVariable Long id) {
		var Endereco = getEndereco(id);

		return ResponseEntity.ok(Endereco);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Endereco> destroy(@PathVariable Long id) {
		var endereco = getEndereco(id);

		repository.delete(endereco);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("{id}")
	public ResponseEntity<Endereco> update(@PathVariable Long id,
			@Valid @RequestBody Endereco endereco) {
		getEndereco(id);

		endereco.setId(id);
		repository.save(endereco);

		return ResponseEntity.ok(endereco);
	}

	private Endereco getEndereco(Long id) {
		return repository.findById(id).orElseThrow(() -> new RestNotFoundException("Endereco nao encontrado"));
	}
}
