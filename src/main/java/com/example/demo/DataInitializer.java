package com.example.demo;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository; 

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {


    @Autowired
    UserRepository users;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception 
    {
    	/*
        log.debug("initializing vehicles data...");
        Arrays.asList("moto", "car").forEach(v -> this.vehicles.saveAndFlush(Vehicle.builder().name(v).build()));

        log.debug("printing all vehicles...");
        this.vehicles.findAll().forEach(v -> log.debug(" Vehicle :" + v.toString()));
*/
    	boolean existeUsuario=true;
    	
    	try
    	{
	    	Optional<User> existeO = this.users.findByUsername("admin");
	    	User usuarioExistente  =existeO.get();
	    	if(usuarioExistente.getUsername().equals("admin"))
	    	{
	    		System.out.println("Usuario se reseteo -....");
	    		usuarioExistente.setPassword(this.passwordEncoder.encode("p4ssw0rd$"));
	            this.users.save(usuarioExistente);
	            existeUsuario=true;
	    	}
    	}
    	catch(Exception e)
    	{
    		System.out.println("Sin usuario admin");	
    		existeUsuario=false;
    	}
    	if(!existeUsuario)
    	{
	        User usuario = this.users.save(User.builder()
	            .username("admin")
	            .nombres("Luis Adrian")
		    	.paterno("Blancas")
		    	.materno("Bahena")
		    	.email("sistemas.blancas@gmail.com")
		    	.intentos(0)
		    	.maximo_intentos(5)
	            .password(this.passwordEncoder.encode("password"))
	            .roles(Arrays.asList( "ROLE_ADMIN"))
	            .build()
	        );
	        usuario.setNombres("Luis Adrian");
	    	usuario.setPaterno("Blancas");
	    	usuario.setMaterno("Bahena");
	    	usuario.setActivo(1);
	    	usuario.setEmail("sistemas.blancas@gmail.com");
	    	usuario.setIntentos(0);
	    	usuario.setMaximo_intentos(5);
			Date dateCreation = new Date(System.currentTimeMillis());
	        Date expirationPassword= new Date(System.currentTimeMillis());
	        expirationPassword.setYear(expirationPassword.getYear() +10);
	        usuario.setFecha_cambio_password (new Timestamp(expirationPassword.getTime()));  
	        usuario.setFecha_creacion ( new Timestamp(dateCreation.getTime()));
	        usuario.setSesionminutos(90);
	        this.users.save(usuario);
    	}
/*
        this.users.save(User.builder()
            .username("bahena")
            .password(this.passwordEncoder.encode("password"))
            .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
            .build()
        );
*/
        log.debug("printing all users...");
        this.users.findAll().forEach(v -> log.debug(" User :" + v.toString()));
        
    }
}
