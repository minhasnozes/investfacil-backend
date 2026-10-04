package investfacil.demo.usuario.domain.model;

import investfacil.demo.shared.domain.exception.DadoInvalidoException;

/**
 * Value Object: um CPF só existe se for válido. Aceita com ou sem máscara
 * e guarda apenas os 11 dígitos.
 */
public record Cpf(String valor) {

    public Cpf {
        String digitos = valor == null ? "" : valor.replaceAll("\\D", "");
        if (!ehValido(digitos)) {
            throw new DadoInvalidoException("cpf", "CPF inválido");
        }
        valor = digitos;
    }

    private static boolean ehValido(String digitos) {
        // Sequências como 111.111.111-11 passam no cálculo, mas não são CPFs reais
        if (digitos.length() != 11 || digitos.chars().distinct().count() == 1) {
            return false;
        }
        return digitos.charAt(9) - '0' == digitoVerificador(digitos, 9)
                && digitos.charAt(10) - '0' == digitoVerificador(digitos, 10);
    }

    private static int digitoVerificador(String digitos, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += (digitos.charAt(i) - '0') * (quantidade + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }
}
