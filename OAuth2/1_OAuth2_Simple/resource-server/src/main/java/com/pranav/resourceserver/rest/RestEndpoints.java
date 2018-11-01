package com.pranav.resourceserver.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
public class RestEndpoints {

	//Pre-Authorize will auto check with the auth server based on the supplied token
	//The auth server will return the list of authorities of the user
	//based on these authorities, it will allow to enter this method
	@PreAuthorize("hasAuthority('VIEW_PROTECTED')")
	@GetMapping(path="/secured",
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> secured(OAuth2Authentication authentication) {
		//The OAuth2Authentication parameter has all the details of the authenticated user
		return new ResponseEntity<>("{\"endpointType\":\"secured\"}",HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('VIEW_REGULAR')")
	@GetMapping(path= "/open",
				produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<?> open() {
		
		return new ResponseEntity<>("{\"endpointType\":\"open\"}",HttpStatus.OK);
	}
}
