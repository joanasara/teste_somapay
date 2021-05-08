package com.springboot.api.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.api.model.ContaCorrente;

public interface ContaCorrenteRepositorio extends JpaRepository<ContaCorrente, Long>{

}
