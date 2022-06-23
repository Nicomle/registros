package app.feature.controllers;

import app.feature.entities.Empresa;
import app.feature.services.EmpresaService;
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
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaServices;

    @GetMapping("/obtener")
    public ResponseEntity<GlobalResponse> obtenerEmpresa(@RequestParam String cuit, HttpServletRequest request) {
        if(cuit == null || cuit.equals("") || !StringUtils.isNumeric(cuit)) {
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", "Cuit invalido. Debe ser un numero no vacio ni nulo.");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return empresaServices.obtenerEmpresa(cuit, request);
    }

    @GetMapping("/obtener/lista")
    public ResponseEntity<GlobalResponse> obtenerListaEmpresas(HttpServletRequest request) {
        return empresaServices.obtenerListaEmpresas(request);
    }

    @PostMapping("/guardar")
    public ResponseEntity<GlobalResponse> guardarEmpresa(@Valid @RequestBody Empresa empresa, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> {
                errors.add(error.getDefaultMessage());
            });
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", String.join(", ", errors));
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return empresaServices.guardarEmpresa(empresa, request);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<GlobalResponse> eliminarEmpresa(@RequestParam String cuit, HttpServletRequest request) {
        if(cuit == null || cuit.equals("") || !StringUtils.isNumeric(cuit)) {
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", "Cuit invalido. Debe ser un numero no vacio ni nulo.");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return empresaServices.eliminarEmpresa(cuit, request);
    }

    @PutMapping("/editar")
    public ResponseEntity<GlobalResponse> editarEmpresa(@RequestParam String id, @Valid @RequestBody Empresa empresa, BindingResult bindingResult, HttpServletRequest request) {
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
        return empresaServices.editarEmpresa(Long.parseLong(id), empresa, request);
    }
}
