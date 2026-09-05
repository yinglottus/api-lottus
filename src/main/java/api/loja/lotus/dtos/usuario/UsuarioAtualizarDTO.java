package api.loja.lotus.dtos.usuario;

public record UsuarioAtualizarDTO(

        String nome,

        String email,

        String senhaAtual,

        String senhaNova
) {
}
