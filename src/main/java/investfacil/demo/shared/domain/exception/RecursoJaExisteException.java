package investfacil.demo.shared.domain.exception;

/** Tentativa de criar algo que já existe (ex.: e-mail já cadastrado). */
public class RecursoJaExisteException extends RegraDeNegocioException {

    public RecursoJaExisteException(String campo, String mensagem) {
        super(campo, mensagem);
    }
}
