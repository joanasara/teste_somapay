package com.springboot.api.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@Entity
public class Usuario {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotEmpty(message = "Campo username e obrigatorio")
    private String username;

    @Column(unique = true, name = "email")
    @NotEmpty(message = "Campo Email e obrigatorio")
    @Email(message = "Email invalido")
    private String email;

    @Column
    @NotNull(message = "Campo password e obrigatorio")
    private String password;
}
