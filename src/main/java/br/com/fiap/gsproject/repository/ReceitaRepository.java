package br.com.fiap.gsproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.gsproject.models.Receita;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {

}
