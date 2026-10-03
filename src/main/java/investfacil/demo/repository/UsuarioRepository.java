package investfacil.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import investfacil.demo.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    
}
