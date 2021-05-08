package com.springboot.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.api.model.ContaCorrente;
import com.springboot.api.repositorio.ContaCorrenteRepositorio;

@RestController
@RequestMapping("/api/contas")
public class ContaCorrenteController {

    @Autowired
    private  ContaCorrenteRepositorio ContaCorrenteRepositorio;

    
    @PostMapping
    @ResponseStatus
    public ContaCorrente create(ContaCorrente contaCorrente){
            return  ContaCorrenteRepositorio.save(contaCorrente);
    }
}
