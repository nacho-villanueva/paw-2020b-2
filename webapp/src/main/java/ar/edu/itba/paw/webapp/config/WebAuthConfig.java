package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.CorsFilter;
import ar.edu.itba.paw.webapp.auth.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Value("classpath:key")
    private Resource key;

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
                    .antMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "/users/**").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC")
                    .antMatchers(HttpMethod.POST, "/users").anonymous()
                    .antMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC")
                    //Rules for /clinics
                    .antMatchers(HttpMethod.GET, "/clinics").permitAll()    //Can be deleted, but stated for clarity
                    .antMatchers(HttpMethod.GET, "/clinics/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/clinics").hasRole("UNDEFINED")
                    .antMatchers(HttpMethod.PUT, "/clinics/**").hasRole("CLINIC")
                    //Rules for /study-types
                    .antMatchers(HttpMethod.GET, "/study-types").permitAll()    //Can be deleted, but stated for clarity
                    .antMatchers(HttpMethod.GET, "/study-types/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/study-types").authenticated()
                    //Rules for /medics
                    .antMatchers(HttpMethod.GET, "/medics").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC")     //Can be deleted, but stated for clarity
                    .antMatchers(HttpMethod.GET, "/medics/**").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC")
                    .antMatchers(HttpMethod.POST, "/medics").hasRole("UNDEFINED")
                    .antMatchers(HttpMethod.PUT, "/medics/**").hasRole("MEDIC")
                    //Rules for /medical-fields
                    .antMatchers(HttpMethod.GET, "/medical-fields").authenticated()     //Can be deleted, but stated for clarity
                    .antMatchers(HttpMethod.GET, "/medical-fields/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/medical-fields").authenticated()
                    //Rules for /orders
                    .antMatchers(HttpMethod.GET, "/orders").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC")
                    .antMatchers(HttpMethod.GET, "/orders/**/shared-with").hasRole("PATIENT")
                    .antMatchers(HttpMethod.GET, "/orders/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/orders").hasRole("MEDIC")
                    .antMatchers(HttpMethod.POST, "/orders/**/shared-with").hasRole("PATIENT")
                    .antMatchers(HttpMethod.POST, "/orders/**/results").hasAnyRole("CLINIC","PATIENT")
                    .antMatchers(HttpMethod.PUT, "/orders/**").hasAnyRole("PATIENT","MEDIC")
                    //Rules for /patients
                    .antMatchers(HttpMethod.GET, "/patients/**").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC")
                    .antMatchers(HttpMethod.POST, "/patients").hasRole("UNDEFINED")
                    .antMatchers(HttpMethod.PUT, "/patients/**").hasRole("PATIENT")
                    //Rules for /share-requests
                    .antMatchers(HttpMethod.GET, "/share-requests/**").hasRole("PATIENT")
                    .antMatchers(HttpMethod.POST, "/share-requests").hasRole("MEDIC")
                    .antMatchers(HttpMethod.POST, "/share-requests/**").hasRole("PATIENT")
                    .antMatchers(HttpMethod.DELETE, "/share-requests/**").hasRole("PATIENT")
                    //Rules for login
                    .antMatchers(HttpMethod.POST, "/").permitAll()
                    //Default rules
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
                .antMatchers("/resources/css/**", "/resources/js/**", "/resources/img/**", "/favicon.ico", "/403");
    }

    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
