package com.example.demo.repository.catalogos;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.catalogos.Estatus;
/**
 * <code>EstatusRepository</code>.
 *
 * @author ${eduardo.santiago}
 * @version 1.0
 */
public interface EstatusRepository extends JpaRepository<Estatus, Long> 
{
	Optional<Estatus> findById(Long id);
	List<Estatus> findAll();
}
