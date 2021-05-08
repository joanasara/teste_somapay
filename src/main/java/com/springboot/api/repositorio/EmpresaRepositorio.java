package com.springboot.api.repositorio;



import org.springframework.data.jpa.repository.JpaRepository;


import com.springboot.api.model.Empresa;
import com.springboot.api.repositorio.empresa.EmpresaRepositoryQuery;

public interface EmpresaRepositorio extends JpaRepository<Empresa, Long>,  EmpresaRepositoryQuery {

	

}
