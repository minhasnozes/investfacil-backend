package investfacil.demo.usuario.adapter.out.persistence;

import org.springframework.stereotype.Component;

import investfacil.demo.usuario.application.port.out.UsuarioRepositoryPort;
import investfacil.demo.usuario.domain.model.Cpf;
import investfacil.demo.usuario.domain.model.Email;
import investfacil.demo.usuario.domain.model.Usuario;

/**
 * Adaptador de saída: implementa a porta UsuarioRepositoryPort usando JPA.
 * Converte o modelo de domínio para a entidade JPA e vice-versa.
 */
@Component
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioPersistenceAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioJpaEntity salvo = jpaRepository.save(paraEntidade(usuario));
        return paraDominio(salvo);
    }

    @Override
    public boolean existePorEmail(Email email) {
        return jpaRepository.existsByEmail(email.valor());
    }

    @Override
    public boolean existePorCpf(Cpf cpf) {
        return jpaRepository.existsByCpf(cpf.valor());
    }

    private static UsuarioJpaEntity paraEntidade(Usuario usuario) {
        UsuarioJpaEntity entidade = new UsuarioJpaEntity();
        entidade.setId(usuario.getId());
        entidade.setNome(usuario.getNome());
        entidade.setEmail(usuario.getEmail().valor());
        entidade.setSenhaHash(usuario.getSenhaHash());
        entidade.setCpf(usuario.getCpf().valor());
        entidade.setDataNascimento(usuario.getDataNascimento());
        entidade.setTelefone(usuario.getTelefone());
        entidade.setPerfilInvestidor(usuario.getPerfilInvestidor());
        entidade.setDataCadastro(usuario.getDataCadastro());
        return entidade;
    }

    private static Usuario paraDominio(UsuarioJpaEntity entidade) {
        return Usuario.restaurar(
                entidade.getId(),
                entidade.getNome(),
                new Email(entidade.getEmail()),
                entidade.getSenhaHash(),
                new Cpf(entidade.getCpf()),
                entidade.getDataNascimento(),
                entidade.getTelefone(),
                entidade.getPerfilInvestidor(),
                entidade.getDataCadastro());
    }
}
