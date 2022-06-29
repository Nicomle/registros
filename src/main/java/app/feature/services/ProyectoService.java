package app.feature.services;

import app.feature.entities.Empresa;
import app.feature.entities.Proyecto;
import app.feature.repositories.ProyectoRepository;
import app.core.exceptions.ErrorDetails;
import app.core.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private EmpresaService empresaService;

    public ResponseEntity<GlobalResponse> obtenerProyecto(Long id, HttpServletRequest request) {
        try {
            Optional<Proyecto> proyectoBase = proyectoRepository.findById(id);
            if (!proyectoBase.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    proyectoBase.get(), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar obtener registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> obtenerListaProyectos(HttpServletRequest request) {
        try {
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    proyectoRepository.findAll(), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar obtener lista de registros en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> guardarProyecto(Proyecto proyecto, HttpServletRequest request) {
        if (proyecto.getCompany().getId() == null || proyecto.getCompany().getId() <= 0) {
            ErrorDetails errorDetails = new ErrorDetails("El ID de la empresa ingresada es invalida.", "ID Empresa: " + proyecto.getCompany().getId());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        try {
            Empresa empresa = empresaService.obtenerEmpresaId(proyecto.getCompany().getId());
            if (empresa == null) {
                ErrorDetails errorDetails = new ErrorDetails("El ID de la empresa ingresada no existe.", "ID Empresa: " + proyecto.getCompany().getId());
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        null, errorDetails), HttpStatus.BAD_REQUEST);
            }
            proyecto.setCompany(empresa);
            proyecto.setStatus(true);
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    proyectoRepository.save(proyecto), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar guardar registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<GlobalResponse> eliminarProyecto(Long id, HttpServletRequest request) {
        try {
            Optional<Proyecto> proyectoBase = proyectoRepository.findById(id);
            if (!proyectoBase.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            proyectoRepository.delete(proyectoBase.get());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    proyectoBase.get(), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar eliminar registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<GlobalResponse> editarProyecto(Long id, Proyecto proyecto, HttpServletRequest request) {
        try {
            Optional<Proyecto> proyectoBase = proyectoRepository.findById(id);
            if (!proyectoBase.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            if (proyecto.getCompany().getId() == null || proyecto.getCompany().getId() <= 0) {
                ErrorDetails errorDetails = new ErrorDetails("El ID de la empresa ingresada es invalida.", "ID Empresa: " + proyecto.getCompany().getId());
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        null, errorDetails), HttpStatus.BAD_REQUEST);
            }
            if (proyectoBase.get().getCompany().getId() != proyecto.getCompany().getId()) {
                Empresa empresa = empresaService.obtenerEmpresaId(proyecto.getCompany().getId());
                if (empresa == null) {
                    ErrorDetails errorDetails = new ErrorDetails("El ID de la empresa ingresada no existe.", "ID Empresa: " + proyecto.getCompany().getId());
                    return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                            null, errorDetails), HttpStatus.BAD_REQUEST);
                }
                proyectoBase.get().setCompany(empresa);
            }
            proyectoBase.get().setName(proyecto.getName());
            proyectoBase.get().setStatus(proyecto.isStatus());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    proyectoRepository.save(proyectoBase.get()), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar editar registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Proyecto obtenerProyectoId(Long id) {
        Optional<Proyecto> proyecto = proyectoRepository.findById(id);
        if (proyecto.isPresent()) {
            return proyecto.get();
        } else {
            return null;
        }
    }
}
