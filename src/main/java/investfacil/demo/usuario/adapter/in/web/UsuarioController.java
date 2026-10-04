package investfacil.demo.usuario.adapter.in.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import investfacil.demo.usuario.adapter.in.web.dto.CadastroUsuarioRequest;
import investfacil.demo.usuario.adapter.in.web.dto.UsuarioResponse;
import investfacil.demo.usuario.application.port.in.CadastrarUsuarioUseCase;
import investfacil.demo.usuario.domain.model.Usuario;
import jakarta.validation.Valid;

/**
 * Adaptador de entrada: traduz HTTP para o caso de uso. Não tem regra de negócio.
 * Os erros são convertidos em resposta pelo GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final CadastrarUsuarioUseCase cadastrarUsuario;

    public UsuarioController(CadastrarUsuarioUseCase cadastrarUsuario) {
        this.cadastrarUsuario = cadastrarUsuario;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody CadastroUsuarioRequest request) {
        Usuario usuario = cadastrarUsuario.cadastrar(request.paraCommand());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuario.getId())
                .toUri();
        return ResponseEntity.created(location).body(UsuarioResponse.de(usuario));
    }
}
