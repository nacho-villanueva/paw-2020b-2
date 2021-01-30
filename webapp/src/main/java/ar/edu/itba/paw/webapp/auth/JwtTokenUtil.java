package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    //TODO: wire to something
    private final String secret = "hudhas8912893ioushdda2";

    public User parseToken(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            String email = body.getSubject();
            int id = Integer.parseInt((String) body.get("id"));
            int role = Integer.parseInt((String) body.get("role"));

            User user = new User();
            user.setId(id);
            user.setEmail(email);
            user.setRole(role);
            return user;
        } catch (JwtException | ClassCastException e) {
            return null;
        }
    }

    public String generateToken(User u) {
        Claims claims = Jwts.claims().setSubject(u.getEmail());
        claims.put("id", u.getId() + "");
        claims.put("role", u.getRole() + "");

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
