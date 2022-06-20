package app.controllers;

import app.entities.Registro;
import app.services.RegistroService;
import core.exceptions.ErrorDetails;
import core.response.GlobalResponse;
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
@RequestMapping("/registros")
public class RegistroController {

    @Autowired
    RegistroService registroService;

    @GetMapping("/obtener")
    public ResponseEntity<GlobalResponse> obtenerRegistro(@RequestParam String id, HttpServletRequest request) {
        if(id == null || id.equals("") || !StringUtils.isNumeric(id)) {
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", "ID invalido. Debe ser un numero no vacio ni nulo.");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return registroService.obtenerRegistro(Long.parseLong(id), request);
    }

    @GetMapping("/obtener/lista")
    public ResponseEntity<GlobalResponse> obtenerListaRegistros(HttpServletRequest request) {
        return registroService.obtenerListaRegistros(request);
    }

    @PostMapping("/guardar")
    public ResponseEntity<GlobalResponse> guardarRegistro(@Valid @RequestBody Registro registro, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> {
                errors.add(error.getDefaultMessage());
            });
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", String.join(", ", errors));
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return registroService.guardarRegistro(registro, request);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<GlobalResponse> eliminarRegistro(@RequestParam String id, HttpServletRequest request) {
        if(id == null || id.equals("") || !StringUtils.isNumeric(id)) {
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", "ID invalido. Debe ser un numero no vacio ni nulo.");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return registroService.eliminarRegistro(Long.parseLong(id), request);
    }

    @PutMapping("/editar")
    public ResponseEntity<GlobalResponse> editarRegistro(@RequestParam String id, @Valid @RequestBody Registro registro, BindingResult bindingResult, HttpServletRequest request) {
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
        return registroService.editarRegistro(Long.parseLong(id), registro, request);
    }
}
