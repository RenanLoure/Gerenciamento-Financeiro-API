package com.example.financeiro.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financeiro.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	void save(Optional<Pessoa> pessoaSalva);

}
