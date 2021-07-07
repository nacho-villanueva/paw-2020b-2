package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.auth.JwtTokenUtil;
import ar.edu.itba.paw.webapp.dto.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
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
    public Response login(@Valid @NotNull final UserCredentials credentials) {
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

            return Response.ok().header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateToken(maybeUser.get())).build();
        } catch (BadCredentialsException | DisabledException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }

    @GET
    @Path("/{token}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response enableUser(@NotNull @PathParam("token") String token) {
        Optional<VerificationToken> verificationToken = us.getVerificationToken(token);

        if(!verificationToken.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User user = verificationToken.get().getUser();

        if(user != null && !user.isEnabled()) {
            us.verify(user);
            return Response.status(Response.Status.ACCEPTED).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
