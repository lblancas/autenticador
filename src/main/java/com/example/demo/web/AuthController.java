package com.example.demo.web;
import static org.springframework.http.ResponseEntity.ok;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.example.demo.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

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
	
    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();
            Optional<User> usuarioOpcional= users.findByUsername(username);
        	User usuario  =usuarioOpcional.get();
        	if(usuario.getActivo()==0)// inactivo
        		return  new ResponseEntity(new UsuarioException(usuario.getId(),400,"Usuario Inactivo"),HttpStatus.BAD_REQUEST);
        	if(usuario.getIntentos()>= usuario.getMaximo_intentos())// inactivo
        		return  new ResponseEntity(new UsuarioException(usuario.getId(),400,"Usuario Bloqueado"),HttpStatus.BAD_REQUEST);
        	try
        	{
	        	if(usuario.getFecha_cambio_password().getTime()< (new Date()).getTime() )
	        		return  new ResponseEntity(new UsuarioException(usuario.getId(),400,"Usuario con password expirada"),HttpStatus.BAD_REQUEST);
        	}catch(Exception dateEx)
        	{
        		return  new ResponseEntity(new UsuarioException(usuario.getId(),400,"Usuario problema en fecha :Fecha_cambio_password"),HttpStatus.BAD_REQUEST);
        	}
        	if(existeSession(usuario))
        		return  new ResponseEntity(new UsuarioException(usuario.getId(),400,"Usuario con sesion activa"),HttpStatus.BAD_REQUEST);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            Session sesionCreada= createSession(usuario,1l);
            String token = jwtTokenProvider.createToken(username, this.users.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found")).getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("usuario", usuario.getUsername());
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
        	return  new ResponseEntity(new UsuarioException(400,"Usuario invalido / o password incorrecto"),HttpStatus.BAD_REQUEST);
        }
    }

	private boolean existeSession(User usuario) 
	{
		List<Session> listaSesssion = sessionRepository.findByUsuarioAndStatus((usuario.getId()).intValue(),1);
		if(listaSesssion.size()>0)
		{
			boolean modificoSessiones=false;
			for (Session sess: listaSesssion) 
			{
				long timeNow = (new Date()).getTime();
				long timeSess= sess.getFecha_inactividad().getTime();
				if(timeNow>timeSess)
				{
					sess.setStatus(0);
					sessionRepository.save(sess);
					modificoSessiones=true;
				}
				else
					return true;
			}
			if(modificoSessiones)
			{
				listaSesssion = sessionRepository.findByUsuarioAndStatus((usuario.getId()).intValue(),1);
				if(listaSesssion.size()>0)
					return true;
				return false;
			}
			
			return true;
		}
		return false;
	}
	private Session createSession(User usuario,Long idtipousuario) 
	{
		try
		{
			int seg =60;
    		int mil = 1000;
    		Date creacion = new Date(System.currentTimeMillis());
    		
    		Date now = new Date();
            now.setYear(now.getYear()+1);
            Date expiracion = new Date(now.getTime()); 
            
            Timestamp  creacionStamp = (new Timestamp(creacion.getTime()));  
            Timestamp  expiracionStamp = ( new Timestamp(expiracion.getTime()));
			Session se=new Session((usuario.getId()).intValue(),creacionStamp,expiracionStamp,1,idtipousuario);
			Session sesionCreada=sessionRepository.save(se);
			return sesionCreada;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
