package net.diaz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.repository.CrudRepository;
import net.diaz.model.Categoria;

/*
public interface CategoriasRepository extends CrudRepository<Categoria, Integer> {
 
}  */


public interface CategoriasRepository extends JpaRepository<Categoria, Integer> {

}