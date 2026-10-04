package investfacil.demo.usuario.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import investfacil.demo.usuario.domain.model.PerfilInvestidorEnum;

/**
 * Testes de integração do UsuarioJpaRepository contra um Postgres real
 * (Testcontainers). As migrations do Flyway rodam antes dos testes, e cada
 * teste roda em uma transação que sofre rollback no final.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class UsuarioJpaRepositoryIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private UsuarioJpaRepository usuarioRepository;

    @Test
    void deveSalvarEBuscarUsuarioPorId() {
        UsuarioJpaEntity salvo = usuarioRepository.saveAndFlush(novoUsuario("12345678901", "maria@email.com"));

        assertThat(salvo.getId()).isNotNull();
        assertThat(usuarioRepository.findById(salvo.getId()))
                .isPresent()
                .get()
                .extracting(UsuarioJpaEntity::getNome)
                .isEqualTo("Maria Silva");
    }

    @Test
    void naoDevePermitirCpfDuplicado() {
        usuarioRepository.saveAndFlush(novoUsuario("12345678901", "maria@email.com"));

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(novoUsuario("12345678901", "outra@email.com")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void naoDevePermitirEmailDuplicado() {
        usuarioRepository.saveAndFlush(novoUsuario("12345678901", "maria@email.com"));

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(novoUsuario("98765432100", "maria@email.com")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void deveVerificarExistenciaPorEmailECpf() {
        usuarioRepository.saveAndFlush(novoUsuario("12345678901", "maria@email.com"));

        assertThat(usuarioRepository.existsByEmail("maria@email.com")).isTrue();
        assertThat(usuarioRepository.existsByEmail("outra@email.com")).isFalse();
        assertThat(usuarioRepository.existsByCpf("12345678901")).isTrue();
        assertThat(usuarioRepository.existsByCpf("98765432100")).isFalse();
    }

    private UsuarioJpaEntity novoUsuario(String cpf, String email) {
        UsuarioJpaEntity usuario = new UsuarioJpaEntity();
        usuario.setNome("Maria Silva");
        usuario.setEmail(email);
        usuario.setSenhaHash("hash-de-teste");
        usuario.setCpf(cpf);
        usuario.setDataNascimento(LocalDate.of(1990, 5, 20));
        usuario.setPerfilInvestidor(PerfilInvestidorEnum.MODERADO);
        usuario.setDataCadastro(LocalDateTime.now());
        return usuario;
    }
}
