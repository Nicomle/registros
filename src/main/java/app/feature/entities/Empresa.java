package app.feature.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EMPRESAS")
@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El cuit no puede ser nulo o vacio.")
    private String cuit;

    @NotBlank(message = "El nombre no puede ser nulo o vacio.")
    private String name;
}
