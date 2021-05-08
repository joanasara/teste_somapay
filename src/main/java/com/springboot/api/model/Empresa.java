package com.springboot.api.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CNPJ;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Empresa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "*CNPJ é obrigatório")
	@CNPJ(message = "CNPJ inválido")
	private String cnpj;

	@NotNull(message = "*Tipo é obrigatório")
	@Enumerated(EnumType.STRING)
	private Tipo tipo;

	@NotBlank(message = "*Nome é obrigatório")
	@Size(max = 50, message = "O nome deve conter no máximo 50 caracteres")
	private String nome;

	@NotBlank(message = "*Razão Social é obrigatória")
	@Size(max = 40, message = "A razão social deve conter no máximo 40 caracteres")
	private String razaoSocial;

	@NotBlank(message = "*Contato é obrigatório")
	@Size(max = 40, message = "O contato deve conter no máximo 40 caracteres")
	private String contato;

	@NotEmpty(message = "*E-mail é obrigatório")
	@Size(max = 40, message = "O e-mail deve conter no máximo 40 caracteres")
	@Email(message = "E-mail inválido")
	private String email;

	@JoinColumn(name = "id_endereco_empresa", nullable = false)
	@ManyToOne
	private Endereco enderecoEmpresa;

	@JoinColumn(name = "id_conta", nullable = false)
	@ManyToOne
	private ContaCorrente contaCorrente;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Empresa other = (Empresa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
