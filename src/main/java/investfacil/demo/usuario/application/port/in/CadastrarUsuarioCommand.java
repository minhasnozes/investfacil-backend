package investfacil.demo.usuario.application.port.in;

import java.time.LocalDate;

/**
 * Dados de entrada do caso de uso. Independe de HTTP: o mesmo comando
 * poderia vir de uma API REST, de uma fila ou de um teste.
 */
public record CadastrarUsuarioCommand(
        String nome,
        String email,
        String senha,
        String cpf,
        LocalDate dataNascimento,
        String telefone) {

    // Evita que a senha apareça em logs caso o comando seja impresso
    @Override
    public String toString() {
        return "CadastrarUsuarioCommand[nome=%s, email=%s, senha=***, cpf=%s, dataNascimento=%s, telefone=%s]"
                .formatted(nome, email, cpf, dataNascimento, telefone);
    }
}
