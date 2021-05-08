package com.springboot.api.model;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
public class ContaCorrente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String TipoConta;

    @Column(nullable = false,  length = 40)
    private Integer agencia;

    @Column(nullable = false, length = 2)
    private String conta;

    @Column(nullable = false)
    private double saldo;


}
