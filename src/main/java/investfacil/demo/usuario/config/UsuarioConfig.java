package investfacil.demo.usuario.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import investfacil.demo.usuario.application.port.in.CadastrarUsuarioUseCase;
import investfacil.demo.usuario.application.port.out.HashSenhaPort;
import investfacil.demo.usuario.application.port.out.UsuarioRepositoryPort;
import investfacil.demo.usuario.application.service.CadastrarUsuarioService;

/**
 * Liga as peças do módulo: o caso de uso recebe as implementações das portas
 * de saída (adaptadores). É o único ponto em que a aplicação "encontra" o Spring.
 */
@Configuration
public class UsuarioConfig {

    @Bean
    CadastrarUsuarioUseCase cadastrarUsuarioUseCase(UsuarioRepositoryPort usuarioRepository,
            HashSenhaPort hashSenha, Clock clock) {
        return new CadastrarUsuarioService(usuarioRepository, hashSenha, clock);
    }
}
