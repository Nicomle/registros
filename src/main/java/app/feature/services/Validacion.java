package app.feature.services;

import app.core.exceptions.ErrorDetails;
import app.core.response.GlobalResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class Validacion {

    public ResponseEntity<GlobalResponse> validarBindingResult(BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> {
                errors.add(error.getDefaultMessage());
            });
            ErrorDetails errorDetails = new ErrorDetails("Error en datos de entrada", String.join(". ", errors));
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    public ResponseEntity<GlobalResponse> validarString(String obj, String nombreCampo, HttpServletRequest request) {
        if (obj == null || obj.equals("")) {
            ErrorDetails errorDetails = new ErrorDetails("Error en datos de entrada", nombreCampo + " invalido. No debe ser vacio ni nulo");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    public ResponseEntity<GlobalResponse> validarId(String id, HttpServletRequest request) {
        if (id == null || id.equals("") || !StringUtils.isNumeric(id)) {
            ErrorDetails errorDetails = new ErrorDetails("Error en datos de entrada", "ID invalido. Debe ser un numero no vacio ni nulo");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
