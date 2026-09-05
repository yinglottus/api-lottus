package api.loja.lotus.dtos.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(

        @NotBlank(message = "Nome é obrigatório!")
        String nome,

        @NotBlank(message = "Email é obrigatório!")
        @Email
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, max = 20)
        String senha
) {
}
