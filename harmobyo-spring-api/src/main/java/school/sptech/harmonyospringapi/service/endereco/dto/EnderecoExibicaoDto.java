package school.sptech.harmonyospringapi.service.endereco.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EnderecoExibicaoDto {

    @Schema(description = "Logradouro do endereço", example = "Rua Haddock Lobo")
    private String logradouro;

    @Schema(description = "Número do endereço", example = "595")
    private String numero;

    @Schema(description = "Complemento do endereço", example = "John Doe")
    private String complemento;

    @Schema(description = "Cidade do endereço", example = "São Paulo")
    private String cidade;

    @Schema(description = "Bairro do endereço", example = "Cerqueira César")
    private String bairro;

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
}
