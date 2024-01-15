package school.sptech.harmonyospringapi.service.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UsuarioAtulizarDadosPessoaisDto {

    @Schema(description = "Nome do usuário", example = "John Doe")
    @Size(min = 4)
    private String nome;

    @Schema(description = "E-mail do usuário", example = "john_doe@email.com")
    @Email
    private String email;

    @Past
    private LocalDate dataNasc;

    @Schema(description = "Sexo do usuário", example = "Homem")
    private String sexo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(LocalDate dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
