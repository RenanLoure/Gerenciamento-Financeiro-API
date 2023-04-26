package com.example.financeiro.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ObjectUtils;

import com.example.financeiro.api.model.Lancamento;
import com.example.financeiro.api.repository.filter.LancamentoFilter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	
	@PersistenceContext
	private EntityManager menager;
	
	@Override
	public List<Lancamento> filtar(LancamentoFilter lancamentoFilter) {
		
		CriteriaBuilder builder = menager.getCriteriaBuilder();
		
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		//Criar as restrições 
		
		Predicate[]  predicades = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicades);
		
		TypedQuery<Lancamento> query = menager.createQuery(criteria);
		return query.getResultList();
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		// where descricao like % %
		
		if(!ObjectUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get("descricao")), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}
		
        if(lancamentoFilter.getDataVencimentoAte() != null) {
        	predicates.add(builder.greaterThanOrEqualTo(root.get("dataVencimento"),lancamentoFilter.getDataVencimentoDe()));
		}
        
        if(lancamentoFilter.getDataVencimentoDe() != null) {
        	predicates.add(builder.lessThanOrEqualTo(root.get("dataVencimento"), lancamentoFilter.getDataVencimentoAte()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
