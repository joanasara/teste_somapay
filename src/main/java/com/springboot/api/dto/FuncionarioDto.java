package com.springboot.api.dto;

import com.springboot.api.model.Endereco;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
public class FuncionarioDto {

    private String nome;
    private  String cpf;
    private String email;
    private String telefone;
    private Endereco endereco;
    private Long idConta;
    private Long idEmpresa;
    

    private Integer agencia;
    private String  conta;
    private Integer saldo;
    private String  tipoConta;
}
