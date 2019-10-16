package com.fareye.twitter.jwt;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



//import com.smile.jwtdemo.security.JwtSuccessHandler;
//import com.smile.jwtdemo.security.filter.JwtAuthenticationFilter;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//	@Autowired
//	private UserDetailsService jwtUserDetailsService;
//	@Autowired
//	private JwtRequestFilter jwtRequestFilter;
	@Autowired
	private MyAuthenticationProvider myAuthenticationProvider;
	
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(){
		
		JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter();
		authenticationFilter.setAuthenticationManager(authenticationManager());
		authenticationFilter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
		return authenticationFilter;
	}
	@Bean
	public AuthenticationManager authenticationManager(){
		
		return new ProviderManager(Collections.singletonList(myAuthenticationProvider));
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// We don't need CSRF for this example
//		httpSecurity.csrf().disable()
//		// dont authenticate this particular request
//		.authorizeRequests().antMatchers("/authenticate").permitAll().antMatchers("/register").permitAll().
//		// all other requests need to be authenticated
//		anyRequest().authenticated().and().
//		// make sure we use stateless session; session won't be used to
//		// store user's state.
//		exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		// Add a filter to validate the tokens with every request
//		httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
		
		http.csrf().disable()
		.authorizeRequests().antMatchers("**/rest/**").authenticated()
		.and()
		.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	http.headers().cacheControl();
	http.cors();
	}
}
