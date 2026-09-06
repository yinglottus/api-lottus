package api.loja.lotus.factories;

import api.loja.lotus.dtos.usuario.UsuarioAtualizarDTO;
import api.loja.lotus.dtos.usuario.UsuarioDeletarDTO;
import api.loja.lotus.dtos.usuario.UsuarioRequestDTO;
import api.loja.lotus.models.Usuario;
import api.loja.lotus.models.enums.RoleUser;

public class UsuarioFactoryTests {

    public Usuario criarUsuario() {

        var usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("teste");
        usuario.setEmail("teste@gmail.com");
        usuario.setRole(RoleUser.ROLE_USER);
        usuario.setSenha("091812");

        return usuario;
    }

    public UsuarioRequestDTO criarUsuarioRequest() {
        return new UsuarioRequestDTO(
                "teste",
                "teste@gmail.com",
                "091812"
        );
    }

    public UsuarioAtualizarDTO criarUsuarioAtualizarDto() {
        return new UsuarioAtualizarDTO(
                "saulo",
                "091812",
                "09181212"
        );
    }

}

