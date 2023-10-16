package com.quiz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.quiz.exception.JSONParse.CustomAccessDeniedHandler;
import com.quiz.security.AuthEntryPointJwt;
import com.quiz.security.AuthTokenFilter;
import com.quiz.service.UserDetailsServiceImpl;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	 * this array consist of all the public api's ( APIS that dosen't need JWT token) 
	 * this array is passed to configure method. 
	 * "/uploads/**" is to get the images from uploads folder from c drive
	 */
	public static final String[] PUBLIC_URLS = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
			"/quiz/get-quiz-by-startdate-and-enddate", "/forgotpass", "/reset/{token}","/uploads/**",
			"/generateCaptcha", "/validateCaptcha", "/Users/**", "/certificate/add", "/certificate/get/**",
			"/social/google/login","/social/IVP/login","/userresponse/count/*","/signout",
			"/challenge-response/**","/download-file/**",
			"/swagger-resources/**", "/swagger-ui/**", "/webjars/**", "/register", "/login/**", "/certificate",
			"/hello" };

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers()
		.frameOptions()
		.disable()
		.and()
		.cors()
		.and()
		.csrf()
		.disable()
		.authorizeHttpRequests()
		.antMatchers(PUBLIC_URLS) // public URLS ( URLS that can eb accessed without token)
		.permitAll().anyRequest()
		.authenticated()
		.and()
		.exceptionHandling()
		.accessDeniedHandler(accessDeniedHandler) // for forbidden error (if user tries to access Admin API and vise versa)
		.authenticationEntryPoint(unauthorizedHandler).and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	/*
	 * --------Old configure method -------------(kept for future reference )
	 * 
	 * @Override protected void configure(HttpSecurity http) throws Exception {
	 * http.cors().and().csrf().disable()
	 * .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
	 * .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
	 * and() .authorizeRequests().antMatchers("/register").permitAll()
	 * .antMatchers("/login").permitAll() .antMatchers("/certificate").permitAll()
	 * .antMatchers("/hello").permitAll() .anyRequest().authenticated();
	 * 
	 * http.addFilterBefore(authenticationJwtTokenFilter(),
	 * UsernamePasswordAuthenticationFilter.class); }
	 * 
	 */
}
