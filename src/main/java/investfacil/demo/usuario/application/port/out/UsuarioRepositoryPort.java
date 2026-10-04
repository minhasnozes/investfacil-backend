package investfacil.demo.usuario.application.port.out;

import investfacil.demo.usuario.domain.model.Cpf;
import investfacil.demo.usuario.domain.model.Email;
import investfacil.demo.usuario.domain.model.Usuario;

/**
 * Porta de saída: o que a aplicação precisa da persistência.
 * Quem implementa é o adaptador JPA; a aplicação não sabe que existe banco.
 */
public interface UsuarioRepositoryPort {

    Usuario salvar(Usuario usuario);

    boolean existePorEmail(Email email);

    boolean existePorCpf(Cpf cpf);
}
