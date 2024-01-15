package school.sptech.harmonyospringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import school.sptech.harmonyospringapi.domain.Aluno;

import java.time.LocalDateTime;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Integer> {

    @Query("SELECT COUNT(a) FROM Aluno a WHERE a.dataCriacao BETWEEN :dataInicial AND :dataFinal")
    Integer obterQuantidadeCadastradosEntre(LocalDateTime dataInicial, LocalDateTime dataFinal);
}
