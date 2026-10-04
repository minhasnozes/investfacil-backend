package investfacil.demo.usuario.domain.model;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import investfacil.demo.shared.domain.exception.DadoInvalidoException;

class PoliticaDeSenhaTest {

    @ParameterizedTest
    @ValueSource(strings = { "senha123", "Abcdefg1", "12345678a" })
    void deveAceitarSenhaForte(String senha) {
        assertThatCode(() -> PoliticaDeSenha.validar(senha)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "abc123", "senha12" })
    void deveRejeitarSenhaCurta(String senha) {
        assertThatThrownBy(() -> PoliticaDeSenha.validar(senha))
                .isInstanceOf(DadoInvalidoException.class)
                .hasMessageContaining("entre 8 e 72");
    }

    @Test
    void deveRejeitarSenhaMaiorQue72Caracteres() {
        assertThatThrownBy(() -> PoliticaDeSenha.validar("a1".repeat(37)))
                .isInstanceOf(DadoInvalidoException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = { "somenteletras", "12345678" })
    void deveExigirLetrasENumeros(String senha) {
        assertThatThrownBy(() -> PoliticaDeSenha.validar(senha))
                .isInstanceOf(DadoInvalidoException.class)
                .hasMessage("A senha deve conter letras e números");
    }
}
