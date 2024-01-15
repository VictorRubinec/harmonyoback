package school.sptech.harmonyospringapi.service.aula;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.sptech.harmonyospringapi.domain.*;

import school.sptech.harmonyospringapi.repository.AulaRepository;
import school.sptech.harmonyospringapi.service.aula.dto.AulaAtualizacaoDto;
import school.sptech.harmonyospringapi.service.aula.dto.AulaCriacaoDto;
import school.sptech.harmonyospringapi.service.aula.dto.AulaExibicaoDto;
import school.sptech.harmonyospringapi.service.aula.dto.AulaMapper;
import school.sptech.harmonyospringapi.service.exceptions.EntidadeConflitanteException;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.instrumento.InstrumentoService;
import school.sptech.harmonyospringapi.service.usuario.AlunoService;
import school.sptech.harmonyospringapi.service.usuario.ProfessorService;
import school.sptech.harmonyospringapi.service.usuario.UsuarioService;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AulaService {

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private InstrumentoService instrumentoService;

    @Autowired
    private AlunoService alunoService;


    public List<AulaExibicaoDto> obterTodos() {
        return this.aulaRepository.findAll()
                .stream()
                .map(AulaMapper::ofAulaExibicaoDto)
                .toList();
    }

    public Integer obterQuantidadeAulasCadastradas() {
        return Math.toIntExact(this.aulaRepository.count());
    }

    public Integer obterQuantidadeAulasCadastradasNesseMes() {
        return Math.toIntExact(this.aulaRepository.countByDataAulaBetween(LocalDate.now().withDayOfMonth(1).atStartOfDay(), LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59)));
    }

    public Double quantidadeUsuariosPorAluno() {
        return Double.parseDouble(new DecimalFormat("#.##").format(((double) this.aulaRepository.count()) / this.alunoService.obterQuantidadeAlunos()).replace(',', '.'));
    }

    public Double quantidadeUsuariosPorAlunoNesseMes() {
        return Double.parseDouble(new DecimalFormat("#.##").format(((double) this.aulaRepository.countByDataAulaBetween(LocalDate.now().withDayOfMonth(1).atStartOfDay(), LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59))) / this.alunoService.obterQuantidadeAlunos()).replace(',', '.'));
    }

    public AulaExibicaoDto cadastrarAula(AulaCriacaoDto aulaCriacaoDto) {
        Integer professorId = aulaCriacaoDto.getProfessorId();
        Integer instrumentoId = aulaCriacaoDto.getInstrumentoId();

        if (this.aulaExiste(professorId, instrumentoId)) throw new EntidadeConflitanteException("Aula já cadastrada !");

        Professor professor = this.professorService.buscarPorId(aulaCriacaoDto.getProfessorId());
        Instrumento instrumento = this.instrumentoService.buscarPorId(aulaCriacaoDto.getInstrumentoId());

        Aula aula = AulaMapper.of(aulaCriacaoDto, professor, instrumento);

        Aula aulaCadastrada = this.aulaRepository.save(aula);

        return AulaMapper.ofAulaExibicaoDto(aulaCadastrada);
    }

    public Aula buscarPorId(Integer id) {
        return aulaRepository.findById(id).orElseThrow(
                () -> new EntitadeNaoEncontradaException("Aula não encontrada!")
        );
    }

    public List<AulaExibicaoDto> buscarAulasPorIdProfessor(int fkProfessor) {
        List<Aula> ltAulas = this.aulaRepository.findAllByProfessorId(fkProfessor);

        return ltAulas.stream().map(AulaMapper::ofAulaExibicaoDto).toList();
    }

    public List<AulaExibicaoDto> buscarAulasAtivasPorIdProfessor(int fkProfessor) {
        List<Aula> ltAulas = this.aulaRepository.findAllByProfessorIdAndAtivaTrue(fkProfessor);

        return ltAulas.stream().map(AulaMapper::ofAulaExibicaoDto).toList();
    }

    public AulaExibicaoDto atualizarAulaPorId(int idAula, AulaAtualizacaoDto aulaAtualizacaoDto) {

        Aula aula = this.buscarPorId(idAula);

        Double novoValorAula = aulaAtualizacaoDto.getValorAula();
        aula.setValorAula(novoValorAula);

        Aula aulaAtualizada = this.aulaRepository.save(aula);

        return AulaMapper.ofAulaExibicaoDto(aulaAtualizada);
    }

    public void deletarAulaPorId(Integer id) {
        if (this.aulaRepository.existsById(id)){
            this.aulaRepository.deleteById(id);
        } else {
            throw new EntitadeNaoEncontradaException("ID de Aula Inválido. Aula não encontrada !");
        }
    }

    public void desativarAulaPorId(Integer idAula) {
        Aula aula = this.buscarPorId(idAula);
        aula.setAtiva(false);
        this.aulaRepository.save(aula);
    }

    public void ativarAulaPorId(Integer idAula) {
        Aula aula = this.buscarPorId(idAula);
        aula.setAtiva(true);
        this.aulaRepository.save(aula);
    }

    public Boolean aulaExiste(int idProfessor, int idInstrumento) {
        return this.aulaRepository.existsByProfessorIdAndInstrumentoId(idProfessor, idInstrumento);
    }


}
