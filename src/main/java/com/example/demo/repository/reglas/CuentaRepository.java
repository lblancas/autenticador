package com.example.demo.repository.reglas;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.catalogos.TipoUsuario;
import com.example.demo.domain.reglas.Cuenta;
import com.example.demo.domain.reglas.Persona;
public interface CuentaRepository extends JpaRepository<Cuenta, Long> 
{
	Optional<Cuenta> findById(Long id);
	List<Cuenta> findByIdpersona(Persona p);
	Optional<Cuenta> findByIdpersonaAndIdtipousuario(Persona p,TipoUsuario t);
	List<Cuenta> findAll();
	
}
