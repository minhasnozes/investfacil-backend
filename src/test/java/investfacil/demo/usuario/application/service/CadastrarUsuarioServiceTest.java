package investfacil.demo.usuario.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import investfacil.demo.shared.domain.exception.DadoInvalidoException;
import investfacil.demo.usuario.application.port.in.CadastrarUsuarioCommand;
import investfacil.demo.usuario.application.port.out.HashSenhaPort;
import investfacil.demo.usuario.application.port.out.UsuarioRepositoryPort;
import investfacil.demo.usuario.domain.exception.CpfJaCadastradoException;
import investfacil.demo.usuario.domain.exception.EmailJaCadastradoException;
import investfacil.demo.usuario.domain.model.Cpf;
import investfacil.demo.usuario.domain.model.Email;
import investfacil.demo.usuario.domain.model.Usuario;

/**
 * Testa o caso de uso isolado: as portas de saída são mocks,
 * então não há banco nem Spring envolvidos.
 */
@ExtendWith(MockitoExtension.class)
class CadastrarUsuarioServiceTest {

    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-10-03T13:00:00Z"), ZoneOffset.UTC);

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private HashSenhaPort hashSenha;

    private CadastrarUsuarioService service;

    @BeforeEach
    void setUp() {
        service = new CadastrarUsuarioService(usuarioRepository, hashSenha, CLOCK);
    }

    @Test
    void deveCadastrarComSenhaEmHash() {
        when(hashSenha.gerarHash("senha123")).thenReturn("hash-bcrypt");
        when(usuarioRepository.salvar(any())).thenAnswer(inv -> comId(inv.getArgument(0)));

        Usuario cadastrado = service.cadastrar(command("Maria@Email.com", "529.982.247-25", "senha123"));

        ArgumentCaptor<Usuario> salvo = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).salvar(salvo.capture());
        assertThat(salvo.getValue().getSenhaHash()).isEqualTo("hash-bcrypt");
        assertThat(salvo.getValue().getEmail().valor()).isEqualTo("maria@email.com");
        assertThat(salvo.getValue().getCpf().valor()).isEqualTo("52998224725");
        assertThat(salvo.getValue().getDataCadastro()).isEqualTo(LocalDateTime.of(2026, 10, 3, 13, 0));
        assertThat(cadastrado.getId()).isNotNull();
    }

    @Test
    void naoDeveCadastrarEmailDuplicado() {
        when(usuarioRepository.existePorEmail(new Email("maria@email.com"))).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrar(command("maria@email.com", "52998224725", "senha123")))
                .isInstanceOf(EmailJaCadastradoException.class);
        verify(usuarioRepository, never()).salvar(any());
    }

    @Test
    void naoDeveCadastrarCpfDuplicado() {
        when(usuarioRepository.existePorCpf(new Cpf("52998224725"))).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrar(command("maria@email.com", "52998224725", "senha123")))
                .isInstanceOf(CpfJaCadastradoException.class);
        verify(usuarioRepository, never()).salvar(any());
    }

    @Test
    void naoDeveGerarHashNemConsultarBancoComSenhaFraca() {
        assertThatThrownBy(() -> service.cadastrar(command("maria@email.com", "52998224725", "fraca")))
                .isInstanceOf(DadoInvalidoException.class)
                .extracting("campo").isEqualTo("senha");
        verify(hashSenha, never()).gerarHash(any());
        verify(usuarioRepository, never()).existePorEmail(any());
    }

    private static CadastrarUsuarioCommand command(String email, String cpf, String senha) {
        return new CadastrarUsuarioCommand("Maria Silva", email, senha, cpf, LocalDate.of(1990, 5, 20), null);
    }

    private static Usuario comId(Usuario usuario) {
        return Usuario.restaurar(UUID.randomUUID(), usuario.getNome(), usuario.getEmail(), usuario.getSenhaHash(),
                usuario.getCpf(), usuario.getDataNascimento(), usuario.getTelefone(), usuario.getPerfilInvestidor(),
                usuario.getDataCadastro());
    }
}
