package com.pranav.authserver.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 * An extension of Spring's UserDetailsService
 * It will use a repository to get user details (given the user name)
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	//UserDetails is a Spring security class
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//User is a Spring security class
		User user = userDetailsRepository.findByUsername(username);

		if (user != null) {
			return user;
		}else{
			throw new UsernameNotFoundException(username);
		}
	}
}
