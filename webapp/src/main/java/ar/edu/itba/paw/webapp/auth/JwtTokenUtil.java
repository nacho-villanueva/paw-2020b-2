package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Date;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class JwtTokenUtil {
    @Value("classpath:key")
    private Resource key;

    private static final int WEEK_IN_MILLISECONDS = (1000*3600*24*7);

    public User parseToken(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(asString(key))
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
        claims.setExpiration(exp);
        claims.setIssuedAt(now);

        return Jwts.builder()
                .setId(id)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, asString(key))
                .compact();
    }

    private static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
