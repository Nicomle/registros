package app.core.auth.controllers;

import app.core.auth.dtos.AuthUserLoggedIn;
import app.core.auth.exceptions.AuthException;
import app.core.auth.request.AuthRequest;
import app.core.auth.response.AuthResponse;
import app.core.auth.services.AuthService;
import app.core.auth.components.AuthValidator;
import app.core.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthValidator authValidator;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public GlobalResponse login(@RequestBody MultiValueMap<String, String> formParams, @RequestParam("grant_type") String grantType, HttpServletRequest request) throws AuthException {
        AuthUserLoggedIn user = authValidator.validate(formParams, grantType);
        AuthResponse token = authService.login(user);
        return GlobalResponse.globalResponse(HttpStatus.OK, request.getContextPath(), token, null);
    }

    @PostMapping(path = "/login")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public GlobalResponse login(@RequestBody AuthRequest authRequest, @RequestParam("grant_type") String grantType, HttpServletRequest request) throws AuthException {
        AuthUserLoggedIn user = authValidator.validate(authRequest, grantType);
        AuthResponse token = authService.login(user);
        return GlobalResponse.globalResponse(HttpStatus.OK, request.getContextPath(), token, null);
    }
}
