/* ----------------------------------------------------------------------------
 * All rights reserved © 2018 Multiva.
 *  
 * This software contains information that is exclusive property of 
 * Multiva, this information is considered confidential.
 * It is strictly forbidden the copy or spreading of any part of this document 
 * in any format, whether mechanic or electronic.
 * ---------------------------------------------------------------------------
 * File name: Perfil.java
 * Original Author: StratPlus
 * Creation Date: 17/10/2018
 * ---------------------------------------------------------------------------
 */
package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <code>Perfil</code>.
 *
 * @author ${eduardo.santiago}
 * @version 1.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "perfil")
public class Perfil 
{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
	@Column( name ="descripcion")
    private String descripcion;

}
