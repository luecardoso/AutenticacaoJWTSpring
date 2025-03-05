package com.projetos.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.projetos.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Component
public class JWTService {

	// Chave privada para assinar o token JWT
	private static final String CHAVE_PRIVADA_JWT = "secretKey";
	// Chave secreta para assinar o token JWT
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(CHAVE_PRIVADA_JWT.getBytes(StandardCharsets.UTF_8));
    // Tempo de expiração do token JWT
    private static final int TEMPO_EXPIRACAO = 86400000; // 1 dia em milissegundos

    /**
	 * Gera um token JWT para o usuário autenticado
	 * 
	 * @param auth
	 * @return token JWT
	 * @see Usuario
	 */
    public String generateToken(Authentication auth) {

        Instant agora = Instant.now();
        Instant dataExpiracao = agora.plusMillis(TEMPO_EXPIRACAO);

        // Pegando o usuário autenticado
        Usuario usuario = (Usuario) auth.getPrincipal();

        // Gerando o token JWT com as informações do usuário autenticado
        return Jwts.builder()
                .subject(usuario.getId().toString())
                .issuedAt(Date.from(agora))
                .expiration(Date.from(dataExpiracao))
                .signWith(SECRET_KEY) // Usando a chave secreta diretamente
                .compact();
    }
    
    public Optional<Long> getUserIdFromToken(String token) {
		try {
			// Parseando o token e pegando o corpo
			Claims claims = parse(token).getPayload();
			
			// Retornando o ID do usuário
			return Optional.of(Long.parseLong(claims.getSubject()));
		} catch (Exception e) {
			// Caso ocorra algum erro, retornar um Optional vazio
			return Optional.empty();
		}
	}
	
    /**
     * Verifica se o token é válido
     * @param token
     * @return true se o token é válido, false caso contrário
     */
	private Jws<Claims> parse(String token) {
		// Verificando a assinatura do token
		JwtParser jwtParser =  Jwts.parser().verifyWith(SECRET_KEY).build();
		// Retornando o corpo do token
		return jwtParser.parseSignedClaims(token);
	}
}
