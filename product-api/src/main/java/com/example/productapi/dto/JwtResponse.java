package com.example.productapi.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;

public class JwtResponse {

	private Integer id;
	private String name;
	private String email;

	public JwtResponse() {

	}

	public JwtResponse(Integer id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static JwtResponse getUser(Claims jwtClaims) {
		try {
			return new ObjectMapper().convertValue(jwtClaims.get("authUser"), JwtResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
