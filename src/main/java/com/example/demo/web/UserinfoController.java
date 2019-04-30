package com.example.demo.web;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Tools;
import com.example.demo.domain.Session;
import com.example.demo.domain.User;
import com.example.demo.domain.catalogos.TipoUsuario;
import com.example.demo.domain.reglas.Cuenta;
import com.example.demo.domain.reglas.Persona;
import com.example.demo.repository.SesionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.catalogos.TipoUsuarioRepository;
import com.example.demo.repository.reglas.CuentaRepository;
import com.example.demo.repository.reglas.PersonaRepository;

@RestController()
public class UserinfoController {

	@Autowired
    UserRepository users;
	
	@Autowired
    SesionRepository sessionRepository;
	
	@Autowired
	CuentaRepository  cuentaRepo;
    
    @Autowired
	PersonaRepository  personaRepo;
    
    @Autowired
	TipoUsuarioRepository tipousuarioRepo; 
    
	
    @GetMapping("/me")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails)
    {
        Map<Object, Object> model = new HashMap<>();
        Optional<User> usuarioOpcional= users.findByUsername(userDetails.getUsername());
    	User usuario  =usuarioOpcional.get();	
    	List<Session> listaSesssion = sessionRepository.findByUsuarioAndStatus((usuario.getId()).intValue(),1);
    	if(listaSesssion.size()>0)
    	{
    		Session sesionUnica=  listaSesssion.get(0);
			
	        model.put("usuario", usuario.getUsername());
	        model.put("caduca", sesionUnica.getFecha_inactividad());
	        model.put("roles", userDetails.getAuthorities()
	                .stream()
	                .map(a -> ((GrantedAuthority) a).getAuthority())
	                .collect(toList())
	            );
    	}
    	else
    	{
    		model.put("usuario", usuario.getUsername());
    		model.put("Sesion","inactiva");
    	}
    	return ok(model);
    }
}
