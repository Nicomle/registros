package app.feature.entities;

import app.core.auth.entities.AuthRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USUARIOS")
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede ser nulo o vacio.")
    private String name;

    @NotBlank(message = "El apellido no puede ser nulo o vacio.")
    private String surname;

    @Min(value = 1000000, message = "El valor minimo del DNI es de 1.000.000")
    @Max(value = 999999999, message = "El valor maximo del DNI es de 999.999.999")
    @NotNull(message = "El DNI no puede ser nulo.")
    private Long dni;

    @Email(message = "Email no valido.")
    @NotBlank(message = "El EMAIL no puede ser nulo o vacio.")
    private String email;

    @NotNull(message = "La contraseña no puede ser nulo.")
    @Size(min = 5, max = 15, message = "La contraseña debe contener una longitud de 5 a 15 caracteres.")
    private String password;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthRoles> roles = new ArrayList<>();
}
