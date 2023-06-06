package br.com.fiap.gsproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.gsproject.models.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

}
