package com.springboot.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@Entity
public class Endereco implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "*Estado é obrigatório")
	@Size(max = 40, message = "O estado deve conter no máximo 40 caracteres")
	@Column(length = 50, nullable = false)
	private String estado;

	@NotBlank(message = "*Cidade é obrigatória")
	@Size(max = 40, message = "A cidade deve conter no máximo 40 caracteres")
	@Column(length = 50, nullable = false)
	private String cidade;

	@NotBlank(message = "*CEP é obrigatório")
	@Size(max = 8, message = "O CEP deve conter 8")
	@Column(length = 50, nullable = false)
	private String cep;

	@NotBlank(message = "*Bairro é obrigatório")
	@Size(max = 40, message = "O bairro deve conter no máximo 40 caracteres")
	@Column(length = 50, nullable = false)
	private String bairro;

	@NotBlank(message = "*Logradouro é obrigatório")
	@Size(max = 40, message = "O logradouro deve conter no máximo 40 caracteres")
	@Column(length = 50, nullable = false)
	private String logradouro;

	@Size(max = 40, message = "O complemento deve conter no máximo 40 caracteres")
	@Column(length = 50, nullable = false)
	private String complemento;

	@JsonIgnore
	@OneToMany(mappedBy = "endereco")
	private List<Funcionario> funcionarios = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "enderecoEmpresa")
	private List<Empresa> empresa = new ArrayList<>();

}
