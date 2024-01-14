package school.sptech.harmonyospringapi.service.naipe.dto;

import school.sptech.harmonyospringapi.domain.Instrumento;
import school.sptech.harmonyospringapi.domain.Naipe;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoMapper;

import java.util.ArrayList;

public class NaipeMapper {

    public static Naipe of(NaipeCriacaoDto naipeCriacaoDto) {
        Naipe naipe = new Naipe();

        naipe.setDescricao(naipeCriacaoDto.getDescricaoNaipe());

        return naipe;
    }

    public static NaipeCriacaoDto of(Naipe naipe) {
        NaipeCriacaoDto naipeCriado = new NaipeCriacaoDto();
        naipeCriado.setDescricaoNaipe(naipe.getDescricao());

        return naipeCriado;
    }

    public static NaipeExibicaoDto ofNaipeExibicao(Naipe naipe) {
        NaipeExibicaoDto naipeExibicaoDto = new NaipeExibicaoDto();

        naipeExibicaoDto.setId(naipe.getId());
        naipeExibicaoDto.setDescricaoNaipe(naipe.getDescricao());

        return naipeExibicaoDto;
    }
}
