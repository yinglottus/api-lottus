package api.loja.lotus.dtos.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 20)
        String senha
) {
}
