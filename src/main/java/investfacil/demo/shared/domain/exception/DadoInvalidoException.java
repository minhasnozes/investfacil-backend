package investfacil.demo.shared.domain.exception;

/** Um dado viola uma regra do domínio (ex.: CPF inválido, senha fraca). */
public class DadoInvalidoException extends RegraDeNegocioException {

    public DadoInvalidoException(String campo, String mensagem) {
        super(campo, mensagem);
    }
}
