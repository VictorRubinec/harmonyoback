package school.sptech.harmonyospringapi.service.endereco.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EnderecoAtualizacaoDto {

    @Size(min = 3)
    @NotBlank
    private String logradouro;

    @Size(min = 1)
    @NotBlank
    private String numero;

    @NotBlank
    private String complemento;

    @NotBlank
    @Size(min = 3)
    private String cidade;

    @NotBlank
    @Size(min = 3)
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
