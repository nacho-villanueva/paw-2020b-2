package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileCopyUtils;

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
                    .invalidSessionUrl("/")
                .and().authorizeRequests()//TODO: revise ant matchers
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/create-order").hasRole("MEDIC")
                    .antMatchers("/upload-result/**","/result-uploaded").hasRole("CLINIC")
                    .antMatchers("/home","/api/image/study/**","api/image/result/**","/api/data/**").authenticated()
                    .antMatchers("/register-as-medic","/apply-as-medic").hasRole("USER")
                    .antMatchers("/register-as-clinic","/apply-as-clinic").hasRole("USER")
                    .antMatchers("/login","/register").anonymous()
                    .antMatchers("/**").permitAll()
                .and().formLogin()
                    .usernameParameter("login_email")
                    .passwordParameter("login_password")
                    .defaultSuccessUrl("/home", false)
                    .loginPage("/")
                    .loginProcessingUrl("/login")
                    .failureUrl("/?error=true")         //TODO: see way to get more detailed information
                .and().rememberMe()
                    .rememberMeParameter("rememberme")
                    .userDetailsService(userDetailsService)
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                    .key(asString(key))
                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                .and().exceptionHandling()
                    .accessDeniedPage("/403")       //TODO: use .accessDeniedHandler(accessDeniedHandler)
                .and().csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/css/**", "/resources/js/**", "/resources/img/**", "/favicon.ico", "/403");
    }

    private static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
