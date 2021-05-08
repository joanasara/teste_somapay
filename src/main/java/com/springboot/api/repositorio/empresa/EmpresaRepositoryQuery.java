package com.springboot.api.repositorio.empresa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.api.model.Empresa;
import com.springboot.api.repositorio.filter.EmpresaFilter;

public interface EmpresaRepositoryQuery {
	public Page<Empresa> filtrar(EmpresaFilter empresaFilter, Pageable pageable);

}
