package com.example.productapi.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.productapi.dto.JwtResponse;
import com.example.productapi.service.exceptions.AuthenticationException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private static final String EMPTY_SPACE = " ";
	private static final Integer TOKEN_INDEX = 1;

	@Value("${app-config.secrets.api-secret}")
	private String apiScret;

	public void validateAuthorization(String token) {
		var acessToken = extractToken(token);
		try {
			var claims = Jwts.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(apiScret.getBytes()))
					.build()
					.parseClaimsJws(acessToken)
					.getBody();
			var user = JwtResponse.getUser(claims);
			System.out.println(user.getEmail());
			if (isEmpty(user) || isEmpty(user.getId())) {
				throw new AuthenticationException("Usuário inválido!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AuthenticationException("Erro no processamento do token");
		}
	}

	private String extractToken(String token) {
		try {
			if (isEmpty(token)) {
				throw new AuthenticationException("Token de acesso não informado");
			}
			if (token.contains(EMPTY_SPACE)) {
				return token.split(EMPTY_SPACE)[TOKEN_INDEX];
			}
			return token;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			throw new AuthenticationException("Token de acesso não informado");
		} catch (NullPointerException e) {
			throw new AuthenticationException("Token de acesso não informado");
		}
	}
}
