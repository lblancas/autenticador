package com.example.demo.repository.reglas;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.User;
import com.example.demo.domain.reglas.Persona;
public interface PersonaRepository  extends JpaRepository<Persona, Long> 
{
	Optional<Persona> findByIdusuario(User u);
	Optional<Persona> findById(Long id);
	List<Persona> findAll();
}
