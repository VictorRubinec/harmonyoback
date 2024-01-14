package school.sptech.harmonyospringapi.service.usuario.dto.aluno_instrumento;

import school.sptech.harmonyospringapi.domain.*;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoMapper;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioMapper;

public class AlunoInstrumentoMapper {

    public static AlunoInstrumentoExibicaoDto ofAlunoInstrumentoExibicao(AlunoInstrumento alunoInstrumento) {
        AlunoInstrumentoExibicaoDto alunoInstrumentoExibicaoDto = new AlunoInstrumentoExibicaoDto();

        alunoInstrumentoExibicaoDto.setId(alunoInstrumento.getId());
        alunoInstrumentoExibicaoDto
                .setAluno(UsuarioMapper.ofUsuarioExibicao(alunoInstrumento.getAluno()));
        alunoInstrumentoExibicaoDto
                .setInstrumento(InstrumentoMapper.ofInstrumentoExibicao(alunoInstrumento.getInstrumento()));
        alunoInstrumentoExibicaoDto.setNivelConhecimento(alunoInstrumento.getNivelConhecimento());

        return alunoInstrumentoExibicaoDto;
    }

    public static AlunoInstrumento of(AlunoInstrumentoCriacaoDto alunoInstrumentoCriacaoDto,
                                      Aluno aluno,
                                      Instrumento instrumento) {

        AlunoInstrumento alunoInstrumento = new AlunoInstrumento();
        alunoInstrumento.setAluno(aluno);
        alunoInstrumento.setInstrumento(instrumento);
        alunoInstrumento.setNivelConhecimento(alunoInstrumentoCriacaoDto.getNivelConhecimento());

        return alunoInstrumento;
    }
}
