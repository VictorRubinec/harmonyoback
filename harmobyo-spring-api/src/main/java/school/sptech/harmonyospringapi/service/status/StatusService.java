package school.sptech.harmonyospringapi.service.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.sptech.harmonyospringapi.domain.Status;
import school.sptech.harmonyospringapi.repository.StatusRepository;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.status.dto.StatusCriacaoDto;
import school.sptech.harmonyospringapi.service.status.dto.StatusExibicaoDto;
import school.sptech.harmonyospringapi.service.status.dto.StatusMapper;

import java.util.List;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    private List<StatusExibicaoDto> obterTodos() {
        return this.statusRepository.findAll()
                .stream()
                .map(StatusMapper::ofStatusExibicaoDto)
                .toList();
    }

    private StatusExibicaoDto criar(StatusCriacaoDto statusCriacaoDto) {

        Status status = StatusMapper.of(statusCriacaoDto);
        return StatusMapper.ofStatusExibicaoDto(this.statusRepository.save(status));
    }

    public Status buscarPorId(Integer id) {
        return statusRepository.findById(id).orElseThrow(
                () -> new EntitadeNaoEncontradaException("Status da Aula não encontrado")
        );
    }

    public Status buscarPorDescricao(String status) {
        return statusRepository.findByDescricao(status).orElseThrow(
                () -> new EntitadeNaoEncontradaException("Status da Aula não encontrado")
        );
    }
}
