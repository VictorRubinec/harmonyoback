package school.sptech.harmonyospringapi.service.instrumento.dto;

import school.sptech.harmonyospringapi.domain.Instrumento;
import school.sptech.harmonyospringapi.domain.Naipe;
import school.sptech.harmonyospringapi.service.naipe.dto.NaipeMapper;

public class InstrumentoMapper {
    public static Instrumento of(InstrumentoCriacaoDto instrumentoCriacaoDto, Naipe naipe) {
        Instrumento instrumento = new Instrumento();

        instrumento.setNome(instrumentoCriacaoDto.getNome());
        instrumento.setNaipe(naipe);

        return instrumento;
    }

    public static InstrumentoExibicaoDto ofInstrumentoExibicao(Instrumento instrumento) {
        InstrumentoExibicaoDto instrumentoExibicaoDto = new InstrumentoExibicaoDto();

        instrumentoExibicaoDto.setId(instrumento.getId());
        instrumentoExibicaoDto.setNome(instrumento.getNome());
        instrumentoExibicaoDto.setNaipe(NaipeMapper.ofNaipeExibicao(instrumento.getNaipe()));

        return instrumentoExibicaoDto;
    }
}
