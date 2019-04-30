package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "estatus")
public class Estatus 
{
	@Id
    @Column(name = "id")
    private Integer id;

    /** descripcion. */
    @NotBlank
    @Column(name = "descripcion")
    private String descripcion;
}
