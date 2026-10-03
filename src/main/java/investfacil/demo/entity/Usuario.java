package investfacil.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity 
@Table (name="usuarios")
public class Usuario {
    @Id 
    @GeneratedValue (strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;
    
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;
    
    @Column(name = "email", nullable = false, length = 150)
    private String email;
    
    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;
    
    @Column(name = "cpf", nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "telefone", nullable = true, length = 20)
    private String telefone;

    @Column(name = "perfil_investidor", length = 20)
    @Enumerated(EnumType.STRING)
    private PerfilInvestidorEnum perfilInvestidor;

    @Column(name = "data_cadastro", insertable = false, updatable = false)
    private LocalDateTime dataCadastro;

}
