package school.sptech.harmonyospringapi.service.aula.dto;

import java.util.Objects;

public class AulaGraficoInformacoesDashboardDto {

    private int aulasRecusadas;

    private int aulasCanceladas;

    private int aulasRealizadas;

    public AulaGraficoInformacoesDashboardDto(Long aulasRecusadas, Long aulasCanceladas, Long aulasRealizadas) {
        if (Objects.isNull(aulasRecusadas)) this.aulasRecusadas = 0;
        else this.aulasRecusadas = aulasRecusadas.intValue();

        if (Objects.isNull(aulasCanceladas)) this.aulasCanceladas = 0;
        else this.aulasCanceladas = aulasCanceladas.intValue();

        if (Objects.isNull(aulasRealizadas)) this.aulasRealizadas = 0;
        else this.aulasRealizadas = aulasRealizadas.intValue();
    }

    public AulaGraficoInformacoesDashboardDto() {
    }

    public int getAulasRecusadas() {
        return aulasRecusadas;
    }

    public void setAulasRecusadas(int aulasRecusadas) {
        this.aulasRecusadas = aulasRecusadas;
    }

    public int getAulasCanceladas() {
        return aulasCanceladas;
    }

    public void setAulasCanceladas(int aulasCanceladas) {
        this.aulasCanceladas = aulasCanceladas;
    }

    public int getAulasRealizadas() {
        return aulasRealizadas;
    }

    public void setAulasRealizadas(int aulasRealizadas) {
        this.aulasRealizadas = aulasRealizadas;
    }
}
