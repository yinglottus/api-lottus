package api.loja.lotus.dtos.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 20, message = "Senha deve ser entre 8 a 20 caracteres!")
        String senha
) {
}
