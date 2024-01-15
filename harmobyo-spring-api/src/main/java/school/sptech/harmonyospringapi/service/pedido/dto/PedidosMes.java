package school.sptech.harmonyospringapi.service.pedido.dto;

public class PedidosMes {

    private String mes;
    private Long aulasCanceladas;
    private Long aulasRecusadas;
    private Long aulasConcluidas;

    public PedidosMes(String mes, Long aulasCanceladas, Long aulasRecusadas, Long aulasConcluidas) {
        this.mes = PedidoHistoricoDto.traduzirNomeMes(mes);
        this.aulasCanceladas = aulasCanceladas;
        this.aulasRecusadas = aulasRecusadas;
        this.aulasConcluidas = aulasConcluidas;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Long getAulasCanceladas() {
        return aulasCanceladas;
    }

    public void setAulasCanceladas(Long aulasCanceladas) {
        this.aulasCanceladas = aulasCanceladas;
    }

    public Long getAulasRecusadas() {
        return aulasRecusadas;
    }

    public void setAulasRecusadas(Long aulasRecusadas) {
        this.aulasRecusadas = aulasRecusadas;
    }

    public Long getAulasConcluidas() {
        return aulasConcluidas;
    }

    public void setAulasConcluidas(Long aulasConcluidas) {
        this.aulasConcluidas = aulasConcluidas;
    }
}
