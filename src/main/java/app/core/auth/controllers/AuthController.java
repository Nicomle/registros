package app.core.auth.controllers;

import app.core.auth.dtos.AuthUserLoggedIn;
import app.core.auth.exceptions.AuthException;
import app.core.auth.request.AuthRequest;
import app.core.auth.response.AuthResponse;
import app.core.auth.services.AuthService;
import app.core.auth.components.AuthValidator;
import app.core.exceptions.ErrorDetails;
import app.core.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthValidator authValidator;

    @Value("${CLIENT_CREDENTIALS}")
    private String CLIENT_CREDENTIALS;

    @PostMapping(path = "/login")
    public ResponseEntity<GlobalResponse> login(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult,
                                                @RequestParam("grant_type") String grantType, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        if (grantType.isEmpty() || !grantType.equals(CLIENT_CREDENTIALS)) {
            errors.add("Grant Type invalido");
        }
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                errors.add(error.getDefaultMessage());
            });
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos", String.join(". ", errors));
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        try {
            AuthUserLoggedIn user = authValidator.validarBuscarUsuario(authRequest);
            AuthResponse token = authService.login(user);
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    token, null), HttpStatus.OK);
        } catch (AuthException e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al acceder usuario", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/roles/obtener/lista")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> obtenerRoles(HttpServletRequest request) {
        return authService.obtenerListaRoles(request);
    }
}
