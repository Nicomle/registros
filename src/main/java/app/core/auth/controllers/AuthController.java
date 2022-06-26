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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthValidator authValidator;

    @PostMapping(path = "/login")
    public ResponseEntity<GlobalResponse> login(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult,
                                @RequestParam("grant_type") String grantType, HttpServletRequest request) throws AuthException {
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> {
                errors.add(error.getDefaultMessage());
            });
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", String.join(", ", errors));
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        AuthUserLoggedIn user = authValidator.validate(authRequest, grantType);
        AuthResponse token = authService.login(user);
        return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                token, null), HttpStatus.OK);
    }
}
