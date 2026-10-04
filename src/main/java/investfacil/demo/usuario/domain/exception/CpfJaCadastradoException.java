package investfacil.demo.usuario.domain.exception;

import investfacil.demo.shared.domain.exception.RecursoJaExisteException;

public class CpfJaCadastradoException extends RecursoJaExisteException {

    public CpfJaCadastradoException() {
        super("cpf", "CPF já cadastrado");
    }
}
