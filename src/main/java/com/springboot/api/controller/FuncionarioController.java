package com.springboot.api.controller;

import java.net.URI;
import java.util.Collections;
import java.util.NoSuchElementException;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.springboot.api.dto.FuncionarioDto;
import com.springboot.api.model.ContaCorrente;
import com.springboot.api.model.Empresa;
import com.springboot.api.model.Funcionario;
import com.springboot.api.repositorio.ContaCorrenteRepositorio;
import com.springboot.api.repositorio.EmpresaRepositorio;
import com.springboot.api.repositorio.FuncionarioRepositorio;
import com.springboot.api.repositorio.filter.FuncionarioFilter;
import com.springboot.api.service.FuncionarioService;

@CrossOrigin("*")
@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

	@Autowired
	private ContaCorrenteRepositorio contaCorrenteRepositorio;

	@Autowired
	private FuncionarioService funciorioService;

	@Autowired
	private FuncionarioRepositorio funcionarioRepositorio;

	@Autowired
	private EmpresaRepositorio empresaRepositorio;

	@GetMapping()
	public Page<Funcionario> listar(FuncionarioFilter funcionarioFilter, Pageable pageable) {
		return funcionarioRepositorio.filtrar(funcionarioFilter, pageable);
	}

	@PostMapping
	public ResponseEntity<Void> salvar(@RequestBody @Valid Funcionario funcionario) {
		try {
			funcionario = funciorioService.salvar(funcionario);

			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(funcionario.getId())
					.toUri();

			return ResponseEntity.created(uri).build();

		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Funcionario create(@RequestBody @Valid FuncionarioDto funcionarioDto) {

		ContaCorrente conta = funciorioService.createContaCorrente(funcionarioDto.getAgencia(),
				funcionarioDto.getConta(), funcionarioDto.getSaldo(), funcionarioDto.getTipoConta());

		Optional<ContaCorrente> contaCorrenteOptional = contaCorrenteRepositorio.findById(conta.getId());
		ContaCorrente contaCorrente = contaCorrenteOptional.orElse(new ContaCorrente());

		Long idEmpresa = funcionarioDto.getIdEmpresa();

		Empresa empresa = empresaRepositorio.findById(idEmpresa)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empresa Não existe"));

		Funcionario funcionario = new Funcionario();
		funcionario.setNome(funcionarioDto.getNome());
		funcionario.setCpf(funcionarioDto.getCpf());
		funcionario.setEndereco(funcionarioDto.getEndereco());
		funcionario.setEmpresa(empresa);
		funcionario.setContaCorrente(contaCorrente);

		return funcionarioRepositorio.save(funcionario);

	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarFuncionario(@PathVariable Long id, @RequestBody @Valid FuncionarioDto funcionarioUpdate) {
		funcionarioRepositorio.findById(id).map(funcionario -> {

			funcionario.setNome(funcionarioUpdate.getNome());
			funcionario.setEmail(funcionarioUpdate.getEmail());
			funcionario.setEmail(funcionarioUpdate.getTelefone());
			funcionario.setCpf(funcionarioUpdate.getCpf());
			funcionario.setEndereco(funcionarioUpdate.getEndereco());

			ContaCorrente contaCorrente = funcionario.getContaCorrente();

			funciorioService.updateContaCorrente(contaCorrente, funcionarioUpdate);
			return funcionarioRepositorio.save(funcionario);
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

	}

	 @GetMapping("/saldo/{id}")
	    public ResponseEntity<Object> getSaldoFuncionario(@PathVariable Long id){
	        Double saldoFuncionario = funcionarioRepositorio.findById(id).map(saldo -> {
	                    return  saldo.getContaCorrente().getSaldo();
	                })
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                        "Funcionario não encontrado"));

	        return ResponseEntity.status(HttpStatus.CREATED).body(
	                Collections.singletonMap("saldo_funcionario", saldoFuncionario));
	    }
	
	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> deletar(@PathVariable("codigo") Long codigo) {
		funciorioService.deletar(codigo);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Funcionario> buscarPorID(@PathVariable Long id) {
		try {
			Funcionario obj = funciorioService.buscar(id);
			return ResponseEntity.ok().body(obj);
		} catch (Exception e) {
			throw new NoSuchElementException("Registro não encontrado");
		}

	}

}
