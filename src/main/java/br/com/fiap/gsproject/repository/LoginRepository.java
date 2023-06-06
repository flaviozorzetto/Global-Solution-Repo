package br.com.fiap.gsproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.gsproject.models.Login;

public interface LoginRepository extends JpaRepository<Login, Long> {

}
