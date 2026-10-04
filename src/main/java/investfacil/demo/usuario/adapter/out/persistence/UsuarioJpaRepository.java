package investfacil.demo.usuario.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/** Repositório Spring Data. Só o UsuarioPersistenceAdapter usa esta interface. */
public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpaEntity, UUID> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
