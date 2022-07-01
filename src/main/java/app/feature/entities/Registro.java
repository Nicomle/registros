package app.feature.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "REGISTROS")
@Entity
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "La fecha no puede ser nulo")
    private LocalDate date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    @NotNull(message = "El usuario no puede ser nulo")
    private Usuario user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "proyecto_id")
    @NotNull(message = "El proyecto no puede ser nulo")
    private Proyecto project;

    @Min(value = 1, message = "El valor minino de cantidad de horas es 1")
    @Max(value = 24, message = "El valor maximo de cantidad de horas es 24")
    @NotNull(message = "La cantidad de horas no puede ser nulo")
    private int hours;

    @Size(max = 250, message = "La contraseña debe contener una longitud maxima de 250 caracteres")
    private String description;
}
