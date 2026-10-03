package investfacil.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import investfacil.demo.entity.PerfilInvestidorEnum;
import investfacil.demo.entity.Usuario;

/**
 * Testes de integração do UsuarioRepository contra um Postgres real
 * (Testcontainers). As migrations do Flyway rodam antes dos testes, e cada
 * teste roda em uma transação que sofre rollback no final.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class UsuarioRepositoryIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveSalvarEBuscarUsuarioPorId() {
        Usuario salvo = usuarioRepository.saveAndFlush(novoUsuario("12345678901", "maria@email.com"));

        assertThat(salvo.getId()).isNotNull();
        assertThat(usuarioRepository.findById(salvo.getId()))
                .isPresent()
                .get()
                .extracting(Usuario::getNome)
                .isEqualTo("Maria Silva");
    }

    @Test
    void naoDevePermitirCpfDuplicado() {
        usuarioRepository.saveAndFlush(novoUsuario("12345678901", "maria@email.com"));

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(novoUsuario("12345678901", "outra@email.com")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private Usuario novoUsuario(String cpf, String email) {
        Usuario usuario = new Usuario();
        usuario.setNome("Maria Silva");
        usuario.setEmail(email);
        usuario.setSenhaHash("hash-de-teste");
        usuario.setCpf(cpf);
        usuario.setDataNascimento(LocalDate.of(1990, 5, 20));
        usuario.setPerfilInvestidor(PerfilInvestidorEnum.MODERADO);
        return usuario;
    }
}
