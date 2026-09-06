package api.loja.lotus.dtos.usuario;

import jakarta.validation.constraints.Size;

public record UsuarioAtualizarDTO(

        String nome,

        @Size(min = 8, max = 20, message = "Senha tem que ter entre 8 a 20 caracteres!")
        String senhaAtual,

        @Size(min = 8, max = 20, message = "Senha tem que ter entre 8 a 20 caracteres!")
        String senhaNova
) {
}
