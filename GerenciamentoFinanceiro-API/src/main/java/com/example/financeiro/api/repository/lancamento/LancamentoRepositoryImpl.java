package com.example.financeiro.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import com.example.financeiro.api.model.Lancamento;
import com.example.financeiro.api.repository.filter.LancamentoFilter;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	
	@PersistenceContext
	private EntityManager menager;
	
	@Override
	public Page<Lancamento> filtar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		
		CriteriaBuilder builder = menager.getCriteriaBuilder();
		
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		//Criar as restrições 
		
		Predicate[]  predicades = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicades);
		
		TypedQuery<Lancamento> query = menager.createQuery(criteria);
		adiconarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>( query.getResultList(), pageable, total(lancamentoFilter));
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

	private void adiconarRestricoesDePaginacao(TypedQuery<Lancamento> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalDeRegistrosPorPagina = pageable.getPageSize();
		int primerioRestro = paginaAtual * totalDeRegistrosPorPagina;
		
		query.setFirstResult(primerioRestro);
		query.setMaxResults(totalDeRegistrosPorPagina);
	}
	
	private Long total(LancamentoFilter lancamentoFilter) {
	    CriteriaBuilder builder = menager.getCriteriaBuilder();
	    CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
	    Root<Lancamento> root = criteria.from(Lancamento.class);
	    
	    Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
	    criteria.where(predicates);
		
	    criteria.select(builder.count(root));
		return menager.createQuery(criteria).getSingleResult();
	}
}
