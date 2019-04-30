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
import com.example.demo.domain.catalogos.Estatus;
import com.example.demo.domain.catalogos.TipoUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Cuenta")
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta   implements Serializable
{
	private static final long serialVersionUID = 8334290105021947380L;
	@Id
    @SequenceGenerator(
            name = "sequence_cuenta",
            sequenceName = "sequence_cuenta",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_cuenta"
    )
    private Long id;
	@OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idestatus", referencedColumnName = "id")
    private Estatus idestatus;
	
	@OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idtipousuario", referencedColumnName = "id")
    private TipoUsuario idtipousuario;  
		
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idpersona", referencedColumnName = "id")
    private Persona idpersona; 
    
    //----------------------- Auditoria
    private Timestamp creado;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "creadopor", referencedColumnName = "id")
    private User creadopor;
    private Timestamp modificado;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "modificadopor", referencedColumnName = "id")
    private User modificadopor;
    
    public Cuenta(User user,TipoUsuario idtipousuario,Persona idpersona) 
	{
		super();
		Date creacion = new Date(System.currentTimeMillis());
        Timestamp  creacionStamp = (new Timestamp(creacion.getTime()));
        this.creadopor =  user;
        this.modificadopor=user;
        this.creado = creacionStamp ;
        this.modificado = creacionStamp ;
        this.idtipousuario = idtipousuario;
        this.idpersona =  idpersona;
	}
}
