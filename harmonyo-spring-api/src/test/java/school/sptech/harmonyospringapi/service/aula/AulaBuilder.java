package school.sptech.harmonyospringapi.service.aula;

import school.sptech.harmonyospringapi.domain.Aula;
import school.sptech.harmonyospringapi.domain.Instrumento;
import school.sptech.harmonyospringapi.domain.Naipe;
import school.sptech.harmonyospringapi.domain.Professor;
import school.sptech.harmonyospringapi.service.aula.dto.AulaAtualizacaoDto;
import school.sptech.harmonyospringapi.service.aula.dto.AulaCriacaoDto;

public class AulaBuilder {

    private static final Integer ID = 1;
    private static final Double VALOR_AULA = 50.00;

    public static Aula criarAula() {
        Naipe naipe = new Naipe();
        naipe.setId(ID);

        Instrumento instrumento = new Instrumento();
        instrumento.setId(ID);
        instrumento.setNaipe(naipe);

        Professor professor = new Professor();
        professor.setId(ID);

        Aula aula = new Aula();
        aula.setId(ID);
        aula.setValorAula(VALOR_AULA);
        aula.setProfessor(professor);
        aula.setInstrumento(instrumento);

        return aula;
    }

    public static Aula criarAula(Integer id) {
        Naipe naipe = new Naipe();
        naipe.setId(id);

        Instrumento instrumento = new Instrumento();
        instrumento.setId(id);
        instrumento.setNaipe(naipe);

        Professor professor = new Professor();
        professor.setId(id);

        Aula aula = new Aula();
        aula.setId(id);
        aula.setValorAula(VALOR_AULA);
        aula.setProfessor(professor);
        aula.setInstrumento(instrumento);

        return aula;
    }

    public static Aula criarAula(Integer id, Double valorAula) {
        Naipe naipe = new Naipe();
        naipe.setId(id);

        Instrumento instrumento = new Instrumento();
        instrumento.setId(id);
        instrumento.setNaipe(naipe);

        Professor professor = new Professor();
        professor.setId(id);

        Aula aula = new Aula();
        aula.setId(id);
        aula.setValorAula(valorAula);
        aula.setProfessor(professor);
        aula.setInstrumento(instrumento);

        return aula;
    }

    public static AulaCriacaoDto criarAulaCriacao() {
        AulaCriacaoDto aulaCriacaoDto = new AulaCriacaoDto();
        aulaCriacaoDto.setValorAula(VALOR_AULA);
        aulaCriacaoDto.setProfessorId(ID);
        aulaCriacaoDto.setInstrumentoId(ID);
        return aulaCriacaoDto;
    }

    public static AulaCriacaoDto criarAulaCriacao(Integer id) {
        AulaCriacaoDto aulaCriacaoDto = new AulaCriacaoDto();
        aulaCriacaoDto.setValorAula(VALOR_AULA);
        aulaCriacaoDto.setProfessorId(id);
        aulaCriacaoDto.setInstrumentoId(id);
        return aulaCriacaoDto;
    }

    public static AulaCriacaoDto criarAulaCriacao(Integer id, Double valorAula) {
        AulaCriacaoDto aulaCriacaoDto = new AulaCriacaoDto();
        aulaCriacaoDto.setValorAula(valorAula);
        aulaCriacaoDto.setProfessorId(id);
        aulaCriacaoDto.setInstrumentoId(id);
        return aulaCriacaoDto;
    }

    public static AulaAtualizacaoDto criarAulaAtualizacao() {
        AulaAtualizacaoDto aulaAtualizacaoDto = new AulaAtualizacaoDto();
        aulaAtualizacaoDto.setValorAula(VALOR_AULA);
        return aulaAtualizacaoDto;
    }

    public static AulaAtualizacaoDto criarAulaAtualizacao(Double valorAula) {
        AulaAtualizacaoDto aulaAtualizacaoDto = new AulaAtualizacaoDto();
        aulaAtualizacaoDto.setValorAula(valorAula);
        return aulaAtualizacaoDto;
    }


    public static Integer getId() {
        return ID;
    }

    public static Double getValorAula() {
        return VALOR_AULA;
    }
}
