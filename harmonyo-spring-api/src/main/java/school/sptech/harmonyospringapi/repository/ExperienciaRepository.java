package school.sptech.harmonyospringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import school.sptech.harmonyospringapi.domain.Experiencia;

import java.util.List;

@Repository
public interface ExperienciaRepository extends JpaRepository<Experiencia, Integer> {

    List<Experiencia> findAllByProfessorId(int id);

    @Modifying
    @Query("UPDATE Experiencia e SET e.titulo = :titulo, e.descricao = :descricao WHERE e.id = :id")
    void atualizarExperienciaPorId(int id, String titulo, String descricao);

}
