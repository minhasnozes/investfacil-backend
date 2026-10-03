package investfacil.demo.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Testes unitários da entidade Usuario: não sobem o Spring nem o banco.
 */
class UsuarioTest {

    @Test
    void deveArmazenarOsDadosInformados() {
        Usuario usuario = new Usuario();
        usuario.setNome("Maria Silva");
        usuario.setEmail("maria@email.com");
        usuario.setCpf("12345678901");
        usuario.setDataNascimento(LocalDate.of(1990, 5, 20));
        usuario.setPerfilInvestidor(PerfilInvestidorEnum.MODERADO);

        assertThat(usuario.getNome()).isEqualTo("Maria Silva");
        assertThat(usuario.getEmail()).isEqualTo("maria@email.com");
        assertThat(usuario.getCpf()).isEqualTo("12345678901");
        assertThat(usuario.getDataNascimento()).isEqualTo(LocalDate.of(1990, 5, 20));
        assertThat(usuario.getPerfilInvestidor()).isEqualTo(PerfilInvestidorEnum.MODERADO);
    }

    @Test
    void novoUsuarioNaoTemIdNemDataDeCadastro() {
        Usuario usuario = new Usuario();

        // id e dataCadastro são preenchidos pelo banco ao persistir
        assertThat(usuario.getId()).isNull();
        assertThat(usuario.getDataCadastro()).isNull();
    }
}
