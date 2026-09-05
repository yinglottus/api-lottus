package api.loja.lotus.dtos.usuario;

import api.loja.lotus.models.enums.RoleUser;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        RoleUser role
) {
}
