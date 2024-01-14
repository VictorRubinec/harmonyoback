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
import school.sptech.harmonyospringapi.domain.Aluno;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoExibicaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.aluno_instrumento.AlunoInstrumentoCriacaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.aluno_instrumento.AlunoInstrumentoExibicaoDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoPilhaDto;
import school.sptech.harmonyospringapi.service.usuario.AlunoService;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioCriacaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioExibicaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.professor.ProfessorExibicaoResumidoDto;
import school.sptech.harmonyospringapi.utils.PilhaObj;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@Tag(name = "Alunos")
public class AlunoController{

    @Autowired
    private AlunoService alunoService;

    @Operation(summary = "Cadastra um aluno", description = "")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description = "Aluno cadastrado."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "Já existe um aluno com este cpf ou email.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioExibicaoDto> cadastrar(@RequestBody @Valid UsuarioCriacaoDto usuarioCriacaoDto){

        UsuarioExibicaoDto alunoCadastrado = this.alunoService.cadastrar(usuarioCriacaoDto);

        return ResponseEntity.status(201).body(alunoCadastrado);
    }

    @Operation(summary = "Obtém uma lista de todos os alunos cadastrados", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alunos encontrados."),
            @ApiResponse(responseCode = "204", description = "Não há alunos cadastrados.", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    public ResponseEntity<List<UsuarioExibicaoDto>> listarAluno(){

        List<UsuarioExibicaoDto> ltUsuariosExibicao = this.alunoService.listar();

        if (ltUsuariosExibicao.isEmpty()){

            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(ltUsuariosExibicao);
    }

    @Operation(summary = "Obtém um aluno pelo seu id", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno encontrado."),
            @ApiResponse(responseCode = "400", description = "ID inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado.")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioExibicaoDto> buscarPorId(@RequestParam Integer id){
        return ResponseEntity.status(200).body(this.alunoService.buscarPorIdParaExibicao(id));
    }

    @Operation(summary = "Obtém um aluno pelo seu nome", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno encontrado."),
            @ApiResponse(responseCode = "400", description = "Nome inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado.")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/nome")
    public ResponseEntity<UsuarioExibicaoDto> buscarPorNome(@RequestParam String nome){
        UsuarioExibicaoDto alunoEncontrado = this.alunoService.buscarPorNome(nome);

        return ResponseEntity.status(200).body(alunoEncontrado);
    }

    @Operation(summary = "Obtém uma lista de alunos ordenada alfabéticamente", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alunos ordenados por ordem alfabética encontrados."),
            @ApiResponse(responseCode = "204", description = "Não há alunos cadastrados.")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/ordem-alfabetica")
    public ResponseEntity<List<UsuarioExibicaoDto>> obterTodosEmOrdemAlfabetica(){

        List<UsuarioExibicaoDto> ltUsuariosExibicao = this.alunoService.obterTodosEmOrdemAlfabetica();

        if (ltUsuariosExibicao.isEmpty()){

            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(ltUsuariosExibicao);
    }

    @Operation(summary = "Deleta um aluno através do seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno deletado do sistema"),
            @ApiResponse(responseCode = "400", description = "ID Inválido. Aluno não encontrado"),
            @ApiResponse(responseCode = "404", description = "ID Inválido. Aluno não encontrado")
    })
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/exclusao/conta/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Integer id){

        this.alunoService.deletarPorId(id);

        return ResponseEntity.status(200).build();
    }

    @Operation(summary = "Obtém uma lista de todos os instrumentos que o aluno possui nível", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instrumentos com esse Id encontrados."),
            @ApiResponse(responseCode = "204", description = "Não há instrumentos com esse Id de aluno.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/instrumentos")
    public ResponseEntity<List<InstrumentoExibicaoDto>> listarInstrumentosPorIdAluno(@PathVariable int id) {
        List<InstrumentoExibicaoDto> instrumentos = this.alunoService.listarInstrumentos(id);

        return instrumentos.isEmpty() ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(instrumentos);
    }

    @Operation(summary = "Cadastra um instrumento para o aluno", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Instrumento cadastrado no aluno."),
            @ApiResponse(responseCode = "400", description = "ID inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado."),
            @ApiResponse(responseCode = "409", description = "Instrumento já cadastrado para o aluno.")
    })
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/{id}/instrumentos")
    public ResponseEntity<AlunoInstrumentoExibicaoDto> cadastrarInstrumento(@PathVariable int id, @RequestBody @Valid AlunoInstrumentoCriacaoDto alunoInstrumentoCriacaoDto) {
        AlunoInstrumentoExibicaoDto alunoInstrumentoExibicaoDto = this.alunoService.adicionarInstrumento(id, alunoInstrumentoCriacaoDto);

        return ResponseEntity.status(201).body(alunoInstrumentoExibicaoDto);
    }

    @Operation(summary = "Retorna o histórico de pedidos do aluno", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de pedidos do aluno encontrado."),
            @ApiResponse(responseCode = "204", description = "Não há histórico de pedidos para esse aluno.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/getHistoricoPilha")
    public ResponseEntity<PilhaObj<PedidoExibicaoPilhaDto>> getPilha(){
        Aluno aluno = new Aluno();
        return ResponseEntity.status(200).body(aluno.getHistorico());
    }

    @GetMapping("/quantidade-cadastrados-semana")
    public ResponseEntity<List<Integer>> obterQuantidadeCadastrados(){
        return ResponseEntity.status(200).body(this.alunoService.obterQuantidadeCadastradosSemana());
    }
}
