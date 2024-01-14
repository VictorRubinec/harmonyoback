package school.sptech.harmonyospringapi.service.pedido.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import school.sptech.harmonyospringapi.domain.*;

import java.lang.Integer;
import java.time.LocalDateTime;

public class PedidoCriacaoDto {

    @NotNull
    @Min(1)
    private Integer alunoId;

    @NotNull
    @Min(1)
    private Integer professorId;

    @NotNull
    @Min(1)
    private Integer aulaId;

    @FutureOrPresent
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataAula;

    private double valorAula;


    public Integer getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Integer alunoId) {
        this.alunoId = alunoId;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public LocalDateTime getDataAula() {
        return dataAula;
    }

    public void setDataAula(LocalDateTime dataAula) {
        this.dataAula = dataAula;
    }

    public Integer getAulaId() {
        return aulaId;
    }

    public void setAulaId(Integer aulaId) {
        this.aulaId = aulaId;
    }

    public double getValorAula() {
        return valorAula;
    }

    public void setValorAula(double valorAula) {
        this.valorAula = valorAula;
    }
}
