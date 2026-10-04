package investfacil.demo.usuario.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import investfacil.demo.shared.domain.exception.DadoInvalidoException;

/**
 * Testes unitários do modelo de domínio: não sobem o Spring nem o banco.
 */
class UsuarioTest {

    private static final LocalDateTime AGORA = LocalDateTime.of(2026, 10, 3, 10, 0);
    private static final Email EMAIL = new Email("maria@email.com");
    private static final Cpf CPF = new Cpf("52998224725");

    @Test
    void deveCriarUsuarioNovo() {
        Usuario usuario = Usuario.novo("  Maria Silva ", EMAIL, "hash", CPF, LocalDate.of(1990, 5, 20),
                " 11999998888 ", AGORA);

        assertThat(usuario.getNome()).isEqualTo("Maria Silva");
        assertThat(usuario.getEmail()).isEqualTo(EMAIL);
        assertThat(usuario.getSenhaHash()).isEqualTo("hash");
        assertThat(usuario.getCpf()).isEqualTo(CPF);
        assertThat(usuario.getTelefone()).isEqualTo("11999998888");
        assertThat(usuario.getDataCadastro()).isEqualTo(AGORA);
    }

    @Test
    void usuarioNovoNaoTemIdNemPerfil() {
        Usuario usuario = Usuario.novo("Maria", EMAIL, "hash", CPF, LocalDate.of(1990, 5, 20), null, AGORA);

        // id é gerado pelo banco; perfil é definido no questionário
        assertThat(usuario.getId()).isNull();
        assertThat(usuario.getPerfilInvestidor()).isNull();
    }

    @Test
    void telefoneEmBrancoViraNulo() {
        Usuario usuario = Usuario.novo("Maria", EMAIL, "hash", CPF, LocalDate.of(1990, 5, 20), "  ", AGORA);

        assertThat(usuario.getTelefone()).isNull();
    }

    @Test
    void deveExigirNome() {
        assertThatThrownBy(() -> Usuario.novo(" ", EMAIL, "hash", CPF, LocalDate.of(1990, 5, 20), null, AGORA))
                .isInstanceOf(DadoInvalidoException.class)
                .extracting("campo").isEqualTo("nome");
    }

    @Test
    void deveAceitarQuemFaz18AnosHoje() {
        LocalDate fez18Hoje = AGORA.toLocalDate().minusYears(18);

        assertThat(Usuario.novo("Maria", EMAIL, "hash", CPF, fez18Hoje, null, AGORA)).isNotNull();
    }

    @Test
    void deveRejeitarMenorDeIdade() {
        LocalDate faz18Amanha = AGORA.toLocalDate().minusYears(18).plusDays(1);

        assertThatThrownBy(() -> Usuario.novo("Maria", EMAIL, "hash", CPF, faz18Amanha, null, AGORA))
                .isInstanceOf(DadoInvalidoException.class)
                .extracting("campo").isEqualTo("dataNascimento");
    }
}
