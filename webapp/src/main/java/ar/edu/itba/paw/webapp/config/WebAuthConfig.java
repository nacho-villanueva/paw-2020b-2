package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
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
                .and().authorizeRequests()//TODO: set proper filters
                    .antMatchers("/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/users/**").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC")
                    .antMatchers(HttpMethod.POST, "/").permitAll()
                    .antMatchers(HttpMethod.GET, "/clinics/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/clinics").hasRole("UNDEFINED")
                    .antMatchers(HttpMethod.PUT, "/clinics/**").hasRole("CLINIC")
                    .antMatchers(HttpMethod.GET, "/medics/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/medics").hasRole("UNDEFINED")
                    .antMatchers(HttpMethod.PUT, "/medics/**").hasRole("MEDIC")
                    .antMatchers(HttpMethod.GET, "/medical-fields/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/medical-fields").authenticated()
                    .antMatchers(HttpMethod.GET, "/orders/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/orders/shared-with").hasRole("PATIENT")
                    .antMatchers(HttpMethod.POST, "/orders").hasRole("MEDIC")
                    .antMatchers(HttpMethod.POST, "/orders/shared-with").hasRole("PATIENT")
                    .antMatchers(HttpMethod.POST, "/orders/**/results").hasRole("CLINIC")
                    .antMatchers(HttpMethod.PUT, "/orders/**").hasAnyRole("PATIENT","MEDIC","CLINIC")
                    .antMatchers(HttpMethod.GET, "/patients/**").hasAnyRole("PATIENT","MEDIC","ADMIN","CLINIC")
                    .antMatchers(HttpMethod.POST, "/patients").hasRole("UNDEFINED")
                    .antMatchers(HttpMethod.PUT, "/patients/**").hasRole("PATIENT")
                    .antMatchers(HttpMethod.GET, "/study-types/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/study-types").authenticated()
                    .antMatchers(HttpMethod.GET, "/share-requests/**").hasRole("PATIENT")
                    .antMatchers(HttpMethod.POST, "/share-requests/**").hasRole("PATIENT")
                    .antMatchers(HttpMethod.POST, "/share-requests/").hasRole("MEDIC")
                    .antMatchers(HttpMethod.DELETE, "/share-requests/**").hasRole("PATIENT")
                .and().exceptionHandling()
                    .authenticationEntryPoint((request, response, ex) -> {
                        response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                ex.getMessage()
                        );
                    })
                .and().addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();      //TODO: check what is cors and why we cant enable it here
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
