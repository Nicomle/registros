package app.core.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserLoggedIn {

    private String name;
    private String surName;
    private Long dni;
    private String email;
    private List<String> roles;
}
