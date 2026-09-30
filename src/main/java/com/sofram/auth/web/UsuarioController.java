package com.sofram.auth.web;

import com.sofram.auth.application.UsuarioAdminService;
import com.sofram.auth.web.dto.ActualizarUsuarioRequest;
import com.sofram.auth.web.dto.CambiarPasswordUsuarioRequest;
import com.sofram.auth.web.dto.CrearUsuarioRequest;
import com.sofram.auth.web.dto.RolResponse;
import com.sofram.auth.web.dto.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioAdminService usuarioAdminService;

    public UsuarioController(
            UsuarioAdminService usuarioAdminService
    ) {
        this.usuarioAdminService = usuarioAdminService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(
            @Valid @RequestBody CrearUsuarioRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioAdminService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioAdminService.listar());
    }

    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<UsuarioResponse> buscarPorEmpleado(
            @PathVariable Long empleadoId
    ) {
        return ResponseEntity.ok(
                usuarioAdminService.buscarPorEmpleado(empleadoId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioRequest request
    ) {
        return ResponseEntity.ok(
                usuarioAdminService.actualizar(id, request)
        );
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<UsuarioResponse> cambiarPassword(
            @PathVariable Long id,
            @Valid @RequestBody CambiarPasswordUsuarioRequest request
    ) {
        return ResponseEntity.ok(
                usuarioAdminService.cambiarPassword(id, request)
        );
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RolResponse>> listarRoles() {
        return ResponseEntity.ok(usuarioAdminService.listarRoles());
    }
}
