package br.com.alura.literAlura.repository;

import br.com.alura.literAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    List<Autor> findByName(String name);

    @Query("SELECT p FROM Autor p WHERE p.birthYear IS NOT NULL AND (p.deathYear IS NULL OR :year <= p.deathYear) AND :year >= p.birthYear")
    List<Autor> findLivingAuthorsInYear(@Param("year") int year);

    @Query("SELECT p FROM Autor p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Autor> findByNameContainingIgnoreCase(@Param("name") String name);

}