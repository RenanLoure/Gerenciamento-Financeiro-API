package com.example.financeiro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financeiro.api.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>  {

}
