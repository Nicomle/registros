package app.feature.controllers;

import app.feature.entities.Usuario;
import app.feature.services.UsuarioService;
import app.core.exceptions.ErrorDetails;
import app.core.response.GlobalResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/obtener")
    public ResponseEntity<GlobalResponse> obtenerProyecto(@RequestParam String dni, HttpServletRequest request) {
        if(dni == null || dni.equals("") || !StringUtils.isNumeric(dni) || Long.parseLong(dni) < 1000000 || Long.parseLong(dni) > 999999999) {
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", "Rango invalido: " + "DNI invalido. Debe ser un numero entre 1.000.000 y 999.999.999 no vacio ni nulo.");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return usuarioService.obtenerUsuario(dni, request);
    }

    @GetMapping("/obtener/lista")
    public ResponseEntity<GlobalResponse> obtenerListaProyectos(HttpServletRequest request) {
        return usuarioService.obtenerListaUsuarios(request);
    }

    @PostMapping("/guardar")
    public ResponseEntity<GlobalResponse> guardarProyecto(@Valid @RequestBody Usuario usuario, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> {
                errors.add(error.getDefaultMessage());
            });
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", String.join(", ", errors));
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return usuarioService.guardarUsuario(usuario, request);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<GlobalResponse> eliminarProyecto(@RequestParam String dni, HttpServletRequest request) {
        if(dni == null || dni.equals("") || !StringUtils.isNumeric(dni) || Long.parseLong(dni) < 1000000 || Long.parseLong(dni) > 999999999) {
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", "DNI invalido. Debe ser un numero entre 1.000.000 y 999.999.999 no vacio ni nulo.");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return usuarioService.eliminarUsuario(dni, request);
    }

    @PutMapping("/editar")
    public ResponseEntity<GlobalResponse> editarProyecto(@RequestParam String dni, @Valid @RequestBody Usuario usuario, BindingResult bindingResult, HttpServletRequest request) {
        if(dni == null || dni.equals("") || !StringUtils.isNumeric(dni) || Long.parseLong(dni) < 1000000 || Long.parseLong(dni) > 999999999) {
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", "DNI invalido. Debe ser un numero entre 1.000.000 y 999.999.999 no vacio ni nulo.");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> {
                errors.add(error.getDefaultMessage());
            });
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", String.join(", ", errors));
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return usuarioService.editarUsuario(dni, usuario, request);
    }
}
