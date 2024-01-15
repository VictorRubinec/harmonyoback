package school.sptech.harmonyospringapi.service.socket;

import java.security.Principal;

public class WebSocketMessage {

    private Integer idUsuario;

    private int pagina;


    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getPagina() {
        return pagina;
    }
}
