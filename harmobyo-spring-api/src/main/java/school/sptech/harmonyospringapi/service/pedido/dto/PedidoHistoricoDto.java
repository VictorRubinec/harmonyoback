package school.sptech.harmonyospringapi.service.pedido.dto;

public class PedidoHistoricoDto {

    private String mes;
    private Integer qtdAulasSolicitadas;
    private Integer qtdAulasRealizadas;

    public PedidoHistoricoDto(String mes, Long qtdAulasSolicitadas, Long qtdAulasRealizadas) {
        this.mes = traduzirNomeMes(mes);
        this.qtdAulasSolicitadas = qtdAulasSolicitadas.intValue();
        this.qtdAulasRealizadas = qtdAulasRealizadas.intValue();
    }
    public static String traduzirNomeMes(String mes){
        switch (mes){
            case "January":
                return "Janeiro";
            case "February":
                return "Fevereiro";
            case "March":
                return "Março";
            case "April":
                return "Abril";
            case "May":
                return "Maio";
            case "June":
                return "Junho";
            case "July":
                return "Julho";
            case "August":
                return "Agosto";
            case "September":
                return "Setembro";
            case "October":
                return "Outubro";
            case "November":
                return "Novembro";
            case "December":
                return "Dezembro";
            default:
                return mes;
        }

    }
    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Integer getQtdAulasSolicitadas() {
        return qtdAulasSolicitadas;
    }

    public void setQtdAulasSolicitadas(Integer qtdAulasSolicitadas) {
        this.qtdAulasSolicitadas = qtdAulasSolicitadas;
    }

    public Integer getQtdAulasRealizadas() {
        return qtdAulasRealizadas;
    }

    public void setQtdAulasRealizadas(Integer qtdAulasRealizadas) {
        this.qtdAulasRealizadas = qtdAulasRealizadas;
    }
}
