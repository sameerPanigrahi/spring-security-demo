package app.security;

import static app.security.ApplicationUserRole.ADMIN;
import static app.security.ApplicationUserRole.ADMINTRAINEE;
import static app.security.ApplicationUserRole.STUDENT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
				.csrf()
					.disable()

					.authorizeRequests()
					.antMatchers("/", "/index.html", "/css/**", "/favicon")
					.permitAll()

					.antMatchers(HttpMethod.GET, "/api/v1/students/**")
					.hasRole(STUDENT.name())

					.antMatchers(	HttpMethod.GET,
									"/management/api/v1/students/**")
					.hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())

					.antMatchers(	HttpMethod.POST,
									"/management/api/v1/students/**")
					.hasAuthority(ApplicationUserPermission.STUDENT_WRITE
							.name())
					.antMatchers(	HttpMethod.PUT,
									"/management/api/v1/students/**")
					.hasAuthority(ApplicationUserPermission.STUDENT_WRITE
							.name())
					.antMatchers(	HttpMethod.DELETE,
									"/management/api/v1/students/**")
					.hasAuthority(ApplicationUserPermission.STUDENT_WRITE
							.name())

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
					.authorities(STUDENT.getGrantedAuthorities())
					// ROLE_STUDENT already added in ApplicationUserRole
					// .roles(STUDENT.name())
					.build();

		UserDetails lindaUser = User
				.builder()
					.username("linda")
					.password(passwordEncoder.encode("adminpassword"))
					.authorities(ADMIN.getGrantedAuthorities())
					// ROLE_ADMIN already added in ApplicationUserRole
					// .roles(ADMIN.name())
					.build();

		UserDetails tomUser = User
				.builder()
					.username("tom")
					.password(passwordEncoder.encode("adminpassword"))
					.authorities(ADMINTRAINEE.getGrantedAuthorities())
					// ROLE_ADMINTRAINEE already added in ApplicationUserRole
					// .roles(ADMINTRAINEE.name())
					.build();

		return new InMemoryUserDetailsManager(annaSmithUser, lindaUser,
				tomUser);
	}
}
