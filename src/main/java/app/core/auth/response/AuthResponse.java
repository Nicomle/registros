package app.core.auth.response;

import app.core.auth.entities.AuthRoles;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("issued_at")
    private String issuedAt;
    @JsonProperty("roles")
    private Set<AuthRoles> roles = new HashSet<>();
}
