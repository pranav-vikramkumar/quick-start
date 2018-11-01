package com.pranav.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.pranav.authserver.user.SecurityConfig;
import com.pranav.authserver.user.UserDetailsServiceImpl;

@Configuration
@EnableAuthorizationServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(SecurityConfig.class)

public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder oauthClientPasswordEncoder;

    /*
     * During the time the auth server is alive, it may issue some long validity tokens.
     * If the server is shut down and re-started tomorrow, those access tokens must still
     * be valid. A token Store is used to store these tokens. US JDBC token store for
     * persistence. For simplicity In-memory token store can be used.
     */
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public OAuth2AccessDeniedHandler oauthAccessDeniedHandler() {
        return new OAuth2AccessDeniedHandler();
    }

    //========================================================
    //			3 CONFIG Functions
    //========================================================
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        //1. Permit everyone to call /oauth/token
    	//2. Permit only authorized people to call /oauth/check_token
    	//3. Specify which password encoder you will be using in the next "configure step" 
    	oauthServer
        	.tokenKeyAccess("permitAll()")
        	.checkTokenAccess("isAuthenticated()")
        	.passwordEncoder(oauthClientPasswordEncoder);    	    
    }

    /*
     * Client is some entity who makes the actual OAuth requests. Eg) An app or the browser      
     * Here "In-memory" client detail storage is used.
     * We can also use JDBC to get values from the database
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
        .withClient("facebook_app") //Client Id
        .secret(oauthClientPasswordEncoder.encode("secret")) //We only need to send "secret". BCrypt will auto encode any received password. So comparison with the stored encrypted password (from UserDetailsService) can happen.
        .authorizedGrantTypes("password","authorization_code","refresh_token") //We can use either password, existing auth token OR a refresh token to get a new auth code.       
        .scopes("get_token","access_to_facebook") //Just like users have authorities, scope is the authority of the client
        .resourceIds("resource-server-rest-api") //Which all resources this particular client will have access to. It is a String.. so multiple values
        .accessTokenValiditySeconds(1200)
        .refreshTokenValiditySeconds(3600)
        
        .and()
        
        .withClient("resource_server")
        .secret(oauthClientPasswordEncoder.encode("resource_server_secret"))
        .authorizedGrantTypes("authorization_code")
        .authorities("CHECK_TOKEN")//User defined
        .scopes("check_the_token")        
        .accessTokenValiditySeconds(1200)
        .refreshTokenValiditySeconds(3600);        
    }

    /*
     * Adding our custom User details storage 
     * and Spring's inbuilt authorization manager 
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {    	       
        
    	endpoints
        	.tokenStore(tokenStore())
        	.authenticationManager(authenticationManager)
        	.userDetailsService(userDetailsService);
    }

}