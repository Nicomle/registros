package app.feature.controllers;

import app.core.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/version")
public class VersionController {

    @Value("${version.app}")
    private String version;

    @GetMapping("/obtener")
    public ResponseEntity<GlobalResponse> obtenerVersion(HttpServletRequest request) {
        String versionResponse = "Version: " + version;
        return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                versionResponse, null), HttpStatus.OK);
    }
}
