package com.example.DemoSecurity.security;

import com.example.DemoSecurity.auth.ApplicationUserService;
import com.example.DemoSecurity.jwt.JwtTokenVerifier;
import com.example.DemoSecurity.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.DemoSecurity.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                  .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // sessions won't be stored in db
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager()))
                .addFilterAfter(new JwtTokenVerifier(),JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
               // .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
              //  .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
              //  .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
              //  .antMatchers("/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
                .anyRequest()
                .authenticated();  // commenting below b/c we don't need form-based authentication now, we will implement jwts.
//                .and()
//                .formLogin()    commenting below all because form
//                .loginPage("/login").permitAll()
//                .defaultSuccessUrl("/courses",true)
//                .and()
//                .rememberMe() // defaults to 2 weeks
//                .and()
//                .logout()
//                .clearAuthentication(true)
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID", "remember-me")
//                .logoutSuccessUrl("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
provider.setPasswordEncoder(passwordEncoder);
provider.setUserDetailsService(applicationUserService);
return provider;
    }

   /** @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails annaSmithUser = User.builder()
                .username("annasmith")
                .password(passwordEncoder.encode("password"))
//                .roles(STUDENT.name()) // ROLE_STUDENT
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails lindaUser = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("password123"))
//                .roles(ADMIN.name()) // ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password123"))
//                .roles(ADMINTRAINEE.name()) // ROLE_ADMINTRAINEE
                .authorities(ADMINTRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                annaSmithUser,
                lindaUser,
                tomUser
        );

    }**/
}