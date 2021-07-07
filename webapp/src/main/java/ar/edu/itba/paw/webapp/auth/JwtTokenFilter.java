package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.Collections;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String TOKEN_STATUS_HEADER = "Token-Status";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            // No token provided
            response.addHeader(TOKEN_STATUS_HEADER, "Missing");
            chain.doFilter(request,response);
            return;
        }

        //Parse token, validate response
        final String token = header.split(" ")[1].trim();
        User tokenUser = jwtTokenUtil.parseToken(token);
        if(tokenUser == null) {
            //Very likely an expired token
            response.addHeader(TOKEN_STATUS_HEADER,"Expired");
            chain.doFilter(request,response);
            return;
        }
        response.addHeader(TOKEN_STATUS_HEADER,"Healthy");

        //Get user identity and set it on the spring security context
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(tokenUser.getEmail());
        } catch (UsernameNotFoundException e) {
            response.addHeader(TOKEN_STATUS_HEADER, "Expired");
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails == null ? Collections.emptyList() : userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
