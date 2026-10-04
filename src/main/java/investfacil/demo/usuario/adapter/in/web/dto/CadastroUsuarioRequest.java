package investfacil.demo.usuario.adapter.in.web.dto;

import java.time.LocalDate;

import investfacil.demo.usuario.application.port.in.CadastrarUsuarioCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

/**
 * JSON recebido em POST /usuarios. As anotações validam o formato da requisição
 * (campos obrigatórios, tamanhos). Regras de negócio, como CPF válido, senha forte
 * e idade mínima, ficam no domínio.
 */
public record CadastroUsuarioRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 150, message = "E-mail deve ter no máximo 150 caracteres")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String senha,

        @NotBlank(message = "CPF é obrigatório")
        String cpf,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve estar no passado")
        LocalDate dataNascimento,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        String telefone) {

    public CadastrarUsuarioCommand paraCommand() {
        return new CadastrarUsuarioCommand(nome, email, senha, cpf, dataNascimento, telefone);
    }

    @Override
    public String toString() {
        return "CadastroUsuarioRequest[nome=%s, email=%s, senha=***, cpf=%s, dataNascimento=%s, telefone=%s]"
                .formatted(nome, email, cpf, dataNascimento, telefone);
    }
}
