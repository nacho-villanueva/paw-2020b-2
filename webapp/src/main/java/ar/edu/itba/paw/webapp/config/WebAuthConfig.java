package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Value("classpath:auth.auth.properties")
    private Properties properties;

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
                    .antMatchers("/upload-result/**").hasRole("CLINIC")
                    .antMatchers("/home","/view-study/**","/api/image/study/**","api/image/result/**").authenticated()
                    .antMatchers("/","/login").anonymous()
                    .antMatchers("/**").permitAll()
                .and().formLogin()
                    .usernameParameter("login_email")
                    .passwordParameter("login_password")
                    .defaultSuccessUrl("/home", false)
                    .loginPage("/")
                    .loginProcessingUrl("/login")
                    .failureUrl("/?error=true")
                .and().rememberMe()
                    .rememberMeParameter("rememberme")
                    .userDetailsService(userDetailsService)
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                    .key(properties.getProperty("rememberme.key"))  //TODO: openssl 2048 key
                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                .and().exceptionHandling()
                    .accessDeniedPage("/403")       //TODO: check for new exception handling error.jsp view
                .and().csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/css/**", "/resources/js/**", "/resources/img/**", "/403");
    }
}
