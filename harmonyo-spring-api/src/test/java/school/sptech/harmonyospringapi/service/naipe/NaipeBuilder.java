package school.sptech.harmonyospringapi.service.naipe;

import school.sptech.harmonyospringapi.domain.Naipe;
import school.sptech.harmonyospringapi.service.naipe.dto.NaipeCriacaoDto;

public class NaipeBuilder {

    public static Naipe criarNaipe() {
        Naipe naipe = new Naipe();
        naipe.setId(1);
        naipe.setDescricao("Cordas");
        return naipe;
    }

    public static Naipe criarNaipe(Integer id) {
        Naipe naipe = new Naipe();
        naipe.setId(id);
        naipe.setDescricao("Cordas");
        return naipe;
    }

    public static Naipe criarNaipe(Integer id, String descricao) {
        Naipe naipe = new Naipe();
        naipe.setId(id);
        naipe.setDescricao(descricao);
        return naipe;
    }

    public static NaipeCriacaoDto criarNaipeCriacaoDto() {
        NaipeCriacaoDto naipeCriacaoDto = new NaipeCriacaoDto();
        naipeCriacaoDto.setDescricaoNaipe("Cordas");
        return naipeCriacaoDto;
    }

    public static NaipeCriacaoDto criarNaipeCriacaoDto(String descricao) {
        NaipeCriacaoDto naipeCriacaoDto = new NaipeCriacaoDto();
        naipeCriacaoDto.setDescricaoNaipe(descricao);
        return naipeCriacaoDto;
    }
}
