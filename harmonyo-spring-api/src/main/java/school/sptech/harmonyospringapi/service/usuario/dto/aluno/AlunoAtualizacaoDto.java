package school.sptech.harmonyospringapi.service.usuario.dto.aluno;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import school.sptech.harmonyospringapi.domain.Endereco;

import java.time.LocalDate;

public class AlunoAtualizacaoDto {


    @Schema(description = "Nome do usuário", example = "John Doe")
    @Size(min = 4)
    private String nome;

    @Schema(description = "E-mail do usuário", example = "john_doe@email.com")
    @Email
    private String email;

    @Schema(description = "Sexo do usuário", example = "Homem")
    private String sexo;

    @Schema(description = "Senha do usuário", example = "123456")
    @Size(min = 3)
    private String senha;

    @NotBlank
    private Endereco endereco;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser anterior a data atual")
    private LocalDate dataNasc;

    @Pattern(
            regexp = "(\\(?\\d{2}\\)?\\s)?(\\d{4,5}\\-\\d{4})",
            message = "Indique um telefone válido"
    )
    private String telefone;

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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public LocalDate getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(LocalDate dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
