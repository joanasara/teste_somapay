package com.springboot.api.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.api.model.Funcionario;
import com.springboot.api.repositorio.empresa.FuncionarioRepositoryQuery;

public interface FuncionarioRepositorio extends JpaRepository<Funcionario, Long>,FuncionarioRepositoryQuery {

}
