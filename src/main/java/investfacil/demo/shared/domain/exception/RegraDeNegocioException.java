package investfacil.demo.shared.domain.exception;

/**
 * Base das exceções lançadas pelo domínio. Não conhece HTTP: quem traduz
 * para status code é o adaptador web (GlobalExceptionHandler).
 * O campo é opcional e permite ao front destacar o input com erro.
 */
public abstract class RegraDeNegocioException extends RuntimeException {

    private final String campo;

    protected RegraDeNegocioException(String campo, String mensagem) {
        super(mensagem);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
