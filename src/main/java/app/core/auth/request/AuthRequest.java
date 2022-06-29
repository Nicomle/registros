package app.core.auth.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @NotBlank(message = "El nombre usuario no puede ser nulo o vacio")
    private String userName;

    @NotBlank(message = "La contraseña no puede ser nulo o vacio")
    private String password;
}
