package com.springboot.api.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.springboot.api.model.DetalhesErro;


public class EmpresaControllerExceptionHandler {
 
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler( EmpresaNaoEncontradaExceptio.class)
	public ResponseEntity<DetalhesErro> handleEmpresaNaoEncontradaException( EmpresaNaoEncontradaExceptio ex) {

		DetalhesErro erro = new DetalhesErro();
		erro.setStatus(404);
		erro.setMensagem("A empresa não pôde ser encontrada!");
		erro.setTimestamp(System.currentTimeMillis());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}

	@ExceptionHandler(RemoverEmpresaException.class)
	public ResponseEntity<DetalhesErro> handleRemoverEmpresaException(RemoverEmpresaException ex) {

		DetalhesErro erro = new DetalhesErro();
		erro.setStatus(404);
		erro.setMensagem("Matriz não pode ser removida!");
		erro.setTimestamp(System.currentTimeMillis());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<DetalhesErro> handleMessageNotReadable(HttpMessageNotReadableException ex) {
		
		DetalhesErro erro = new DetalhesErro();
		erro.setStatus(400);
		erro.setMensagem("Campo informado não reconhecido!");
		erro.setTimestamp(System.currentTimeMillis());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
		
	}

	@ExceptionHandler()
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<DetalhesErro> erros = criarListaDeErros(ex.getBindingResult());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
	}
	
	private List<DetalhesErro> criarListaDeErros(BindingResult bindingResult){
		List<DetalhesErro> erros = new ArrayList<>();
			
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			DetalhesErro erro = new DetalhesErro();
			erro.setStatus(400);
			erro.setMensagem(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()));
			erro.setTimestamp(System.currentTimeMillis());
			erros.add(erro);
		}
		
		return erros;
	}
}
