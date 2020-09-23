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

import java.util.*;
import java.util.regex.Pattern;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final int USER_ROLE_ID = 1;
    private static final int MEDIC_ROLE_ID = 2;
    private static final int CLINIC_ROLE_ID = 3;
    private static final int CLINIC_MEDIC_ROLE_ID = 4;
    private static final int ADMIN_ROLE_ID = 0;

    @Autowired
    private UserService us;

    @Autowired
    private PasswordEncoder encoder;

    private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final User user = us.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "not found"));

        final Collection<GrantedAuthority> authorities = new HashSet<>();

        switch (user.getRole()) {
            case MEDIC_ROLE_ID:
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_MEDIC"));
                break;
            case CLINIC_ROLE_ID:
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_CLINIC"));
                break;
            case CLINIC_MEDIC_ROLE_ID:
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_MEDIC"));
                authorities.add(new SimpleGrantedAuthority("ROLE_CLINIC"));
                break;
            case ADMIN_ROLE_ID:
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
            case USER_ROLE_ID:
            default:
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                break;
        }

        //TODO: update database password to encrypted, maybe, i think it doesnt apply to us
        final String password;
        if(!BCRYPT_PATTERN.matcher(user.getPassword()).matches()) {
            User newUser = us.updatePassword(user, user.getPassword());
            password = newUser.getPassword();
        } else {
            password = user.getPassword();
        }

        return new org.springframework.security.core.userdetails.User(email, password, authorities);
    }
}
