package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

import static ar.edu.itba.paw.models.User.*;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService us;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final User user = us.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "not found"));

        final Collection<GrantedAuthority> authorities = new HashSet<>();

        switch (user.getRole()) {
            case MEDIC_ROLE_ID:
                if(!user.isVerifying()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_VERIFIED"));
                }
                authorities.add(new SimpleGrantedAuthority("ROLE_MEDIC"));
                break;
            case CLINIC_ROLE_ID:
                if(!user.isVerifying()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_VERIFIED"));
                }
                authorities.add(new SimpleGrantedAuthority("ROLE_CLINIC"));
                break;
            case ADMIN_ROLE_ID:
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
            case PATIENT_ROLE_ID:
                authorities.add(new SimpleGrantedAuthority("ROLE_PATIENT"));
                break;
            case UNDEFINED_ROLE_ID:
            default:
                authorities.add(new SimpleGrantedAuthority("ROLE_UNDEFINED"));
        }

        return new org.springframework.security.core.userdetails.User(email, user.getPassword(), user.isEnabled(), true, true, true, authorities);
    }
}
