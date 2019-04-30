package com.example.demo.repository.catalogos;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.catalogos.TipoUsuario;
/**
 * <code>EstatusRepository</code>.
 *
 * @author ${eduardo.santiago}
 * @version 1.0
 */
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> 
{
	Optional<TipoUsuario> findById(Long id);
	List<TipoUsuario> findAll();
}
