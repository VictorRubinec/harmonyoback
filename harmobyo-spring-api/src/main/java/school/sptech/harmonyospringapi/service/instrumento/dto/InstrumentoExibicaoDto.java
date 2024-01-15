package school.sptech.harmonyospringapi.service.instrumento.dto;

import school.sptech.harmonyospringapi.service.naipe.dto.NaipeExibicaoDto;

public class InstrumentoExibicaoDto {

    private Integer id;

    private String nome;

    private NaipeExibicaoDto naipe;


    public InstrumentoExibicaoDto() {
    }

    public InstrumentoExibicaoDto(Integer id, String nome, NaipeExibicaoDto naipe) {
        this.id = id;
        this.nome = nome;
        this.naipe = naipe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public NaipeExibicaoDto getNaipe() {
        return naipe;
    }

    public void setNaipe(NaipeExibicaoDto naipe) {
        this.naipe = naipe;
    }

    @Override
    public String toString() {
        return "InstrumentoExibicaoDto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", naipe=" + naipe +
                '}';
    }
}
