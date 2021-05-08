package com.springboot.api.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springboot.api.dto.FolhaPagamentoDto;
import com.springboot.api.model.Empresa;
import com.springboot.api.model.Funcionario;

public class FolhaPagamento {

	@Autowired
	private EmpresaService empresaService;

	public ResponseEntity<Object> sendPagamento( Integer totalfolha, List<Funcionario> funcionarios,
			FolhaPagamentoDto folhaPagamentoDto,Optional<Empresa> empresa) {

		if (totalfolha > empresa.get().getContaCorrente().getSaldo()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(Collections.singletonMap("Erro na Conta", "Saldo insuficiente"));
		} else {
			this.realizarPagamentoFuncionarios(funcionarios,folhaPagamentoDto);
			double taxaAdministracao = this.calcularTaxa(totalfolha);
			empresaService.atualizaSaldo(empresa, totalfolha, taxaAdministracao);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(Collections.singletonMap("Total_folha_pagamento", totalfolha)

					);
		}

	}

	public void realizarPagamentoFuncionarios(List<Funcionario> funcionarios, FolhaPagamentoDto folhaPagamentoDto) {
		for (Funcionario funcionario : funcionarios) {
			empresaService.pagamentoFuncionario(funcionario, folhaPagamentoDto);
		}

	}

	public Integer calculaFolhaPagamento(List<Funcionario> funcionarios, FolhaPagamentoDto folhaPagamentoDto) {
		Integer countSalario = Math.toIntExact(funcionarios.stream().count());
		Integer totalFolhaPagamento = countSalario * folhaPagamentoDto.getSalario();

		return totalFolhaPagamento;
	}

	public double calcularTaxa( Integer totalfolha) {
		double valoTaxa =  totalfolha * 0.30 / 100;
		return valoTaxa;
	}

	public Double calcularTotalDescontos(Integer totalFolha, double taxa) {
		double totalDescontos = totalFolha + taxa;
		return totalDescontos;
	}

}
