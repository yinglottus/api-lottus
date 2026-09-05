package api.loja.lotus.mappers;

import api.loja.lotus.dtos.usuario.UsuarioRequestDTO;
import api.loja.lotus.dtos.usuario.UsuarioResponseDTO;
import api.loja.lotus.models.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequestDTO dto) {

        var usuario = new Usuario();
        usuario.setEmail(dto.email());
        usuario.setNome(dto.nome());

        return usuario;
    }

    public static UsuarioResponseDTO toDTO(Usuario usuario) {

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }

}
