package com.springboot.api.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.api.dto.EmpresaDto;
import com.springboot.api.dto.FolhaPagamentoDto;
import com.springboot.api.model.ContaCorrente;
import com.springboot.api.model.Empresa;
import com.springboot.api.model.Endereco;
import com.springboot.api.model.Funcionario;
import com.springboot.api.model.Tipo;
import com.springboot.api.repositorio.ContaCorrenteRepositorio;
import com.springboot.api.repositorio.EmpresaRepositorio;
import com.springboot.api.repositorio.FuncionarioRepositorio;
import com.springboot.api.repositorio.filter.EmpresaFilter;
import com.springboot.api.service.EmpresaService;
import com.springboot.api.service.FolhaPagamento;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/empresas")
public class EmpresaController {

	@Autowired
	private FuncionarioRepositorio funcionarioRepo;

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private EmpresaRepositorio empresaRepository;

	@Autowired
	private ContaCorrenteRepositorio contCorrenteRepos;

	@Autowired
	private FolhaPagamento folhaPagamento;

	@GetMapping
	public Page<Empresa> listar(EmpresaFilter empresaFilter, Pageable pageable) {
		return empresaRepository.filtrar(empresaFilter, pageable);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Empresa> buscarPeloCodigo(@PathVariable Long codigo) {
		Empresa empresa = empresaService.buscar(codigo);
		return ResponseEntity.ok(empresa);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Empresa criar(@RequestBody @Valid EmpresaDto empresaTto) {

		ContaCorrente conta = empresaService.createContaCorrente(empresaTto.getAgencia(), empresaTto.getConta(),
				empresaTto.getSaldo(), empresaTto.getTipoConta());

		Optional<ContaCorrente> contCorrenteOptional = contCorrenteRepos.findById(conta.getId());
		ContaCorrente contaCorrente = contCorrenteOptional.orElse(new ContaCorrente());

		Empresa empresa = new Empresa();
		empresa.setRazaoSocial(empresaTto.getRazaoSocial());
		empresa.setCnpj(empresaTto.getCnpj());
		empresa.setEnderecoEmpresa(empresaTto.getEndereco());
		empresa.setContaCorrente(contaCorrente);

		return empresaRepository.save(empresa);
	}

	@PutMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateEmpresa(@PathVariable Long codigo, @RequestBody @Valid EmpresaDto empresaUpdate) {
		empresaRepository.findById(codigo).map(empresa -> {
			empresa.setRazaoSocial(empresaUpdate.getRazaoSocial());
			empresa.setCnpj(empresaUpdate.getCnpj());
			empresa.setEnderecoEmpresa(empresaUpdate.getEndereco());

			ContaCorrente contaCorrente = empresa.getContaCorrente();

			empresaService.updateContaCorrente(contaCorrente, empresaUpdate);
			return empresaRepository.save(empresa);
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> deletar(@PathVariable("codigo") Long codigo) {
		empresaService.deletar(codigo);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/saldo/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Object> getSaldoEmpresa(@PathVariable Long codigo) {
		Double saldoEmpresa = empresaRepository.findById(codigo).map(saldo -> {
			return saldo.getContaCorrente().getSaldo();
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

		return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("saldo_empresa", saldoEmpresa));
	}

	@PostMapping("/pagar-funcionarios")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> pagarFuncionarios(@RequestBody FolhaPagamentoDto folhaPagamentoDto) {

		List<Funcionario> funcionarios = funcionarioRepo.findAll();
		Optional<Empresa> empresa = empresaRepository.findById(folhaPagamentoDto.getId_empresa());
		Integer totalfolha = folhaPagamento.calculaFolhaPagamento(funcionarios, folhaPagamentoDto);
		return folhaPagamento.sendPagamento(totalfolha, funcionarios, folhaPagamentoDto, empresa);

	}

	@GetMapping(value = "/endereco/{cep}")
	public ResponseEntity<Endereco> buscarEnderecoPeloCep(@PathVariable(name = "cep") String cep) {
		RestTemplate restTemplate = new RestTemplate();

		String uri = "http://viacep.com.br/ws/{cep}/json/";

		Map<String, String> params = new HashMap<String, String>();
		params.put("cep", cep);

		Endereco endereco = restTemplate.getForObject(uri, Endereco.class, params);

		return new ResponseEntity<Endereco>(endereco, HttpStatus.OK);
	}

	@GetMapping("/tipos")
	public List<Tipo> listarTipos() {
		return Arrays.asList(Tipo.values());
	}

}