package school.sptech.harmonyospringapi.service.pedido.dto;

public class PedidoExibicaoDashboardDto {

    private Integer idInstrumento;
    private String nomeInstrumento;
    private Integer quantidadeAulas;
    private Double rendimentoTotal;

    public PedidoExibicaoDashboardDto(Integer idInstrumento, String nomeInstrumento, Long quantidadeAulas, Double rendimentoTotal) {
        this.idInstrumento = idInstrumento;
        this.nomeInstrumento = nomeInstrumento;
        this.quantidadeAulas = quantidadeAulas.intValue();
        this.rendimentoTotal = rendimentoTotal;
    }

    public Integer getIdInstrumento() {
        return idInstrumento;
    }

    public void setIdInstrumento(Integer idInstrumento) {
        this.idInstrumento = idInstrumento;
    }

    public String getNomeInstrumento() {
        return nomeInstrumento;
    }

    public void setNomeInstrumento(String nomeInstrumento) {
        this.nomeInstrumento = nomeInstrumento;
    }

    public Integer getQuantidadeAulas() {
        return quantidadeAulas;
    }

    public void setQuantidadeAulas(Integer quantidadeAulas) {
        this.quantidadeAulas = quantidadeAulas;
    }

    public Double getRendimentoTotal() {
        return rendimentoTotal;
    }

    public void setRendimentoTotal(Double rendimentoTotal) {
        this.rendimentoTotal = rendimentoTotal;
    }
}
