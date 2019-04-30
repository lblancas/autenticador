package com.example.demo.domain.catalogos;

import java.io.Serializable;
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

import com.example.demo.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="TipoPersona")
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoPersona   implements Serializable
{
	private static final long serialVersionUID = 8334290105021947380L;
	@Id
    @SequenceGenerator(
            name = "sequence_tipopersona",
            sequenceName = "sequence_tipopersona",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_tipopersona"
    )
    private Long id;
    private String descripcion;  
    
    //----------------------- Auditoria
    private Timestamp creado;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "creadopor", referencedColumnName = "id")
    private User creadopor;
    private Timestamp modificado;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "modificadopor", referencedColumnName = "id")
    private User modificadopor;
	public TipoPersona(String descripcion,User user) 
	{
		super();
		this.descripcion = descripcion;
		Date creacion = new Date(System.currentTimeMillis());
        Timestamp  creacionStamp = (new Timestamp(creacion.getTime()));
        this.creadopor =  user;
        this.modificadopor=user;
        this.creado = creacionStamp ;
        this.modificado = creacionStamp ;
        
	}
}
