package app.core.auth.dtos;

import app.core.auth.entities.AuthRoles;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserLoggedIn {

    private String userName;
    private String name;
    private String surName;
    private Long dni;
    private String email;
    private Set<AuthRoles> roles = new HashSet<>();
}
