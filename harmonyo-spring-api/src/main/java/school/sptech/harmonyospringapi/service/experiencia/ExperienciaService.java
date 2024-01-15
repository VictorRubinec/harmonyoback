package school.sptech.harmonyospringapi.service.experiencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.sptech.harmonyospringapi.domain.Experiencia;
import school.sptech.harmonyospringapi.domain.Professor;
import school.sptech.harmonyospringapi.repository.ExperienciaRepository;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.experiencia.dto.ExperienciaCriacaoDto;
import school.sptech.harmonyospringapi.service.experiencia.dto.ExperienciaExibicaoDto;
import school.sptech.harmonyospringapi.service.experiencia.dto.ExperienciaMapper;
import school.sptech.harmonyospringapi.service.usuario.ProfessorService;

import java.util.List;
import java.util.Optional;

@Service
public class ExperienciaService {

    @Autowired
    private ExperienciaRepository experienciaRepository;

    @Autowired
    private ProfessorService professorService;

    public ExperienciaExibicaoDto cadastrarExp(ExperienciaCriacaoDto novaExperiencia) {

        Integer professorId = novaExperiencia.getIdProfessor();
        Professor professor = this.professorService.buscarPorId(professorId);

        Experiencia experiencia = ExperienciaMapper.of(novaExperiencia, professor);

        Experiencia experienciaCadastrada = this.experienciaRepository.save(experiencia);

        return ExperienciaMapper.of(experienciaCadastrada);
    }

    public Experiencia buscarPorId(Integer id) {
        return experienciaRepository.findById(id).orElseThrow(
                () -> new EntitadeNaoEncontradaException("Experiência não encontrada")
        );
    }

    public List<ExperienciaExibicaoDto> buscarExperienciasPorIdProfessor(Integer idProfessor) {
        List<Experiencia> experiencias = this.experienciaRepository.findAllByProfessorId(idProfessor);

        return experiencias.stream().map(ExperienciaMapper::of).toList();
    }

    public ExperienciaExibicaoDto atualizarExperienciaPorId(int id, String titulo, String descricao) {
        Experiencia experiencia = this.buscarPorId(id);

        experiencia.setTitulo(titulo);
        experiencia.setDescricao(descricao);

        Experiencia experienciaCadastrada = this.experienciaRepository.save(experiencia);

        return ExperienciaMapper.of(experienciaCadastrada);
    }

    public void deletarExperienciaPorId(int id){
        if (this.experienciaRepository.existsById(id)) {
            this.experienciaRepository.deleteById(id);
        } else{
            throw new EntitadeNaoEncontradaException("ID de Experiência Inválido!");
        }
    }

}
