package app.core.auth.configs;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "configs.auth")
public class JwtConfig {

    private Security security;
    private String timeZone;
    private String issuer;
    private Token token;

    @Data
    public static class Security {
        public boolean enabled;
    }

    @Data
    public static class Token {
        public Auth auth;
        public String secret;
        public Expiration expiration;
    }

    @Data
    public static class Auth {
        public String path;
    }

    @Data
    public static class Expiration {
        public int seconds;
    }
}
