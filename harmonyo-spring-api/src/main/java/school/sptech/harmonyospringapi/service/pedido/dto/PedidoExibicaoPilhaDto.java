package school.sptech.harmonyospringapi.service.pedido.dto;

import school.sptech.harmonyospringapi.domain.Pedido;

import java.time.LocalDateTime;

public class PedidoExibicaoPilhaDto {

    private Integer id;
    private LocalDateTime dataAula;

    private String instrumentoAula;

    private String professorAula;

    public PedidoExibicaoPilhaDto(Pedido p){
        this.id = p.getId();
        this.dataAula = p.getDataAula();
        this.instrumentoAula = p.getAula().getInstrumento().getNome();
        this.professorAula = p.getAula().getProfessor().getNome();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataAula() {
        return dataAula;
    }

    public void setDataAula(LocalDateTime dataAula) {
        this.dataAula = dataAula;
    }

    public String getInstrumentoAula() {
        return instrumentoAula;
    }

    public void setInstrumentoAula(String instrumentoAula) {
        this.instrumentoAula = instrumentoAula;
    }

    public String getProfessorAula() {
        return professorAula;
    }

    public void setProfessorAula(String professorAula) {
        this.professorAula = professorAula;
    }

    @Override
    public String toString() {
        return "PedidoExibicaoPilhaDto{" +
                "id=" + id +
                ", dataAula=" + dataAula +
                ", instrumentoAula='" + instrumentoAula + '\'' +
                ", professorAula='" + professorAula + '\'' +
                '}';
    }
}
