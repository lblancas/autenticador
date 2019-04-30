/* ----------------------------------------------------------------------------
 * All rights reserved © 2018 Multiva.
 *  
 * This software contains information that is exclusive property of 
 * Multiva, this information is considered confidential.
 * It is strictly forbidden the copy or spreading of any part of this document 
 * in any format, whether mechanic or electronic.
 * ---------------------------------------------------------------------------
 * File name: PerfilRepository.java
 * Original Author: StratPlus
 * Creation Date: 17/10/2018
 * ---------------------------------------------------------------------------
 */
package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Perfil;

/**
 *  <code>PerfilRepository</code>.
 *
 * @author ${eduardo.santiago}
 * @version 1.0
 */
@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    /**
     * Find by perfil.
     *
     * @param perfil perfil
     * @return perfil
     */
    public Perfil findByDescripcion(String descripcion);
    public Perfil findById(Integer id);
}
