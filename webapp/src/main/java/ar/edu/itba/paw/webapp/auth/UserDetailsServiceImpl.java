package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService us;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Optional<User> user = us.findByEmail(username); //TODO: this should be an optional

        if(!user.isPresent()) {
            //TODO see what to do
        }

        //TODO: Assign roles based on the username data provided
        final Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER"));//,
                //new SimpleGrantedAuthority("ROLE_ADMIN"));

        //TODO: update database password to encrypted, maybe, i think it doesnt apply to us

        return new org.springframework.security.core.userdetails.User(username, encoder.encode(user.get().getPassword()), authorities);
    }
}
