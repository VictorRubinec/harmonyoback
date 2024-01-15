package school.sptech.harmonyospringapi.service.aula.dto;

import school.sptech.harmonyospringapi.domain.*;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoExibicaoDto;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoMapper;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioMapper;

public class AulaMapper {

    public static Aula of(AulaCriacaoDto aulaCriacaoDto, Professor professor, Instrumento instrumento) {

        Aula aula = new Aula();
        aula.setValorAula(aulaCriacaoDto.getValorAula());
        aula.setProfessor(professor);
        aula.setInstrumento(instrumento);
        aula.setAtiva(true);

        return aula;
    }

    public static AulaExibicaoDto ofAulaExibicaoDto(Aula aula) {
        AulaExibicaoDto aulaExibicaoDto = new AulaExibicaoDto();

        aulaExibicaoDto.setId(aula.getId());
        aulaExibicaoDto.setValorAula(aula.getValorAula());
        aulaExibicaoDto.setInstrumento(InstrumentoMapper.ofInstrumentoExibicao(aula.getInstrumento()));
        aulaExibicaoDto.setAtiva(aula.isAtiva());

        return aulaExibicaoDto;
    }

    public static InstrumentoExibicaoDto ofInstrumentoExibicao(AulaExibicaoDto aulaExibicaoDto) {
        return aulaExibicaoDto.getInstrumento();
    }

}
