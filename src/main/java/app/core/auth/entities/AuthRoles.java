package app.core.auth.entities;

import app.core.auth.enums.Roles;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ROLES")
public class AuthRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Roles rol;
}
