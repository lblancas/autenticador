package com.example.demo.web;

import static org.springframework.http.ResponseEntity.ok;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bean.UsuarioBean;
import com.example.demo.domain.Session;
import com.example.demo.domain.User;
import com.example.demo.repository.SesionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/users")
public class CreateUserController 
{
		private int minutosSession =10;
	 	@Autowired
	    AuthenticationManager authenticationManager;

	    @Autowired
	    JwtTokenProvider jwtTokenProvider;

	    @Autowired
	    UserRepository users;

	    @Autowired
	    PasswordEncoder passwordEncoder;
	    
	    @Autowired
	    SesionRepository sessionRepository;
	    
	    @PreAuthorize("hasRole('ROLE_ADMIN')")
	    @PutMapping("/createUser")
	    public ResponseEntity create(@RequestBody UsuarioBean bean,@AuthenticationPrincipal UserDetails userDetails) 
	    {
	    	if(!existeSessionValida(userDetails))
	    	{
	    		Map<Object, Object> model = new HashMap<>();
	            model.put("usuario",userDetails.getUsername());
	            model.put("mensaje","Session invalida");
	            model.put("code",400);
	            return new ResponseEntity<>(model,HttpStatus.BAD_REQUEST);
	    	}
	    	String strAdmin ="";
	    	boolean existeUsuario=false;
	        try 
	        {
	        	Optional<User> usuarioOpcional= users.findByUsername(bean.getUsername());
	        	User usuario  =usuarioOpcional.get();
	        	existeUsuario=true;
	        }
	        catch(Exception ex)
	        {
	        	existeUsuario=false;
	        }
	        if(!existeUsuario)
	        {
		        try
		        {
		        	User usuario= this.users.save(User.builder()
		                    .username(bean.getUsername())
		                    .password(this.passwordEncoder.encode(bean.getPassword()))
		                    .roles(Arrays.asList(bean.getPerfil()))
		                    .build()
		                );
		        	//Optional<User> usuarioOpcional= users.findByUsername(bean.getUsername());
		        	//User usuario  =usuarioOpcional.get();
		        	usuario.setNombres(bean.getNombres());
		        	usuario.setPaterno(bean.getPaterno());
		        	usuario.setMaterno(bean.getMaterno());
		        	usuario.setActivo(1);
		        	usuario.setEmail(bean.getEmail());
		        	usuario.setIntentos(0);
		        	usuario.setMaximo_intentos(5);
		        	int seg =60;
		    		int mil = 1000;
		    		int sesion =  10 ; //minutos;
		    		Date dateCreation = new Date(System.currentTimeMillis());
		            Date expirationPassword= new Date(System.currentTimeMillis() + (minutosSession *  seg *  mil));
		            usuario.setFecha_cambio_password (new Timestamp(expirationPassword.getTime()));  
		            usuario.setFecha_creacion ( new Timestamp(dateCreation.getTime()));
		            usuario.setSesionminutos(bean.getMinutes());
		            this.users.save(usuario);
		            Map<Object, Object> model = new HashMap<>();
		            model.put("usuario",usuario);
		            return ok(model);
		        } catch (AuthenticationException e) {
		        	strAdmin = ("Invalid create user ::: "+bean.getUsername());
		        }
	        }
	        else
	        {
	        	strAdmin = ("El usuario ya existe ::: "+bean.getUsername());
	        }
	        throw new BadCredentialsException(strAdmin );
	    }
		
	    @PutMapping("/createAutoUser")
	    public ResponseEntity createAutoUser(@RequestBody UsuarioBean bean) 
	    {

	    	String strAdmin ="";
	    	boolean existeUsuario=false;
	        try 
	        {
	        	Optional<User> usuarioOpcional= users.findByUsername(bean.getUsername());
	        	User usuario  =usuarioOpcional.get();
	        	existeUsuario=true;
	        }
	        catch(Exception ex)
	        {
	        	existeUsuario=false;
	        }
	        if(!existeUsuario)
	        {
		        try
		        {
		        	User usuario= this.users.save(User.builder()
		                    .username(bean.getUsername())
		                    .password(this.passwordEncoder.encode(bean.getPassword()))
		                    .roles(Arrays.asList(bean.getPerfil()))
		                    .build()
		                );
		        	usuario.setNombres(bean.getNombres());
		        	usuario.setPaterno(bean.getPaterno());
		        	usuario.setMaterno(bean.getMaterno());
		        	usuario.setActivo(1);
		        	usuario.setEmail(bean.getEmail());
		        	usuario.setIntentos(0);
		        	usuario.setMaximo_intentos(5);
		        	int seg =60;
		    		int mil = 1000;
		    		int sesion =  10 ; //minutos;
		    		Date dateCreation = new Date(System.currentTimeMillis());
		            Date expirationPassword= new Date(System.currentTimeMillis() + (minutosSession *  seg *  mil));
		            usuario.setFecha_cambio_password (new Timestamp(expirationPassword.getTime()));  
		            usuario.setFecha_creacion ( new Timestamp(dateCreation.getTime()));
		            usuario.setSesionminutos(bean.getMinutes());
		            this.users.save(usuario);
		            Map<Object, Object> model = new HashMap<>();
		            model.put("usuario",usuario);
		            return ok(model);
		        } catch (Exception e) 
				{
		        	strAdmin = ("Invalid create user ::: "+bean.getUsername());
		        }
	        }
	        else
	        {
	        	strAdmin = ("El usuario ya existe ::: "+bean.getUsername());
	        }
			Map<Object, Object> modelError = new HashMap<>();
		            modelError.put("usuario","Error de creacion "+bean.toString() );
			return ok(modelError);
	    }
	    @PreAuthorize("hasRole('ROLE_ADMIN')")
	    @DeleteMapping("/deleteUser")
	    public ResponseEntity deleteAdmin(@RequestBody String username,@AuthenticationPrincipal UserDetails userDetails) 
	    {
	    	if(!existeSessionValida(userDetails))
	    	{
	    		Map<Object, Object> model = new HashMap<>();
	            model.put("usuario",userDetails.getUsername());
	            model.put("mensaje","Session invalida");
	            model.put("code",400);
	            return new ResponseEntity<>(model,HttpStatus.BAD_REQUEST);
	    	}
	    	
	    	String strAdmin ="Usuario no encontrado ::: "+username;
	    	boolean existeUsuario=false;
	        try 
	        {
	        	Optional<User> usuarioOpcional= users.findByUsername(username);
	        	User usuario  =usuarioOpcional.get();
	        	users.delete(usuario);
	        	existeUsuario=true;
	        	Map<Object, Object> model = new HashMap<>();
	            model.put("usuario",usuario.getUsername());
	            model.put("code",200);
	            model.put("mensaje","Usuario Borrado con exito");
	            return ok(model);
	        }
	        catch(Exception ex)
	        {
	        	strAdmin = ex.getMessage();
	        }
	        throw new BadCredentialsException(strAdmin );
	    }
	    @PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
	    @PostMapping("/modifyPasswordUser")
	    public ResponseEntity modifiPassord(@RequestBody UsuarioBean bean,@AuthenticationPrincipal UserDetails userDetails) 
	    {
	    	if(!existeSessionValida(userDetails))
	    	{
	    		Map<Object, Object> model = new HashMap<>();
	            model.put("usuario",userDetails.getUsername());
	            model.put("mensaje","Session invalida");
	            model.put("code",400);
	            return new ResponseEntity<>(model,HttpStatus.BAD_REQUEST);
	    	}
	    	
	    	String strAdmin ="Usuario no encontrado ::: "+bean.getUsername();
	    	boolean existeUsuario=false;
	        try 
	        {
	        	Optional<User> usuarioOpcional= users.findByUsername(bean.getUsername());
	        	User usuario  =usuarioOpcional.get();
	        	usuario.setPassword(this.passwordEncoder.encode(bean.getPassword()));
	            this.users.save(usuario);
	        	Map<Object, Object> model = new HashMap<>();
	        	model.put("usuario",usuario);
	            model.put("code",200);
	            model.put("mensaje","Usuario modificado con exito ::"+bean.getUsername());
	            return ok(model);
	        }
	        catch(Exception ex)
	        {
	        	strAdmin = ex.getMessage();
	        }
	        throw new BadCredentialsException(strAdmin );
	    }
	    @PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
	    @PostMapping("/modifyTimeSession")
	    public ResponseEntity modifyTimeSession(@RequestBody UsuarioBean bean,@AuthenticationPrincipal UserDetails userDetails) 
	    {
	    	if(!existeSessionValida(userDetails))
	    	{
	    		Map<Object, Object> model = new HashMap<>();
	            model.put("usuario",userDetails.getUsername());
	            model.put("mensaje","Session invalida");
	            model.put("code",400);
	            return new ResponseEntity<>(model,HttpStatus.BAD_REQUEST);
	    	}
	    	String strAdmin ="Usuario no encontrado ::: "+bean.getUsername();
	    	boolean existeUsuario=false;
	        try 
	        {
	        	Optional<User> usuarioOpcional= users.findByUsername(bean.getUsername());
	        	User usuario  =usuarioOpcional.get();
	        	usuario.setSesionminutos(bean.getMinutes());
	            this.users.save(usuario);
	        	Map<Object, Object> model = new HashMap<>();
	            model.put("usuario",usuario);
	            model.put("code",200);
	            model.put("mensaje","Usuario modificado con exito ::"+bean.getUsername());
	            return ok(model);
	        }
	        catch(Exception ex)
	        {
	        	strAdmin = ex.getMessage();
	        }
	        throw new BadCredentialsException(strAdmin );
	    }
	   
		@PreAuthorize("hasRole('ROLE_ADMIN')")
	    @PostMapping("/desbloqueaUser")
	    public ResponseEntity desbloqueaUser(@RequestBody UsuarioBean bean) 
	    {
	    	String strAdmin ="Usuario no encontrado ::: "+bean.getUsername();
	    	boolean existeUsuario=false;
	        try 
	        {
	        	Optional<User> usuarioOpcional= users.findByUsername(bean.getUsername());
	        	User usuario  =usuarioOpcional.get();
	        	usuario.setActivo(1);
	        	int seg =60;
	    		int mil = 1000;
	    		int sesion =  10 ; //minutos;
	    		usuario.setIntentos(0);
	        	usuario.setMaximo_intentos(5);
	    		Date dateCreation = new Date(System.currentTimeMillis());
	            Date expirationPassword= new Date(System.currentTimeMillis() + (minutosSession *  seg *  mil));
	            usuario.setFecha_cambio_password (new Timestamp(expirationPassword.getTime()));  
	            usuario.setFecha_creacion ( new Timestamp(dateCreation.getTime()));
	            this.users.save(usuario);
	        	Map<Object, Object> model = new HashMap<>();
	            model.put("usuario",usuario);
	            model.put("code",200);
	            model.put("mensaje","Usuario modificado con exito ::"+bean.getUsername());
	            return ok(model);
	        }
	        catch(Exception ex)
	        {
	        	strAdmin = ex.getMessage();
	        }
	        throw new BadCredentialsException(strAdmin );
	    }

	    @PostMapping("/logout")
	    public ResponseEntity logout(@AuthenticationPrincipal UserDetails userDetails)
	    {
	        Map<Object, Object> model = new HashMap<>();
	        Optional<User> usuarioOpcional= users.findByUsername(userDetails.getUsername());
	    	User usuario  =usuarioOpcional.get();
	    	List<Session> listaSesssion = sessionRepository.findByUsuarioAndStatus((usuario.getId()).intValue(),1);
	    	for (Session sess: listaSesssion) 
			{
				long timeNow = (new Date()).getTime();
				long timeSess= sess.getFecha_inactividad().getTime();
				sess.setStatus(0);
				sessionRepository.save(sess);
			}
	        model.put("user", usuario);
	        model.put("estatus", "Sin actividad");
	        return ok(model);
	    }
		
		@PostMapping("/logoutAll")
	    public ResponseEntity logout()
	    {
	        Map<Object, Object> model = new HashMap<>();
	        Optional<User> usuarioOpcional= users.findByUsername("admin");
	    	User usuario  =usuarioOpcional.get();
	    	List<Session> listaSesssion = sessionRepository.findByUsuarioAndStatus((usuario.getId()).intValue(),1);
	    	for (Session sess: listaSesssion) 
			{
				long timeNow = (new Date()).getTime();
				long timeSess= sess.getFecha_inactividad().getTime();
				sess.setStatus(0);
				sessionRepository.save(sess);
			}
	        model.put("user", usuario);
	        model.put("estatus", "Sin actividad");
	        return ok(model);
	    }
		
	    private boolean existeSessionValida(UserDetails userDetails) {
	    	Map<Object, Object> model = new HashMap<>();
	        Optional<User> usuarioOpcional= users.findByUsername(userDetails.getUsername());
	    	User usuario  =usuarioOpcional.get();
	    	List<Session> listaSesssion = sessionRepository.findByUsuarioAndStatus((usuario.getId()).intValue(),1);
	    	if(listaSesssion.size()>0)
	    		return true;
	    	return false;
		}
}
	