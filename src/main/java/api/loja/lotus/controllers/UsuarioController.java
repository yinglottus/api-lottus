package api.loja.lotus.controllers;

import api.loja.lotus.dtos.usuario.UsuarioAtualizarDTO;
import api.loja.lotus.dtos.usuario.UsuarioDeletarDTO;
import api.loja.lotus.dtos.usuario.UsuarioResponseDTO;
import api.loja.lotus.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PutMapping("/atualizar")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@RequestBody @Valid UsuarioAtualizarDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/buscar/todos")
    public ResponseEntity<Page<UsuarioResponseDTO>> buscarTodosUsuarios(
            @PageableDefault(size = 12, sort = "nome") Pageable pageable
    )
    {
        return ResponseEntity.ok(usuarioService.buscarTodosUsuarios(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> buscarMeusDados() {
        return ResponseEntity.ok(usuarioService.buscarMeusDados());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/buscar")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorId(id));
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<Void> excluirUsuario(@RequestBody @Valid UsuarioDeletarDTO dto) {

        usuarioService.excluirUsuario(dto);

        return ResponseEntity.noContent().build();
    }

}
