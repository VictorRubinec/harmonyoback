package school.sptech.harmonyospringapi.service.experiencia.dto;

import school.sptech.harmonyospringapi.domain.Experiencia;
import school.sptech.harmonyospringapi.domain.Professor;

public abstract class ExperienciaMapper {

    public static ExperienciaExibicaoDto of(Experiencia experiencia){

        ExperienciaExibicaoDto experienciaResumidaDto = new ExperienciaExibicaoDto();

        experienciaResumidaDto.setId(experiencia.getId());
        experienciaResumidaDto.setTitulo(experiencia.getTitulo());
        experienciaResumidaDto.setDescricao(experiencia.getDescricao());

        return experienciaResumidaDto;
    }

    public static Experiencia of(ExperienciaCriacaoDto experienciaCriacaoDto, Professor professor){

        Experiencia novaExperiencia = new Experiencia();

        novaExperiencia.setTitulo(experienciaCriacaoDto.getTitulo());
        novaExperiencia.setDescricao(experienciaCriacaoDto.getDescricao());
        novaExperiencia.setProfessor(professor);

        return novaExperiencia;
    }
}
