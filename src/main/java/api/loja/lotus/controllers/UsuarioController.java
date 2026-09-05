package api.loja.lotus.controllers;

import api.loja.lotus.dtos.usuario.UsuarioAtualizarDTO;
import api.loja.lotus.dtos.usuario.UsuarioResponseDTO;
import api.loja.lotus.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/atualizar")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@RequestBody @Valid UsuarioAtualizarDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(dto));
    }

}
