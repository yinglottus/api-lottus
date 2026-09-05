package api.loja.lotus.controllers;

import api.loja.lotus.dtos.login.LoginRequestDTO;
import api.loja.lotus.dtos.login.LoginResponseDTO;
import api.loja.lotus.dtos.usuario.UsuarioRequestDTO;
import api.loja.lotus.dtos.usuario.UsuarioResponseDTO;
import api.loja.lotus.services.UsuarioService;
import api.loja.lotus.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criarUsuario(dto));
    }

}
