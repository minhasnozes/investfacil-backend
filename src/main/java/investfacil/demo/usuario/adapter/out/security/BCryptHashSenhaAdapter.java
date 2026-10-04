package investfacil.demo.usuario.adapter.out.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import investfacil.demo.usuario.application.port.out.HashSenhaPort;

/** Adaptador de saída: implementa HashSenhaPort com BCrypt. */
@Component
public class BCryptHashSenhaAdapter implements HashSenhaPort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String gerarHash(String senhaPura) {
        return encoder.encode(senhaPura);
    }
}
