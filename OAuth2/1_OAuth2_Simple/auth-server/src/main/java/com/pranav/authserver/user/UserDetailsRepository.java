package com.pranav.authserver.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

/*
 * This is a dummy repository instead of a database repository.
 * The basic logic is however the same, based on the username, all details of
 * the user must be fetched.
 * It is in this class that we define the authority of each user.
 */
@Repository
public class UserDetailsRepository {

	public User findByUsername(String username) {		
		switch(username){
		case "pranav":return getDummyUnPrivilegedUser();
		case "admin":return getDummyPrivilegedUser();		
		default : return null;
		}
	}
		
	private User getDummyUnPrivilegedUser(){
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("VIEW_REGULAR"));
		
		//Was previously BCrypt encrypted using the user password encoder
		// pranav pranav
		String password = "$2a$08$u5XarjT/sDbNy3L3btpYAeGPgFkzLdD89TSoh40Ve19AFfjRinQ.m";
		return new User("pranav",password, authorities);
	}

	private User getDummyPrivilegedUser(){
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("VIEW_REGULAR"));
		authorities.add(new SimpleGrantedAuthority("VIEW_PROTECTED"));
		
		// admin password
		String password = "$2a$08$pZolfcZ.uTONTtYPMUJRduLwm1.H7BisCpaiY/syyxvnixs8h3/Ri";
		return new User("admin",password, authorities);
	}
}
