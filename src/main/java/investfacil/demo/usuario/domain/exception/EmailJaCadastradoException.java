package investfacil.demo.usuario.domain.exception;

import investfacil.demo.shared.domain.exception.RecursoJaExisteException;

public class EmailJaCadastradoException extends RecursoJaExisteException {

    public EmailJaCadastradoException() {
        super("email", "E-mail já cadastrado");
    }
}
