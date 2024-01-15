package school.sptech.harmonyospringapi.service.instrumento;

import school.sptech.harmonyospringapi.domain.Instrumento;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoCriacaoDto;
import school.sptech.harmonyospringapi.service.naipe.NaipeBuilder;

public class InstrumentoBuilder {

    public static Instrumento criarInstrumento() {
        Instrumento instrumento = new Instrumento();
        instrumento.setId(1);
        instrumento.setNome("Violão");
        instrumento.setNaipe(NaipeBuilder.criarNaipe(1));
        return instrumento;
    }

    public static Instrumento criarInstrumento(Integer id) {
        Instrumento instrumento = new Instrumento();
        instrumento.setId(id);
        instrumento.setNome("Violão");
        instrumento.setNaipe(NaipeBuilder.criarNaipe(1));
        return instrumento;
    }

    public static Instrumento criarInstrumento(Integer id, String nome) {
        Instrumento instrumento = new Instrumento();
        instrumento.setId(id);
        instrumento.setNome(nome);
        instrumento.setNaipe(NaipeBuilder.criarNaipe(1));
        return instrumento;
    }

    public static InstrumentoCriacaoDto criarInstrumentoCriacaoDto() {
        InstrumentoCriacaoDto instrumentoCriacaoDto = new InstrumentoCriacaoDto();
        instrumentoCriacaoDto.setNome("Violão");
        instrumentoCriacaoDto.setNaipe(1);
        return instrumentoCriacaoDto;
    }

    public static InstrumentoCriacaoDto criarInstrumentoCriacaoDto(Integer naipeId) {
        InstrumentoCriacaoDto instrumentoCriacaoDto = new InstrumentoCriacaoDto();
        instrumentoCriacaoDto.setNome("Violão");
        instrumentoCriacaoDto.setNaipe(naipeId);
        return instrumentoCriacaoDto;
    }

public static InstrumentoCriacaoDto criarInstrumentoCriacaoDto(Integer naipeId, String nome) {
        InstrumentoCriacaoDto instrumentoCriacaoDto = new InstrumentoCriacaoDto();
        instrumentoCriacaoDto.setNome(nome);
        instrumentoCriacaoDto.setNaipe(naipeId);
        return instrumentoCriacaoDto;
    }


}
