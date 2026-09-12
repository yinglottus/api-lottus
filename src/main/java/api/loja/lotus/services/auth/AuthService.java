package api.loja.lotus.services.auth;

import api.loja.lotus.dtos.login.LoginRequestDTO;
import api.loja.lotus.dtos.login.LoginResponseDTO;
import api.loja.lotus.models.Usuario;
import api.loja.lotus.security.JwtService;
import api.loja.lotus.services.CarrinhoService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CarrinhoService carrinhoService;

    public LoginResponseDTO login(LoginRequestDTO dto, String cartToken) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                dto.email(),
                                dto.senha()
                        )
                );

        Usuario usuario = (Usuario) authentication.getPrincipal();

        assert usuario != null;

        carrinhoService.associarOuMergearCarrinho(cartToken, usuario);

        String token = jwtService.gerarToken(usuario);

        return new LoginResponseDTO(token);
    }

}
