package com.example.financeiro.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.financeiro.api.model.Pessoa;
import com.example.financeiro.api.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	 private PessoaRepository pessoaRepository;
	 
	 public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		 Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);

		 if(pessoaSalva == null) {
			 
			 throw new EmptyResultDataAccessException(1);
		 }
		 BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
		 
		 pessoaRepository.save(pessoaSalva);
		 return pessoaSalva;
		 
				 
	 }
	 
	 private Pessoa buscarPessoaPeloCodigo(Long codigo) {
			Pessoa pessoaSalva = pessoaRepository.findById(codigo)
					.orElseThrow(() -> new EmptyResultDataAccessException(1));
			return pessoaSalva;
		}

}
