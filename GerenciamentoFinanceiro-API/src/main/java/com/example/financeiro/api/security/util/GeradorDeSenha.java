package com.example.financeiro.api.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorDeSenha {
	
	public class GeradorSenha {
	    public static void main(String[] args) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			System.out.println(encoder.encode("admin"));
		}
	}

}
