package com.projetos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projetos.service.UsuarioService;

@Service
public class CustomUserDatailService implements UserDetailsService {

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return usuarioService.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
	}
	
	public UserDetails loadUserById(Long id) {
        return usuarioService.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
	}
	
}
