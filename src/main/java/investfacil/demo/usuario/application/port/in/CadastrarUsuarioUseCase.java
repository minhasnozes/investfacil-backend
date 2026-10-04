package investfacil.demo.usuario.application.port.in;

import investfacil.demo.usuario.domain.model.Usuario;

/**
 * Porta de entrada: o que a aplicação oferece ao mundo externo.
 * O controller depende desta interface, não da implementação.
 */
public interface CadastrarUsuarioUseCase {

    Usuario cadastrar(CadastrarUsuarioCommand command);
}
