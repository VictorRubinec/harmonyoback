package school.sptech.harmonyospringapi.domain;

public class FiltroMinimoMaximo {
 
    private Double precoMinimo;
    private Double precoMaximo;

    private Double distanciaMinima;
    private Double distanciaMaxima;

    public Double getPrecoMinimo() {
        return precoMinimo;
    }

    public void setPrecoMinimo(Double precoMinimo) {
        this.precoMinimo = precoMinimo;
    }

    public Double getPrecoMaximo() {
        return precoMaximo;
    }

    public void setPrecoMaximo(Double precoMaximo) {
        this.precoMaximo = precoMaximo;
    }

    public Double getDistanciaMinima() {
        return distanciaMinima;
    }

    public void setDistanciaMinima(Double distanciaMinima) {
        this.distanciaMinima = distanciaMinima;
    }

    public Double getDistanciaMaxima() {
        return distanciaMaxima;
    }

    public void setDistanciaMaxima(Double distanciaMaxima) {
        this.distanciaMaxima = distanciaMaxima;
    }
}
