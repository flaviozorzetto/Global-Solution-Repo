package br.com.fiap.gsproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.gsproject.models.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
