package com.example.demo.domain.reglas;

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
import com.example.demo.domain.catalogos.Banco;
import com.example.demo.domain.catalogos.TipoPersona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Persona")
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Persona   implements Serializable
{
	private static final long serialVersionUID = 8334290105021947380L;
	@Id
    @SequenceGenerator(
            name = "sequence_persona",
            sequenceName = "sequence_persona",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_persona"
    )
    private Long id;
	@OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idtipopersona", referencedColumnName = "id")
    private TipoPersona idtipopersona; //Por Default 
	@OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idusuario", referencedColumnName = "id")
    private User idusuario; //Por Default
    private String  rfc;
    private String nacionalidad;
    private String clave;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idbanco", referencedColumnName = "id")
    private Banco idbanco;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idcuenta", referencedColumnName = "id")
    private Cuenta idcuenta; //Por Default 
    //----------------------- Auditoria
    private Timestamp creado;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "creadopor", referencedColumnName = "id")
    private User creadopor;
    private Timestamp modificado;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "modificadopor", referencedColumnName = "id")
    private User modificadopor;
    
    public Persona(User user,String clave,String nacionalidad,String rfc,
    		Banco banco,TipoPersona person) 
	{
		super();
		Date creacion = new Date(System.currentTimeMillis());
        Timestamp  creacionStamp = (new Timestamp(creacion.getTime()));
        this.creadopor =  user;
        this.modificadopor=user;
        this.creado = creacionStamp ;
        this.modificado = creacionStamp ;
        this.idusuario = user;
        this.clave = clave;
        this.nacionalidad =  nacionalidad;
        this.rfc=rfc;
        this.idbanco =  banco;
        this.idtipopersona =  person;
	}
}
