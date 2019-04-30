package com.example.demo.repository.catalogos;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.catalogos.Banco;
/**
 * <code>EstatusRepository</code>.
 *
 * @author ${eduardo.santiago}
 * @version 1.0
 */
public interface BancoRepository extends JpaRepository<Banco, Long> 
{
	Optional<Banco> findById(Long id);
	List<Banco> findAll();
	Optional<Banco> findByClave(String clave);
}
