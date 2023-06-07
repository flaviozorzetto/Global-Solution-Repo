package br.com.fiap.gsproject.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
import br.com.fiap.gsproject.models.Login;
import br.com.fiap.gsproject.repository.LoginRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/login")
public class LoginController {
	Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	LoginRepository repository;

	@Autowired
	PagedResourcesAssembler<Object> assembler;

	@GetMapping
	public PagedModel<EntityModel<Object>> index(@PageableDefault(size = 5) Pageable pageable) {
		Page<Login> logins = repository.findAll(pageable);

		return assembler.toModel(logins.map(Login::toEntityModel));
	}

	@GetMapping("{id}")
	public EntityModel<Login> show(@PathVariable Long id) {
		return getLogin(id).toEntityModel();
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Valid Login login) {
		repository.save(login);

		return ResponseEntity
				.created(login.toEntityModel().getRequiredLink("self").toUri()).body(login.toEntityModel());
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Login> destroy(@PathVariable Long id) {
		var login = getLogin(id);

		repository.delete(login);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("{id}")
	public EntityModel<Login> update(@PathVariable Long id, @Valid @RequestBody Login login) {
		getLogin(id);

		login.setId(id);
		repository.save(login);

		return login.toEntityModel();
	}

	private Login getLogin(Long id) {
		return repository.findById(id).orElseThrow(() -> new RestNotFoundException("Login nao encontrada"));
	}
}
