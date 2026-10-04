package investfacil.demo.usuario.application.service;

import java.time.Clock;
import java.time.LocalDateTime;

import investfacil.demo.usuario.application.port.in.CadastrarUsuarioCommand;
import investfacil.demo.usuario.application.port.in.CadastrarUsuarioUseCase;
import investfacil.demo.usuario.application.port.out.HashSenhaPort;
import investfacil.demo.usuario.application.port.out.UsuarioRepositoryPort;
import investfacil.demo.usuario.domain.exception.CpfJaCadastradoException;
import investfacil.demo.usuario.domain.exception.EmailJaCadastradoException;
import investfacil.demo.usuario.domain.model.Cpf;
import investfacil.demo.usuario.domain.model.Email;
import investfacil.demo.usuario.domain.model.PoliticaDeSenha;
import investfacil.demo.usuario.domain.model.Usuario;

/**
 * Implementa o caso de uso orquestrando domínio e portas de saída.
 * Sem anotações do Spring: o bean é registrado em UsuarioConfig.
 */
public class CadastrarUsuarioService implements CadastrarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final HashSenhaPort hashSenha;
    private final Clock clock;

    public CadastrarUsuarioService(UsuarioRepositoryPort usuarioRepository, HashSenhaPort hashSenha, Clock clock) {
        this.usuarioRepository = usuarioRepository;
        this.hashSenha = hashSenha;
        this.clock = clock;
    }

    @Override
    public Usuario cadastrar(CadastrarUsuarioCommand command) {
        Email email = new Email(command.email());
        Cpf cpf = new Cpf(command.cpf());
        PoliticaDeSenha.validar(command.senha());

        if (usuarioRepository.existePorEmail(email)) {
            throw new EmailJaCadastradoException();
        }
        if (usuarioRepository.existePorCpf(cpf)) {
            throw new CpfJaCadastradoException();
        }

        Usuario usuario = Usuario.novo(
                command.nome(),
                email,
                hashSenha.gerarHash(command.senha()),
                cpf,
                command.dataNascimento(),
                command.telefone(),
                LocalDateTime.now(clock));

        return usuarioRepository.salvar(usuario);
    }
}
