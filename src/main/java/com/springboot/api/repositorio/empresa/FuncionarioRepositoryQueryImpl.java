package com.springboot.api.repositorio.empresa;

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
import org.springframework.util.StringUtils;


import com.springboot.api.model.Funcionario;
import com.springboot.api.repositorio.filter.FuncionarioFilter;

public class FuncionarioRepositoryQueryImpl implements FuncionarioRepositoryQuery{
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Funcionario> filtrar(FuncionarioFilter empresaFilter, Pageable pageable) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Funcionario> criteria = builder.createQuery(Funcionario.class);
		Root<Funcionario> root = criteria.from(Funcionario.class);

		Predicate[] predicates = criarRestricoes(empresaFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Funcionario> query = em.createQuery(criteria);

		adicionarRestricoesPaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(empresaFilter));
	}
	

	@SuppressWarnings("deprecation")
	private Predicate[] criarRestricoes(FuncionarioFilter funcionarioFilter, CriteriaBuilder builder, Root<Funcionario> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(funcionarioFilter.getNome())) {
			predicates.add(
					builder.like(builder.lower(root.get("nome")), "%" + funcionarioFilter.getNome().toLowerCase() + "%"));
		}

		if (!StringUtils.isEmpty(funcionarioFilter.getEmail())) {
			predicates.add(builder.equal(root.get("email"), funcionarioFilter.getEmail()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionarRestricoesPaginacao(TypedQuery<Funcionario> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPagina = pageable.getPageSize();
		int primeiroRegistroPagina = paginaAtual * totalRegistrosPagina;

		query.setFirstResult(primeiroRegistroPagina);
		query.setMaxResults(totalRegistrosPagina);
	}

	private Long total(FuncionarioFilter funcionarioFilter) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Funcionario> root = criteria.from(Funcionario.class);

		Predicate[] predicates = criarRestricoes(funcionarioFilter, builder, root);
		criteria.where(predicates);

		criteria.select(builder.count(root));

		return em.createQuery(criteria).getSingleResult();
	}



}
