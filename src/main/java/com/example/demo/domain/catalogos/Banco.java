package com.example.demo.domain.catalogos;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.demo.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Banco")
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Banco   implements Serializable
{
	private static final long serialVersionUID = 8334290105021947380L;
	@Id
    @SequenceGenerator(
            name = "sequence_bancotr",
            sequenceName = "sequence_bancotr",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_bancotr"
    )
    private Long id;
	@NotBlank(message = "Campo requerido")
    @Size(min=1, max=250 )
    private String descripcion;
	@Size(min=1, max=5 )
    private String clave;
	private String corta;
    //----------------------- Auditoria
    private Timestamp creado;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "creadopor", referencedColumnName = "id")
    private User creadopor;
    private Timestamp modificado;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "modificadopor", referencedColumnName = "id")
    private User modificadopor;
	public Banco(String descripcion0,String corta0,String clabe,User user) 
	{
		this.descripcion = descripcion0;
		this.corta =  corta0;
		Date creacion = new Date(System.currentTimeMillis());
        Timestamp  creacionStamp = (new Timestamp(creacion.getTime()));
        this.creadopor =  user;
        this.modificadopor=user;
        this.creado = creacionStamp ;
        this.modificado = creacionStamp ;
        
	}
    
}
