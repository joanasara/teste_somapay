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

import com.springboot.api.model.Empresa;
import com.springboot.api.repositorio.filter.EmpresaFilter;



public class EmpresaRepositoryQueryImpl implements EmpresaRepositoryQuery {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Empresa> filtrar(EmpresaFilter empresaFilter, Pageable pageable) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Empresa> criteria = builder.createQuery(Empresa.class);
		Root<Empresa> root = criteria.from(Empresa.class);

		Predicate[] predicates = criarRestricoes(empresaFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Empresa> query = em.createQuery(criteria);

		adicionarRestricoesPaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(empresaFilter));
	}

	@SuppressWarnings("deprecation")
	private Predicate[] criarRestricoes(EmpresaFilter empresaFilter, CriteriaBuilder builder, Root<Empresa> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(empresaFilter.getNome())) {
			predicates.add(
					builder.like(builder.lower(root.get("nome")), "%" + empresaFilter.getNome().toLowerCase() + "%"));
		}

		if (!StringUtils.isEmpty(empresaFilter.getCnpj())) {
			predicates.add(builder.equal(root.get("cnpj"), empresaFilter.getCnpj()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionarRestricoesPaginacao(TypedQuery<Empresa> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPagina = pageable.getPageSize();
		int primeiroRegistroPagina = paginaAtual * totalRegistrosPagina;

		query.setFirstResult(primeiroRegistroPagina);
		query.setMaxResults(totalRegistrosPagina);
	}

	private Long total(EmpresaFilter empresaFilter) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Empresa> root = criteria.from(Empresa.class);

		Predicate[] predicates = criarRestricoes(empresaFilter, builder, root);
		criteria.where(predicates);

		criteria.select(builder.count(root));

		return em.createQuery(criteria).getSingleResult();
	}

}
