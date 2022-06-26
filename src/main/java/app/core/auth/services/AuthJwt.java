package app.core.auth.services;

import app.core.auth.dtos.AuthUserLoggedIn;
import app.core.utils.GsonUtil;
import com.google.gson.Gson;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

@Service
public class AuthJwt {

    private final static Logger logger = LoggerFactory.getLogger(AuthJwt.class);

    @Value("${configs.auth.token.secret:secretPassDefault}")
    private String SECRET;

    @Value("${configs.auth.timezone:America/Argentina/Buenos_Aires}")
    private String TIMEZONE;

    @Value("${configs.auth.token.expiration:3600}")
    private int EXPIRATION_TIME;

    @Value("${configs.auth.issuer:none}")
    private String ISSUER;

    public String generateToken(Object obj) {
        String subject = GsonUtil.serialize(obj);
        TimeZone tz = TimeZone.getTimeZone(TIMEZONE);
        ZonedDateTime now = ZonedDateTime.now(tz.toZoneId());
        ZonedDateTime exp = now.plusSeconds(EXPIRATION_TIME);

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(exp.toInstant()))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("token mal formado");
        } catch (UnsupportedJwtException e) {
            logger.error("token no soportado");
        } catch (ExpiredJwtException e) {
            logger.error("token expirado");
        } catch (IllegalArgumentException e) {
            logger.error("token vacío");
        } catch (SignatureException e) {
            logger.error("fail en la firma");
        }
        return false;
    }

    public String getNombreUsuarioFromToken(String token) {
        Gson gson = new Gson();
        AuthUserLoggedIn usuario = gson.fromJson(Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject(), AuthUserLoggedIn.class);
        return usuario.getUserName();
    }
}
