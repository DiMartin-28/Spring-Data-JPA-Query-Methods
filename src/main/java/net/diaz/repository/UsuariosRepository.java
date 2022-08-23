package net.diaz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import net.diaz.model.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer>{

}
