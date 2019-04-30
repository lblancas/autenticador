package com.example.demo.repository.catalogos;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.catalogos.TipoPersona;
/**
 * <code>EstatusRepository</code>.
 *
 * @author ${eduardo.santiago}
 * @version 1.0
 */
public interface TipoPersonaRepository extends JpaRepository<TipoPersona, Long> 
{
	Optional<TipoPersona> findById(Long id);
	List<TipoPersona> findAll();
}
