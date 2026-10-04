package investfacil.demo.usuario.application.port.out;

/** Porta de saída: gera o hash da senha. O algoritmo (BCrypt) fica no adaptador. */
public interface HashSenhaPort {

    String gerarHash(String senhaPura);
}
