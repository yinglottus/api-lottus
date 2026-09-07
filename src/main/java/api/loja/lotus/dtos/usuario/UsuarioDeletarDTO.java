package api.loja.lotus.dtos.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDeletarDTO(

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, max = 20, message = "Senha tem que ter entre 8 a 20 caracteres!")
        String senhaAtual
) {
}
