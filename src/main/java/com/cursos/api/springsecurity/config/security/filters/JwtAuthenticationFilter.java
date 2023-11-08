package com.cursos.api.springsecurity.config.security.filters;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.Date;
import com.cursos.api.springsecurity.persistence.entity.Token;
import com.cursos.api.springsecurity.persistence.repository.TokenRepository;
import com.cursos.api.springsecurity.persistence.util.Utils;
import com.cursos.api.springsecurity.service.impl.TokenServiceImpl;
import com.cursos.api.springsecurity.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private TokenServiceImpl tokenServiceImpl;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
				
		//Este método se encargará de obtener los headers, especialmente el Authorization. De él va a extraer el token
		//Y mientras lo extrae, valida que tenga el formato correcto.
		String jwt = Utils.getTokenFromRequest(request);
		
		//En caso de que la extracción del jwt haya fracasado por formato incorrecto, entonces saltará este filtro.
		if (jwt == null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		Optional<Token> token = tokenRepository.findByToken(jwt);
		boolean tokenValid = this.validToken(token);
		if (!tokenValid) {
			filterChain.doFilter(request, response);
			return;
		}
		
		
		//Obtenemos el nombre de usuario de nuestro token. Y de pasón verificamos la validez y vigencia del token.
		String username = tokenServiceImpl.extractNameFromToken(jwt);
		
		//Creamos un objeto de tipo Authentication con user, contraseña y los authorities del userdetails.
		UserDetails user = userDetailsServiceImpl.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
		
		//Este paso es únicamente para que a la authentication se le añadan detalles como ip remoto y session id. Después
		//podrían sernos de alguna utilidad.
		
		authentication.setDetails(new WebAuthenticationDetails(request));
		//Al contexto de seguridad de la aplicación, le vamos a setear nuestro objeto de tipo Authentication.
		SecurityContextHolder.getContext().setAuthentication(authentication);

		//Terminamos el método mandando el registro de los filtros y continuamos con los demás filtros.
		filterChain.doFilter(request, response);

	}
	
	private boolean validToken(Optional<Token> token) {
		
		if (!token.isPresent()) {
			System.out.println("Este token no es válido");
			return false;
		}
		Date currectDate = new Date(System.currentTimeMillis());

		boolean tokenValido =  token.get().isValid() && (token.get().getExpiration().after(currectDate));
		
		if (!tokenValido) {
			System.out.println("Este token ya no está disponible");
			this.updateToken(token.get());
		}
		
			

		return tokenValido;
	}
	
	private void updateToken(Token token) {
		token.setValid(false);
	}

}
