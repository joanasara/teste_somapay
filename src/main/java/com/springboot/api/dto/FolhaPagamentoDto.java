package com.springboot.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
public class FolhaPagamentoDto {
	
	
	private Long id_empresa;
	private Integer salario;
}
