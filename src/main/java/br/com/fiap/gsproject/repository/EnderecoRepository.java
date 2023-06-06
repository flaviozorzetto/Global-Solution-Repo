package br.com.fiap.gsproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.gsproject.models.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}
