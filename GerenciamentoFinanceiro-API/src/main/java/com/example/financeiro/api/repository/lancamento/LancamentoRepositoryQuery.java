package com.example.financeiro.api.repository.lancamento;

import java.util.List;

import com.example.financeiro.api.model.Lancamento;
import com.example.financeiro.api.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	public List<Lancamento> filtar(LancamentoFilter lancamentoFilter);
	
}
