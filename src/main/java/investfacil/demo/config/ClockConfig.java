package investfacil.demo.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Relógio da aplicação. Injetar o Clock (em vez de chamar LocalDateTime.now()
 * direto) permite fixar a data nos testes (ex.: regra de idade mínima).
 */
@Configuration
public class ClockConfig {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
