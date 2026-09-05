package api.loja.lotus.dtos.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioAtualizarDTO(

        String nome,

        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 20)
        String senhaAtual,

        String senhaNova
) {
}
