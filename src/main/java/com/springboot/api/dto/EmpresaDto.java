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
public class EmpresaDto {
	
	
    private String razaoSocial;
    private  String cnpj;
    private Endereco  endereco;
    private Long idConta;

    private Integer agencia;
    private String conta;
    private Integer saldo;
    private String  tipoConta;
}
