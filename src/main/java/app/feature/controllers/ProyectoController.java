package app.feature.controllers;

import app.feature.entities.Proyecto;
import app.feature.services.ProyectoService;
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
@RequestMapping("/proyectos")
public class ProyectoController {

    @Autowired
    ProyectoService proyectoService;

    @GetMapping("/obtener")
    public ResponseEntity<GlobalResponse> obtenerProyecto(@RequestParam String id, HttpServletRequest request) {
        if(id == null || id.equals("") || !StringUtils.isNumeric(id)) {
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", "ID invalido. Debe ser un numero no vacio ni nulo.");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return proyectoService.obtenerProyecto(Long.parseLong(id), request);
    }

    @GetMapping("/obtener/lista")
    public ResponseEntity<GlobalResponse> obtenerListaProyectos(HttpServletRequest request) {
        return proyectoService.obtenerListaProyectos(request);
    }

    @PostMapping("/guardar")
    public ResponseEntity<GlobalResponse> guardarProyecto(@Valid @RequestBody Proyecto proyecto, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> {
                errors.add(error.getDefaultMessage());
            });
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", String.join(", ", errors));
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return proyectoService.guardarProyecto(proyecto, request);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<GlobalResponse> eliminarProyecto(@RequestParam String id, HttpServletRequest request) {
        if(id == null || id.equals("") || !StringUtils.isNumeric(id)) {
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", "ID invalido. Debe ser un numero no vacio ni nulo.");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return proyectoService.eliminarProyecto(Long.parseLong(id), request);
    }

    @PutMapping("/editar")
    public ResponseEntity<GlobalResponse> editarProyecto(@RequestParam String id, @Valid @RequestBody Proyecto proyecto, BindingResult bindingResult, HttpServletRequest request) {
        if (id == null || id.equals("") || !StringUtils.isNumeric(id)) {
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", "ID invalido. Debe ser un numero no vacio ni nulo.");
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
        return proyectoService.editarProyecto(Long.parseLong(id), proyecto, request);
    }
}
