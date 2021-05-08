package com.springboot.api.exceptions;

public class EmpresaNaoEncontradaExceptio extends RuntimeException {

	private static final long serialVersionUID = 1L;
  
	public EmpresaNaoEncontradaExceptio(String mensagem) {
		super(mensagem);
	}
}
