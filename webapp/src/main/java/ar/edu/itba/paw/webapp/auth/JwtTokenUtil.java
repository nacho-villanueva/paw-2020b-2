package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenUtil {

    //TODO: wire to something
    private final String secret = "hudhas8912893ioushdda2";

    private static final int WEEK_IN_MILLISECONDS = (1000*3600*24*7);

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
            //Very likely an expired token
            return null;
        }
    }

    public String generateToken(User u) {
        String id = UUID.randomUUID().toString().replace("-","");

        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + WEEK_IN_MILLISECONDS);

        Claims claims = Jwts.claims().setSubject(u.getEmail());
        claims.put("id", u.getId() + "");
        claims.put("role", u.getRole() + "");

        return Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
