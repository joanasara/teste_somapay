package com.springboot.api.repositorio.empresa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.springboot.api.model.Funcionario;
import com.springboot.api.repositorio.filter.FuncionarioFilter;

public interface FuncionarioRepositoryQuery {
	public Page<Funcionario> filtrar(FuncionarioFilter funcionarioFilter, Pageable pageable);
}
