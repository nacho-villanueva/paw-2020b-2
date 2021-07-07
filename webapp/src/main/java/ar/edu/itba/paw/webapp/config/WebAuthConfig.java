package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.CorsFilter;
import ar.edu.itba.paw.webapp.auth.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CorsFilter corsFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                    //Rules for /users
                    .antMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/users").anonymous()
                    .antMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC", "UNDEFINED")
                    //Rules for /clinics
                    .antMatchers(HttpMethod.PUT, "/api/clinics/**/verify").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "/api/clinics").permitAll()    //Can be deleted, but stated for clarity
                    .antMatchers(HttpMethod.GET, "/api/clinics/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/clinics").hasRole("UNDEFINED")
                    .antMatchers(HttpMethod.PUT, "/api/clinics/**").hasRole("CLINIC")
                    //Rules for /study-types
                    .antMatchers(HttpMethod.GET, "/api/study-types").permitAll()    //Can be deleted, but stated for clarity
                    .antMatchers(HttpMethod.GET, "/api/study-types/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/study-types").authenticated()
                    //Rules for /medic-plans
                    .antMatchers(HttpMethod.GET, "/api/medic-plans").permitAll()    //Can be deleted, but stated for clarity
                    .antMatchers(HttpMethod.GET, "/api/medic-plans/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/medic-plans").authenticated()
                    //Rules for /medics
                    .antMatchers(HttpMethod.PUT, "/api/medics/**/verify").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "/api/medics").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC")     //Can be deleted, but stated for clarity
                    .antMatchers(HttpMethod.GET, "/api/medics/**").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC")
                    .antMatchers(HttpMethod.POST, "/api/medics").hasRole("UNDEFINED")
                    .antMatchers(HttpMethod.PUT, "/api/medics/**").hasRole("MEDIC")
                    //Rules for /medical-fields
                    .antMatchers(HttpMethod.GET, "/api/medical-fields").authenticated()     //Can be deleted, but stated for clarity
                    .antMatchers(HttpMethod.GET, "/api/medical-fields/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/medical-fields").authenticated()
                    //Rules for /orders
                    .antMatchers(HttpMethod.GET, "/api/orders").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC")
                    .antMatchers(HttpMethod.GET, "/api/orders/filters/**").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC")
                    .antMatchers(HttpMethod.GET, "/api/orders/**/shared-with").hasRole("PATIENT")
                    .antMatchers(HttpMethod.GET, "/api/orders/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/orders").hasRole("MEDIC")
                    .antMatchers(HttpMethod.POST, "/api/orders/**/shared-with").hasRole("PATIENT")
                    .antMatchers(HttpMethod.POST, "/api/orders/**/results").hasAnyRole("CLINIC","PATIENT")
                    .antMatchers(HttpMethod.PUT, "/api/orders/**").hasAnyRole("PATIENT","MEDIC")
                    //Rules for /patients
                    .antMatchers(HttpMethod.GET, "/api/patients").hasAnyRole("MEDIC","ADMIN")
                    .antMatchers(HttpMethod.GET, "/api/patients/**").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC")
                    .antMatchers(HttpMethod.POST, "/api/patients").hasRole("UNDEFINED")
                    .antMatchers(HttpMethod.PUT, "/api/patients/**").hasRole("PATIENT")
                    //Rules for /share-requests
                    .antMatchers(HttpMethod.GET, "/api/share-requests/**").hasRole("PATIENT")
                    .antMatchers(HttpMethod.POST, "/api/share-requests").hasRole("MEDIC")
                    .antMatchers(HttpMethod.POST, "/api/share-requests/**").hasRole("PATIENT")
                    .antMatchers(HttpMethod.DELETE, "/api/share-requests/**").hasRole("PATIENT")
                    //Rules for login
                    .antMatchers(HttpMethod.POST, "/api/").permitAll()
                    //Default rules
                    .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/**").permitAll()
                .and().exceptionHandling()
                    .authenticationEntryPoint((request, response, ex) -> {
                        response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                ex.getMessage()
                        );
                    })
                .and().addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/static/**", "/locales/**", "/index.html", "/favicon.ico", "/403", "/resources/**");
    }

    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
