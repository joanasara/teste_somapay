package com.springboot.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.api.dto.EmpresaDto;
import com.springboot.api.dto.FolhaPagamentoDto;
import com.springboot.api.exceptions.EmpresaNaoEncontradaExceptio;
import com.springboot.api.exceptions.RemoverEmpresaException;
import com.springboot.api.model.ContaCorrente;
import com.springboot.api.model.Empresa;
import com.springboot.api.model.Funcionario;
import com.springboot.api.model.Tipo;
import com.springboot.api.repositorio.ContaCorrenteRepositorio;
import com.springboot.api.repositorio.EmpresaRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpresaService {

	@Autowired
	private EmpresaRepositorio empresaRepository;

	@Autowired
    private FolhaPagamento folhaPagamento;

	@Autowired
	private ContaCorrenteRepositorio contaCorrenteRepositorio ;
	
	
	public Empresa buscar(Long id) {
		return empresaRepository.findById(id)
				.orElseThrow(() -> new EmpresaNaoEncontradaExceptio("A empresa não pôde ser encontrada!"));
	}

	private void verificarExistencia(Empresa empresa) {
		buscar(empresa.getId());
	}

	public Empresa salvar(Empresa empresa) {
		return empresaRepository.save(empresa);
	}

	public void alterar(Empresa empresa) {
		verificarExistencia(empresa);
		empresaRepository.save(empresa);
	}

	public void deletar(Long id) {
		Optional<Empresa> empresa = empresaRepository.findById(id);

		empresa.map(e -> new Empresa())
				.orElseThrow(() -> new EmpresaNaoEncontradaExceptio("A empresa não pôde ser encontrada!"));

		empresa.filter(e -> e.getTipo() != Tipo.MATRIZ)
				.orElseThrow(() -> new RemoverEmpresaException("Empresa Matriz não pode ser removida!"));

		empresaRepository.delete(empresa.get());
	}
	

	 public ContaCorrente createContaCorrente(Integer agencia, String conta, double saldo, String tipoConta){
	        ContaCorrente contaCorrente = new ContaCorrente();
	        contaCorrente.setAgencia(agencia);
	        contaCorrente.setConta(conta);
	        contaCorrente.setSaldo(saldo);
	        contaCorrente.setTipoConta(tipoConta);
	        return contaCorrenteRepositorio.save(contaCorrente);

	    }

	    public void updateContaCorrente(ContaCorrente conta, EmpresaDto empresaUpdate){

	    	contaCorrenteRepositorio.findById(conta.getId())
	                .map(contaCorrente -> {
	                    contaCorrente.setConta(empresaUpdate.getConta());
	                    contaCorrente.setAgencia(empresaUpdate.getAgencia());
	                    contaCorrente.setTipoConta(empresaUpdate.getTipoConta());
	                    contaCorrente.setSaldo(empresaUpdate.getSaldo());
	                    return contaCorrenteRepositorio.save(contaCorrente);
	                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

	    }

	    public void atualizaSaldo(Optional<Empresa> empresa, Integer totalfolha,  double taxa){

	        double valorTotal = empresa.get().getContaCorrente().getSaldo() - folhaPagamento.calcularTotalDescontos(totalfolha,taxa);
	        contaCorrenteRepositorio.findById(empresa.get().getContaCorrente().getId())
	                .map(contaCorrente -> {
	                    contaCorrente.setConta(empresa.get().getContaCorrente().getConta());
	                    contaCorrente.setAgencia(empresa.get().getContaCorrente().getAgencia());
	                    contaCorrente.setConta(empresa.get().getContaCorrente().getTipoConta());
	                    contaCorrente.setSaldo((int) valorTotal);
	                    return contaCorrenteRepositorio.save(contaCorrente);
	                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

	    }

	    public void pagamentoFuncionario(Funcionario funcionario, FolhaPagamentoDto folhaPagamentoDto){
	    	contaCorrenteRepositorio.findById(funcionario.getContaCorrente().getId())
	                .map(contaCorrente -> {
	                    contaCorrente.setConta(funcionario.getContaCorrente().getConta());
	                    contaCorrente.setAgencia(funcionario.getContaCorrente().getAgencia());
	                    contaCorrente.setConta(funcionario.getContaCorrente().getTipoConta());
	                    contaCorrente.setSaldo(folhaPagamentoDto.getSalario());
	                    return contaCorrenteRepositorio.save(contaCorrente);
	                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	    }
}