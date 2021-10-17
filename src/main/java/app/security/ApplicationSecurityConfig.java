package app.security;

import static app.security.ApplicationUserRole.ADMIN;
import static app.security.ApplicationUserRole.ADMINTRAINEE;
import static app.security.ApplicationUserRole.STUDENT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
					.antMatchers("/", "/index.html", "/css/**", "/favicon")
					.permitAll()
					.anyRequest()
					.authenticated()
					.and()
					.httpBasic();
	}

	@Override
	@Bean
	protected UserDetailsService userDetailsService() {
		UserDetails annaSmithUser = User
				.builder()
					.username("annasmith")
					.password(passwordEncoder.encode("password"))
					.roles(STUDENT.name()) // ROLE_STUDENT
					.build();

		UserDetails lindaUser = User
				.builder()
					.username("linda")
					.password(passwordEncoder.encode("password123"))
					.roles(ADMIN.name()) // ROLE_ADMIN
					.build();

		UserDetails tomUser = User
				.builder()
					.username("tom")
					.password(passwordEncoder.encode("password123"))
					.roles(ADMINTRAINEE.name()) // ROLE_ADMINTRAINEE
					.build();

		return new InMemoryUserDetailsManager(annaSmithUser, lindaUser,
				tomUser);

	}
}