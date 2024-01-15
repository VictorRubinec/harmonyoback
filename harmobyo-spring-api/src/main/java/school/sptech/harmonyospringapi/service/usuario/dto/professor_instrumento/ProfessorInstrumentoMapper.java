package school.sptech.harmonyospringapi.service.usuario.dto.professor_instrumento;

import school.sptech.harmonyospringapi.domain.Instrumento;
import school.sptech.harmonyospringapi.domain.Professor;
import school.sptech.harmonyospringapi.domain.ProfessorInstrumento;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoMapper;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioMapper;

public class ProfessorInstrumentoMapper {

    public static ProfessorInstrumentoExibicaoDto ofProfessorInstrumentoExibicao(ProfessorInstrumento professorInstrumento) {
        ProfessorInstrumentoExibicaoDto professorInstrumentoExibicaoDto = new ProfessorInstrumentoExibicaoDto();

        professorInstrumentoExibicaoDto.setId(professorInstrumento.getId());
        professorInstrumentoExibicaoDto.setProfessor(UsuarioMapper.ofUsuarioExibicao(professorInstrumento.getProfessor()));
        professorInstrumentoExibicaoDto.setInstrumento(InstrumentoMapper.ofInstrumentoExibicao(professorInstrumento.getInstrumento()));
        professorInstrumentoExibicaoDto.setNivelConhecimento(professorInstrumento.getNivelConhecimento());
        professorInstrumentoExibicaoDto.setEmprestaInstrumento(professorInstrumento.isEmprestaInstrumento());

        return professorInstrumentoExibicaoDto;
    }

    public static ProfessorInstrumento of(ProfessorInstrumentoCriacaoDto professorInstrumentoCriacaoDto, Professor professor, Instrumento instrumento) {
        ProfessorInstrumento professorInstrumento = new ProfessorInstrumento();
        professorInstrumento.setProfessor(professor);
        professorInstrumento.setInstrumento(instrumento);
        professorInstrumento.setNivelConhecimento(professorInstrumentoCriacaoDto.getNivelConhecimento());
        professorInstrumento.setEmprestaInstrumento(professorInstrumentoCriacaoDto.isEmprestaInstrumento());

        return professorInstrumento;
    }
}
