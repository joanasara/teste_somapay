package com.springboot.api.exceptions;

public class RemoveFuncionarioException  extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public  RemoveFuncionarioException(String mensagem) {
		super(mensagem);
	}
}
