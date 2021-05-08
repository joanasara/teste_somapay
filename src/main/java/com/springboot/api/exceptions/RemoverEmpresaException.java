package com.springboot.api.exceptions;

public class RemoverEmpresaException extends RuntimeException{
   
	private static final long serialVersionUID = 1L;

	public RemoverEmpresaException(String mensagem) {
		super(mensagem);
	}
}
