package school.sptech.harmonyospringapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import school.sptech.harmonyospringapi.domain.Notificacao;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {


    Page<Notificacao> findByUsuarioId(Integer idUsuario, Pageable pageable);

    List<Notificacao> findByUsuarioId(Integer idUsuario);

    int countByUsuarioIdAndLidaFalse(Integer idUsuario);
}
