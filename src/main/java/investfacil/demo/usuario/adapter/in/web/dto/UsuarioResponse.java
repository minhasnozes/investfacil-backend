package investfacil.demo.usuario.adapter.in.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import investfacil.demo.usuario.domain.model.PerfilInvestidorEnum;
import investfacil.demo.usuario.domain.model.Usuario;

/** JSON devolvido pela API. Nunca expõe senha nem hash. */
public record UsuarioResponse(
        UUID id,
        String nome,
        String email,
        PerfilInvestidorEnum perfilInvestidor,
        LocalDateTime dataCadastro) {

    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail().valor(),
                usuario.getPerfilInvestidor(),
                usuario.getDataCadastro());
    }
}
