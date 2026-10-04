package investfacil.demo.usuario.domain.model;

import investfacil.demo.shared.domain.exception.DadoInvalidoException;

/**
 * Regra de senha forte. Valida a senha em texto puro, antes do hash,
 * por isso não fica dentro do Usuario (que só conhece o hash).
 */
public final class PoliticaDeSenha {

    static final int TAMANHO_MINIMO = 8;
    // Limite do BCrypt: bytes além do 72º são ignorados
    static final int TAMANHO_MAXIMO = 72;

    private PoliticaDeSenha() {
    }

    public static void validar(String senha) {
        if (senha == null || senha.length() < TAMANHO_MINIMO || senha.length() > TAMANHO_MAXIMO) {
            throw new DadoInvalidoException("senha",
                    "A senha deve ter entre %d e %d caracteres".formatted(TAMANHO_MINIMO, TAMANHO_MAXIMO));
        }
        boolean temLetra = senha.chars().anyMatch(Character::isLetter);
        boolean temNumero = senha.chars().anyMatch(Character::isDigit);
        if (!temLetra || !temNumero) {
            throw new DadoInvalidoException("senha", "A senha deve conter letras e números");
        }
    }
}
