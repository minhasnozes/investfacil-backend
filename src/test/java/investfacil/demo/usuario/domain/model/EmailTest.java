package investfacil.demo.usuario.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import investfacil.demo.shared.domain.exception.DadoInvalidoException;

class EmailTest {

    @Test
    void deveNormalizarParaMinusculasSemEspacos() {
        assertThat(new Email("  Maria@Email.COM ").valor()).isEqualTo("maria@email.com");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "maria", "maria@", "maria@email", "@email.com", "ma ria@email.com" })
    void deveRejeitarEmailInvalido(String valor) {
        assertThatThrownBy(() -> new Email(valor))
                .isInstanceOf(DadoInvalidoException.class)
                .extracting("campo").isEqualTo("email");
    }

    @Test
    void deveRejeitarEmailMaiorQue150Caracteres() {
        String longo = "a".repeat(141) + "@email.com";

        assertThatThrownBy(() -> new Email(longo)).isInstanceOf(DadoInvalidoException.class);
    }
}
