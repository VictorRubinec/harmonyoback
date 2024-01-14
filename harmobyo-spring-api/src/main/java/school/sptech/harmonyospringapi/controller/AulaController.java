package school.sptech.harmonyospringapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.harmonyospringapi.domain.Aula;
import school.sptech.harmonyospringapi.service.aula.AulaService;
import school.sptech.harmonyospringapi.service.aula.dto.AulaAtualizacaoDto;
import school.sptech.harmonyospringapi.service.aula.dto.AulaCriacaoDto;
import school.sptech.harmonyospringapi.service.aula.dto.AulaExibicaoDto;
import school.sptech.harmonyospringapi.utils.ArvoreBin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/aulas")
@Tag(name = "Aulas")
public class AulaController {

    @Autowired
    private AulaService aulaService;



    @Operation(summary = "Cadastra a aula de um professor ", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aula Cadastrada com sucesso"),
            @ApiResponse(responseCode = "409", description = "Aula já cadastrada para este professor e para este instrumento !", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "ID do Professor inválido e/ou ID do instrumento inválido !", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<AulaExibicaoDto> cadastrar(@RequestBody @Valid AulaCriacaoDto aulaCriacaoDto) {
        AulaExibicaoDto aulaCriada = this.aulaService.cadastrarAula(aulaCriacaoDto);

        return ResponseEntity.created(null).body(aulaCriada);
    }

    @Operation(summary = "Obtém uma lista de todos as aulas cadastradas de um professor ", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aulas encontrados."),
            @ApiResponse(responseCode = "204", description = "Este professor não possui aulas cadastradas.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "ID do Professor inválido !", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{fkProfessor}")
    public ResponseEntity<List<AulaExibicaoDto>> buscarAulasPorIdProfessor(@PathVariable int fkProfessor) {

        List<AulaExibicaoDto> ltAulas = this.aulaService.buscarAulasPorIdProfessor(fkProfessor);

        if (ltAulas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ltAulas);
    }

    @Operation(summary = "Obtém uma lista de todos as aulas cadastradas e ativas de um professor", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aulas encontrados."),
            @ApiResponse(responseCode = "204", description = "Este professor não possui aulas cadastradas e ativas.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "ID do Professor inválido !", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/ativas/{fkProfessor}")
    public ResponseEntity<List<AulaExibicaoDto>> buscarAulasAtivasPorIdProfessor(@PathVariable int fkProfessor) {

        List<AulaExibicaoDto> ltAulas = this.aulaService.buscarAulasAtivasPorIdProfessor(fkProfessor);

        if (ltAulas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ltAulas);
    }

    @PutMapping("/desativar/{idAula}")
    public ResponseEntity<AulaExibicaoDto> desativarAulaPorId(@PathVariable int idAula) {

        this.aulaService.desativarAulaPorId(idAula);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/ativar/{idAula}")
    public ResponseEntity<AulaExibicaoDto> ativarAulaPorId(@PathVariable int idAula) {

        this.aulaService.ativarAulaPorId(idAula);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Atualiza uma Aula pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aula atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "O valor da aula não foi digitado corretamente.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "ID da Aula inválido !", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{idAula}")
    public ResponseEntity<AulaExibicaoDto> atualizarAulaPorId(@PathVariable int idAula, @RequestBody @Valid AulaAtualizacaoDto aulaAtualizacaoDto) {

        AulaExibicaoDto aulaAtualizada = this.aulaService.atualizarAulaPorId(idAula, aulaAtualizacaoDto);

        return ResponseEntity.ok(aulaAtualizada);
    }

    @Operation(summary = "Deleta uma aula através do seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aula deletada com sucesso do sistema"),
            @ApiResponse(responseCode = "404", description = "ID Inválido. Aula não encontrada")
    })
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{idAula}")
    public ResponseEntity<Void> deletarAulaPorId(@PathVariable int idAula) {

        this.aulaService.deletarAulaPorId(idAula);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtém uma lista de todas as aulas cadastradas", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aulas encontradas."),
            @ApiResponse(responseCode = "204", description = "Não há aulas cadastradas.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<AulaExibicaoDto>> listar() {
        List<AulaExibicaoDto> ltAulas = this.aulaService.obterTodos();

        if (ltAulas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ltAulas);
    }

    @GetMapping("/quantidade")
    public ResponseEntity<Integer> obterQuantidadeAulasCadastradas() {
        return ResponseEntity.ok(this.aulaService.obterQuantidadeAulasCadastradas());
    }

    @GetMapping("/quantidade-mes")
    public ResponseEntity<Integer> obterQuantidadeAulasCadastradasNesseMes() {
        return ResponseEntity.ok(this.aulaService.obterQuantidadeAulasCadastradasNesseMes());
    }

    @GetMapping("quantidade-por-aluno")
    public ResponseEntity<Double> quantidadeUsuariosPorAluno() {
        return ResponseEntity.ok(this.aulaService.quantidadeUsuariosPorAluno());
    }

    @GetMapping("quantidade-por-aluno-mes")
    public ResponseEntity<Double> quantidadeUsuariosPorAlunoNesseMes() {
        return ResponseEntity.ok(this.aulaService.quantidadeUsuariosPorAlunoNesseMes());
    }



}



