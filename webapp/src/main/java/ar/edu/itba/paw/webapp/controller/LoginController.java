package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.auth.JwtTokenUtil;
import ar.edu.itba.paw.webapp.dto.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/")
@Component
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService us;


    @POST
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    public Response login(@Valid final UserCredentials credentials) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(), credentials.getPassword()
                    )
            );

            String username = ((org.springframework.security.core.userdetails.User) authenticate.getPrincipal()).getUsername();

            Optional<User> maybeUser = us.findByEmail(username);

            if(!maybeUser.isPresent()) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

            return Response.ok().header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateToken(maybeUser.get()))
                    .header(org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,HttpHeaders.AUTHORIZATION).build();
        } catch (BadCredentialsException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }
}
