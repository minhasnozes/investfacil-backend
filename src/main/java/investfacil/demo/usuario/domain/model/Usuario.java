package investfacil.demo.usuario.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

import investfacil.demo.shared.domain.exception.DadoInvalidoException;

/**
 * Modelo de domínio do usuário: Java puro, sem JPA nem Spring.
 * Só é possível criar um Usuario válido (as regras ficam aqui dentro).
 */
public final class Usuario {

    static final int IDADE_MINIMA = 18;

    private final UUID id;
    private final String nome;
    private final Email email;
    private final String senhaHash;
    private final Cpf cpf;
    private final LocalDate dataNascimento;
    private final String telefone;
    private final PerfilInvestidorEnum perfilInvestidor;
    private final LocalDateTime dataCadastro;

    private Usuario(UUID id, String nome, Email email, String senhaHash, Cpf cpf, LocalDate dataNascimento,
            String telefone, PerfilInvestidorEnum perfilInvestidor, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.perfilInvestidor = perfilInvestidor;
        this.dataCadastro = dataCadastro;
    }

    /** Cria um usuário novo. O id é gerado pelo banco e o perfil é definido depois, no questionário. */
    public static Usuario novo(String nome, Email email, String senhaHash, Cpf cpf, LocalDate dataNascimento,
            String telefone, LocalDateTime agora) {
        if (nome == null || nome.isBlank()) {
            throw new DadoInvalidoException("nome", "Nome é obrigatório");
        }
        if (dataNascimento == null) {
            throw new DadoInvalidoException("dataNascimento", "Data de nascimento é obrigatória");
        }
        if (Period.between(dataNascimento, agora.toLocalDate()).getYears() < IDADE_MINIMA) {
            throw new DadoInvalidoException("dataNascimento",
                    "É preciso ter pelo menos %d anos para se cadastrar".formatted(IDADE_MINIMA));
        }
        String telefoneNormalizado = telefone == null || telefone.isBlank() ? null : telefone.trim();
        return new Usuario(null, nome.trim(), email, senhaHash, cpf, dataNascimento, telefoneNormalizado,
                null, agora);
    }

    /** Reconstrói um usuário que já existe (usado pelo adaptador de persistência). Não revalida. */
    public static Usuario restaurar(UUID id, String nome, Email email, String senhaHash, Cpf cpf,
            LocalDate dataNascimento, String telefone, PerfilInvestidorEnum perfilInvestidor,
            LocalDateTime dataCadastro) {
        return new Usuario(id, nome, email, senhaHash, cpf, dataNascimento, telefone, perfilInvestidor,
                dataCadastro);
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Email getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public PerfilInvestidorEnum getPerfilInvestidor() {
        return perfilInvestidor;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
}
