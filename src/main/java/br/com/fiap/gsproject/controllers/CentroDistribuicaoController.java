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
import br.com.fiap.gsproject.models.CentroDistribuicao;
import br.com.fiap.gsproject.models.Pessoa;
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

	@GetMapping("{id}/pessoas")
	public ResponseEntity<List<Pessoa>> showPessoasFromCentro(@PathVariable Long id) {
		var CentroDistribuicao = getCentroDistribuicao(id);

		return ResponseEntity.ok(CentroDistribuicao.getPessoas());
	}

	@PostMapping
	public ResponseEntity<CentroDistribuicao> create(@RequestBody @Valid CentroDistribuicao centroDistribuicao) {
		repository.save(centroDistribuicao);

		return ResponseEntity.status(HttpStatus.CREATED).body(centroDistribuicao);
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

	private CentroDistribuicao getCentroDistribuicao(Long id) {
		return repository.findById(id).orElseThrow(() -> new RestNotFoundException("CentroDistribuicao nao encontrado"));
	}
}
