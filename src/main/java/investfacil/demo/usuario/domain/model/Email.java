package investfacil.demo.usuario.domain.model;

import java.util.Locale;
import java.util.regex.Pattern;

import investfacil.demo.shared.domain.exception.DadoInvalidoException;

/** Value Object: e-mail normalizado (sem espaços e em minúsculas). */
public record Email(String valor) {

    private static final Pattern FORMATO = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final int TAMANHO_MAXIMO = 150;

    public Email {
        String normalizado = valor == null ? "" : valor.trim().toLowerCase(Locale.ROOT);
        if (normalizado.length() > TAMANHO_MAXIMO || !FORMATO.matcher(normalizado).matches()) {
            throw new DadoInvalidoException("email", "E-mail inválido");
        }
        valor = normalizado;
    }
}
