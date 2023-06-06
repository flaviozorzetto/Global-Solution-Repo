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

	@GetMapping
	public List<Pessoa> index() {
		return repository.findAll();
	}

	@GetMapping("{id}")
	public ResponseEntity<Pessoa> show(@PathVariable Long id) {
		var Pessoa = getPessoa(id);

		return ResponseEntity.ok(Pessoa);
	}

	@PostMapping
	public ResponseEntity<Pessoa> create(@RequestBody @Valid Pessoa pessoa) {
		repository.save(pessoa);

		return ResponseEntity.status(HttpStatus.CREATED).body(pessoa);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Pessoa> destroy(@PathVariable Long id) {
		var pessoa = getPessoa(id);

		repository.delete(pessoa);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("{id}")
	public ResponseEntity<Pessoa> update(@PathVariable Long id, @Valid @RequestBody Pessoa pessoa) {
		Pessoa pessoaEncontrada = getPessoa(id);
		pessoaEncontrada.setNome_pessoa(pessoa.getNome_pessoa());
		pessoaEncontrada.setValor_altura(pessoa.getValor_altura());
		pessoaEncontrada.setValor_peso(pessoa.getValor_peso());

		repository.save(pessoaEncontrada);

		return ResponseEntity.ok(pessoaEncontrada);
	}

	// metodos relacionados ao documento da pessoa

	// adicionar um documento a pessoa
	@PostMapping("{id}/documento")
	public ResponseEntity<Pessoa> insertDocumento(@PathVariable Long id, @RequestBody @Valid Documento documento) {
		Pessoa pessoa = getPessoa(id);
		if (pessoa.getDocumento() != null) {
			throw new BadRequestException("Documento ja cadastrado para essa pessoa");
		}

		pessoa.setDocumento(documento);

		repository.save(pessoa);

		return ResponseEntity.status(HttpStatus.CREATED).body(pessoa);
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
