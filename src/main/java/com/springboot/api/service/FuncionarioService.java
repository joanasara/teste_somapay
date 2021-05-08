package com.springboot.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.api.dto.FuncionarioDto;
import com.springboot.api.exceptions.FuncionarioNaoEncontradoException;
import com.springboot.api.model.ContaCorrente;
import com.springboot.api.model.Funcionario;
import com.springboot.api.repositorio.ContaCorrenteRepositorio;
import com.springboot.api.repositorio.FuncionarioRepositorio;

@Service
public class FuncionarioService {
	
	@Autowired
	private  ContaCorrenteRepositorio contaCorrenteReposito;
	
	@Autowired
	private FuncionarioRepositorio Repository;

	public Funcionario buscar(Long id) {
		return Repository.findById(id)
				.orElseThrow(() -> new FuncionarioNaoEncontradoException("Funcionario não pôde ser encontrada (o)!"));
	}

	private void verificarExistencia( Funcionario funcionario) {
		buscar(funcionario.getId());
	}

	public Funcionario salvar( Funcionario funcionario) {
		return Repository.save(funcionario);
	}

	public void alterar( Funcionario funcionario) {
		verificarExistencia(funcionario);
		Repository.save(funcionario);
	}

	public void deletar(Long id) {
		Optional<Funcionario> funcionario = Repository.findById(id);

		funcionario.map(e -> new Funcionario())
				.orElseThrow(() -> new FuncionarioNaoEncontradoException("Funcionario não pôde ser encontrada (o)!"));

		
		Repository.delete(funcionario.get());
	}
	
	 public ContaCorrente createContaCorrente(Integer agencia, String conta, Integer saldo, String tipoConta){
	        ContaCorrente contaCorrente = new ContaCorrente();
	        contaCorrente.setAgencia(agencia);
	        contaCorrente.setConta(conta);
	        contaCorrente.setSaldo(saldo);
	        contaCorrente.setTipoConta(tipoConta);
	        return  contaCorrenteReposito.save(contaCorrente);

	    }

	    public void updateContaCorrente(ContaCorrente conta, FuncionarioDto funcionarioUpdate){

	    	contaCorrenteReposito.findById(conta.getId())
	                .map(contaCorrente -> {
	                    contaCorrente.setConta(funcionarioUpdate.getConta());
	                    contaCorrente.setAgencia(funcionarioUpdate.getAgencia());
	                    contaCorrente.setTipoConta(funcionarioUpdate.getTipoConta());
	                    contaCorrente.setSaldo(funcionarioUpdate.getSaldo());
	                    return contaCorrenteReposito.save(contaCorrente);
	                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

	    }
}
