package api.loja.lotus.services.auth;

import api.loja.lotus.exceptions.BusinessException;
import api.loja.lotus.models.Usuario;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioAutenticadoService {

    public Usuario usuarioLogado() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
            !authentication.isAuthenticated() ||
            !(authentication.getPrincipal() instanceof Usuario usuario)
        )
        {

            throw new BusinessException("Usuário não autenticado!");
        }

        return usuario;
    }

    public Optional<Usuario> usuarioAtual() {
        
        Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();

        if (authentication == null ||
            !(authentication.isAuthenticated()) ||
            !(authentication.getPrincipal() instanceof Usuario usuario)     
        )
        {
            return Optional.empty();
        }

        return Optional.of(usuario);
    }

}
