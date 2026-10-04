package investfacil.demo.usuario.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import investfacil.demo.usuario.adapter.out.persistence.UsuarioJpaRepository;

/**
 * Teste de integração ponta a ponta do cadastro: HTTP → controller → caso de uso
 * → adaptador JPA → Postgres real (Testcontainers). @Transactional desfaz as
 * inserções ao fim de cada teste (o MockMvc roda na mesma thread do teste).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class UsuarioControllerIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioJpaRepository usuarioRepository;

    @Test
    void ct101_deveCadastrarComDadosValidos() throws Exception {
        cadastrar(json("Maria@Email.com", "529.982.247-25", "senha123", "1990-05-20"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/usuarios/")))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nome").value("Maria Silva"))
                .andExpect(jsonPath("$.email").value("maria@email.com"))
                .andExpect(jsonPath("$.perfilInvestidor").doesNotExist())
                .andExpect(jsonPath("$.dataCadastro").isNotEmpty())
                .andExpect(jsonPath("$", not(hasKey("senha"))))
                .andExpect(jsonPath("$", not(hasKey("senhaHash"))));
    }

    @Test
    void ct116_senhaDeveSerArmazenadaComHash() throws Exception {
        cadastrar(json("maria@email.com", "52998224725", "senha123", "1990-05-20"))
                .andExpect(status().isCreated());

        String senhaHash = usuarioRepository.findAll().getFirst().getSenhaHash();
        assertThat(senhaHash).isNotEqualTo("senha123").startsWith("$2");
    }

    @Test
    void ct102_deveRetornar409ComEmailJaCadastrado() throws Exception {
        cadastrar(json("maria@email.com", "52998224725", "senha123", "1990-05-20"))
                .andExpect(status().isCreated());

        cadastrar(json("MARIA@email.com", "11144477735", "senha123", "1990-05-20"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.erros.email").value("E-mail já cadastrado"));
    }

    @Test
    void deveRetornar409ComCpfJaCadastrado() throws Exception {
        cadastrar(json("maria@email.com", "52998224725", "senha123", "1990-05-20"))
                .andExpect(status().isCreated());

        cadastrar(json("outra@email.com", "529.982.247-25", "senha123", "1990-05-20"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.erros.cpf").value("CPF já cadastrado"));
    }

    @Test
    void ct103_deveRetornar400ComSenhaFraca() throws Exception {
        cadastrar(json("maria@email.com", "52998224725", "somenteletras", "1990-05-20"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.senha").value("A senha deve conter letras e números"));
    }

    @Test
    void ct104_deveRetornar400ComCamposObrigatoriosVazios() throws Exception {
        cadastrar("{}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.nome").value("Nome é obrigatório"))
                .andExpect(jsonPath("$.erros.email").value("E-mail é obrigatório"))
                .andExpect(jsonPath("$.erros.senha").value("Senha é obrigatória"))
                .andExpect(jsonPath("$.erros.cpf").value("CPF é obrigatório"))
                .andExpect(jsonPath("$.erros.dataNascimento").value("Data de nascimento é obrigatória"));
    }

    @Test
    void deveRetornar400ComCpfInvalido() throws Exception {
        cadastrar(json("maria@email.com", "12345678901", "senha123", "1990-05-20"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.cpf").value("CPF inválido"));
    }

    @Test
    void deveRetornar400ParaMenorDeIdade() throws Exception {
        cadastrar(json("maria@email.com", "52998224725", "senha123", "2015-01-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.dataNascimento").exists());
    }

    @Test
    void deveRetornar400ComJsonMalformado() throws Exception {
        cadastrar(json("maria@email.com", "52998224725", "senha123", "20/05/1990"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Corpo da requisição inválido ou com formato incorreto"));
    }

    private ResultActions cadastrar(String corpo) throws Exception {
        return mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpo));
    }

    private static String json(String email, String cpf, String senha, String dataNascimento) {
        return """
                {
                  "nome": "Maria Silva",
                  "email": "%s",
                  "senha": "%s",
                  "cpf": "%s",
                  "dataNascimento": "%s",
                  "telefone": "11999998888"
                }
                """.formatted(email, senha, cpf, dataNascimento);
    }
}
