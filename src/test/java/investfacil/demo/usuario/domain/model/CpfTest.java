package investfacil.demo.usuario.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import investfacil.demo.shared.domain.exception.DadoInvalidoException;

class CpfTest {

    @Test
    void deveAceitarCpfValidoSemMascara() {
        assertThat(new Cpf("52998224725").valor()).isEqualTo("52998224725");
    }

    @Test
    void deveRemoverMascara() {
        assertThat(new Cpf("529.982.247-25").valor()).isEqualTo("52998224725");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "52998224724", "12345678901", "11111111111", "5299822472", "529982247250", "abc" })
    void deveRejeitarCpfInvalido(String valor) {
        assertThatThrownBy(() -> new Cpf(valor))
                .isInstanceOf(DadoInvalidoException.class)
                .hasMessage("CPF inválido")
                .extracting("campo").isEqualTo("cpf");
    }
}
