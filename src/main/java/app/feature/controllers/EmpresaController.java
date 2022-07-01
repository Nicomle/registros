package app.feature.controllers;

import app.feature.entities.Empresa;
import app.feature.services.EmpresaService;
import app.core.response.GlobalResponse;
import app.feature.services.Validacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaServices;

    @Autowired
    private Validacion validacion;

    @GetMapping("/obtener")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GlobalResponse> obtenerEmpresa(@RequestParam String cuit, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarString(cuit, "Cuit", request);
        if (response != null) {
            return response;
        }
        return empresaServices.obtenerEmpresa(cuit, request);
    }

    @GetMapping("/obtener/lista")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GlobalResponse> obtenerListaEmpresas(HttpServletRequest request) {
        return empresaServices.obtenerListaEmpresas(request);
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> guardarEmpresa(@Valid @RequestBody Empresa empresa, BindingResult bindingResult, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarBindingResult(bindingResult, request);
        if (response != null) {
            return response;
        }
        return empresaServices.guardarEmpresa(empresa, request);
    }

    @DeleteMapping("/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> eliminarEmpresa(@RequestParam String cuit, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarString(cuit, "Cuit", request);
        if (response != null) {
            return response;
        }
        return empresaServices.eliminarEmpresa(cuit, request);
    }

    @PutMapping("/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> editarEmpresa(@RequestParam String id, @Valid @RequestBody Empresa empresa, BindingResult bindingResult, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarId(id, request);
        if (response != null) {
            return response;
        }
        response = validacion.validarBindingResult(bindingResult, request);
        if (response != null) {
            return response;
        }
        return empresaServices.editarEmpresa(Long.parseLong(id), empresa, request);
    }

    @PostMapping("/cambiar/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> cambiarStatus(@RequestParam String id, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarId(id, request);
        if (response != null) {
            return response;
        }
        return empresaServices.cambiarStatus(Long.parseLong(id), request);
    }
}
