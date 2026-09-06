package api.loja.lotus.units;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import api.loja.lotus.dtos.usuario.UsuarioAtualizarDTO;
import api.loja.lotus.dtos.usuario.UsuarioDeletarDTO;
import api.loja.lotus.dtos.usuario.UsuarioResponseDTO;
import api.loja.lotus.exceptions.BusinessException;
import api.loja.lotus.exceptions.SenhaIncorretaException;
import api.loja.lotus.exceptions.UsuarioExistenteException;
import api.loja.lotus.factories.UsuarioFactoryTests;
import api.loja.lotus.models.Usuario;
import api.loja.lotus.models.enums.RoleUser;
import api.loja.lotus.repository.UsuarioRepository;
import api.loja.lotus.services.UsuarioService;
import api.loja.lotus.services.auth.UsuarioAutenticadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioAutenticadoService usuarioLogado;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final UsuarioFactoryTests usuarioFactory = new UsuarioFactoryTests();

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void usuarioLogado() {
        usuario = usuarioFactory.criarUsuario();
    }

    @Nested
    class CriarUsuarioTest {

        @Test
        void deveCriarUsuarioComSucesso() {

            var request = usuarioFactory.criarUsuarioRequest();

            when(usuarioRepository.existsByEmail(request.email()))
                    .thenReturn(false);

            UsuarioResponseDTO resultado = usuarioService.criarUsuario(request);

            assertThat(resultado.email()).isEqualTo(request.email());
            assertThat(resultado.nome()).isEqualTo(request.nome());
            assertThat(resultado.role()).isEqualTo(RoleUser.ROLE_USER);

            verify(usuarioRepository).save(any(Usuario.class));

            ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);

            verify(usuarioRepository).save(captor.capture());

            Usuario usuarioCapturado = captor.getValue();

            assertThat(usuarioCapturado.getEmail()).isEqualTo(request.email());
            assertThat(usuarioCapturado.getNome()).isEqualTo(request.nome());
        }

        @Test
        void deveImpedirCriacaoSeJaExistirUsuarioComEmail() {

            var request = usuarioFactory.criarUsuarioRequest();

            when(usuarioRepository.existsByEmail(request.email()))
                    .thenReturn(true);

            UsuarioExistenteException exception = assertThrows(
                    UsuarioExistenteException.class,
                    () -> usuarioService.criarUsuario(request)
            );

            assertThat(exception.getMessage()).isEqualTo("Usuário já existente com esse email!");

            verify(usuarioRepository, never()).save(any(Usuario.class));
        }

    }

    @Nested
    class AtualizarUsuarioTest {

        @Test
        void deveAtualizarUsuarioSemTrocarDeSenha() {

            var request = new UsuarioAtualizarDTO(
                    "saulo",
                    "091812",
                    null
            );

            when(usuarioLogado.usuarioLogado())
                    .thenReturn(usuario);

            when(passwordEncoder.matches(request.senhaAtual(), usuario.getSenha()))
                    .thenReturn(true);

            UsuarioResponseDTO resultado = usuarioService.atualizarUsuario(request);

            assertThat(resultado.nome()).isEqualTo(request.nome());

            ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);

            verify(usuarioRepository).save(captor.capture());

            Usuario usuarioCapturado = captor.getValue();

            assertThat(usuarioCapturado.getNome()).isEqualTo(request.nome());
        }

        @Test
        void deveAtualizarUsuarioTrocandoDeSenha() {

            var request = usuarioFactory.criarUsuarioAtualizarDto();

            when(usuarioLogado.usuarioLogado())
                    .thenReturn(usuario);

            when(passwordEncoder.matches(request.senhaAtual(), usuario.getSenha()))
                    .thenReturn(true);

            when(passwordEncoder.encode(request.senhaNova()))
                    .thenReturn(request.senhaNova());

            UsuarioResponseDTO resultado = usuarioService.atualizarUsuario(request);

            assertThat(resultado.nome()).isEqualTo(request.nome());

            ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);

            verify(usuarioRepository).save(captor.capture());

            Usuario usuarioCapturado = captor.getValue();

            assertThat(usuarioCapturado.getSenha()).isEqualTo(request.senhaNova());
        }

        @Test
        void deveImpedirAtualizarUsuarioSenhaIncorreta() {

            var request = usuarioFactory.criarUsuarioAtualizarDto();

            when(usuarioLogado.usuarioLogado())
                    .thenReturn(usuario);

            when(passwordEncoder.matches(request.senhaAtual(), usuario.getSenha()))
                    .thenReturn(false);

            SenhaIncorretaException exception = assertThrows(
                    SenhaIncorretaException.class,
                    () -> usuarioService.atualizarUsuario(request)
            );

            assertThat(exception.getMessage()).isEqualTo("Senha incorreta!");

            verify(usuarioRepository, never()).save(any(Usuario.class));
        }

        @Test
        void deveImpedirAtualizarEmailJaExistente() {

            var request = usuarioFactory.criarUsuarioAtualizarDto();

            when(usuarioLogado.usuarioLogado())
                    .thenReturn(usuario);

            when(passwordEncoder.matches(request.senhaAtual(), usuario.getSenha()))
                    .thenReturn(true);

            UsuarioExistenteException exception = assertThrows(
                    UsuarioExistenteException.class,
                    () -> usuarioService.atualizarUsuario(request)
            );

            assertThat(exception.getMessage()).isEqualTo("Usuário existente com esse email!");

            verify(usuarioRepository, never()).save(any(Usuario.class));
        }

    }

    @Nested
    class BuscarTodosUsuariosTest {

        @Test
        void deveBuscarTodosUsuariosComSucesso() {

            usuario.setRole(RoleUser.ROLE_ADMIN);

            var usuarioTeste = usuarioFactory.criarUsuario();
            usuarioTeste.setId(2L);

            var usuarioTeste2 = usuarioFactory.criarUsuario();
            usuarioTeste2.setId(3L);

            List<Usuario> usuarios = List.of(usuarioTeste, usuarioTeste2);

            Page<Usuario> page = new PageImpl<>(usuarios);

            Pageable pageable = PageRequest.of(0, 10);

            when(usuarioLogado.usuarioLogado())
                    .thenReturn(usuario);

            when(usuarioRepository.findAll(pageable))
                    .thenReturn(page);

            Page<UsuarioResponseDTO> resultado = usuarioService.buscarTodosUsuarios(pageable);

            assertThat(resultado.getContent()).extracting(UsuarioResponseDTO::id)
                    .containsExactlyInAnyOrder(usuarioTeste.getId(), usuarioTeste2.getId());
        }

        @Test
        void deveImpedirBuscarTodosUsuariosSemPermissao() {

            Pageable pageable = PageRequest.of(0, 10);

            when(usuarioLogado.usuarioLogado())
                    .thenReturn(usuario);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> usuarioService.buscarTodosUsuarios(pageable)
            );

            assertThat(exception.getMessage()).isEqualTo("Você não tem permissão de visualizar usuarios!");

            verify(usuarioRepository, never()).findAll(pageable);
        }

    }

    @Nested
    class BuscarMeusDadosTest {

        @Test
        void debeBuscarDadosDeUsuarioLogadoComSucesso() {

            when(usuarioLogado.usuarioLogado())
                    .thenReturn(usuario);

            UsuarioResponseDTO resultado = usuarioService.buscarMeusDados();

            assertThat(resultado.id()).isEqualTo(usuario.getId());
            assertThat(resultado.email()).isEqualTo(usuario.getEmail());
        }

    }

    @Nested
    class BuscarUsuarioPorIdTest {

        @Test
        void deveBuscarUsuarioPorIdComSucesso() {

            var usuarioBuscado = usuarioFactory.criarUsuario();
            usuarioBuscado.setId(2L);
            usuarioBuscado.setEmail("teste@gmail.com");

            usuario.setRole(RoleUser.ROLE_ADMIN);

            when(usuarioLogado.usuarioLogado())
                    .thenReturn(usuario);

            when(usuarioRepository.findById(usuarioBuscado.getId()))
                    .thenReturn(Optional.of(usuarioBuscado));

            UsuarioResponseDTO resultado = usuarioService.buscarUsuarioPorId(usuarioBuscado.getId());

            assertThat(resultado.id()).isEqualTo(usuarioBuscado.getId());
            assertThat(resultado.email()).isEqualTo(usuarioBuscado.getEmail());
        }

        @Test
        void deveImpedirUsuarioSemPermissaoBuscarUsuarioPorId() {

            when(usuarioLogado.usuarioLogado())
                    .thenReturn(usuario);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> usuarioService.buscarUsuarioPorId(2L)
            );

            assertThat(exception.getMessage()).isEqualTo("Você não tem acesso para visualizar usuários!");

            verify(usuarioRepository, never()).findById(any());
        }

    }

    @Nested
    class ExcluirUsuarioTest {

        @Test
        void deveExcluirUsuarioComSucesso() {

            UsuarioDeletarDTO dto = new UsuarioDeletarDTO(
                    "091812"
            );

            when(usuarioLogado.usuarioLogado())
                    .thenReturn(usuario);

            when(passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha()))
                    .thenReturn(true);

            usuarioService.excluirUsuario(dto);

            verify(usuarioRepository).delete(usuario);
        }

        @Test
        void deveImpedirExclusaoDeUsuarioSenhaIncorreta() {

            UsuarioDeletarDTO dto = new UsuarioDeletarDTO(
                    "091813"
            );

            when(usuarioLogado.usuarioLogado())
                    .thenReturn(usuario);

            when(passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha()))
                    .thenReturn(false);

            SenhaIncorretaException exception = assertThrows(
                    SenhaIncorretaException.class,
                    () -> usuarioService.excluirUsuario(dto)
            );

            assertThat(exception.getMessage()).isEqualTo("Senha incorreta!");

            verify(usuarioRepository, never()).delete(any(Usuario.class));
        }

    }

}
