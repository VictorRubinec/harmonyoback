package school.sptech.harmonyospringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.harmonyospringapi.domain.Naipe;

public interface NaipeRepository extends JpaRepository<Naipe, Integer> {

    boolean existsNaipeByDescricaoIgnoreCase(String descricaoNaipe);
}
