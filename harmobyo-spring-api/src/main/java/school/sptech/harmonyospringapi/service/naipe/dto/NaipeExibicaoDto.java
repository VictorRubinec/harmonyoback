package school.sptech.harmonyospringapi.service.naipe.dto;

import java.util.List;

public class NaipeExibicaoDto {

    private Integer id;

    private String descricaoNaipe;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricaoNaipe() {
        return descricaoNaipe;
    }

    public void setDescricaoNaipe(String descricaoNaipe) {
        this.descricaoNaipe = descricaoNaipe;
    }

    @Override
    public String toString() {
        return "NaipeExibicaoDto{" +
                "id=" + id +
                ", descricaoNaipe='" + descricaoNaipe + '\'' +
                '}';
    }
}
