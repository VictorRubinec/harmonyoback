package school.sptech.harmonyospringapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import school.sptech.harmonyospringapi.domain.Pedido;
import school.sptech.harmonyospringapi.domain.fila.FilaEspera;
import school.sptech.harmonyospringapi.service.pedido.PedidoService;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoAlteracaoStatus;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoCriacaoDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDto;
import school.sptech.harmonyospringapi.service.pedido.fila.FilaEsperaService;
import school.sptech.harmonyospringapi.service.pedido.hashing.HashTableService;
import school.sptech.harmonyospringapi.service.socket.WebSocketMessage;
import school.sptech.harmonyospringapi.service.socket.WebSocketService;
import school.sptech.harmonyospringapi.utils.PilhaObj;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private HashTableService hashTableService;

    @Lazy
    @Autowired
    private FilaEsperaService filaService;

    @Autowired
    private WebSocketService webSocketService;

    @GetMapping
    @Operation(summary = "Lista todos os pedidos cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum pedido encontrado")
    })
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PedidoExibicaoDto>> listarPedidos(){
        List<PedidoExibicaoDto> ltPedidosExibicao = this.pedidoService.obterTodos();

        if(ltPedidosExibicao.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(ltPedidosExibicao);
    }

    @GetMapping("/pedidos-pendentes/{idProfessor}")
    @Operation(summary = "Lista todos os pedidos pendentes de um professor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum pedido encontrado"),
            @ApiResponse(responseCode =  "204", description = "Nenhum pedido pendente encontrado")
    })
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PilhaObj<PedidoExibicaoDto>> listarPedidosPendentes(@PathVariable int idProfessor){
        PilhaObj<PedidoExibicaoDto> ltPedidosExibicao = this.pedidoService.obterPedidosPendentes(idProfessor);

        if(ltPedidosExibicao.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(ltPedidosExibicao);
    }


    @PostMapping
    @Operation(summary = "Cadastra um pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PedidoExibicaoDto> adicionarPedido(@RequestBody @Valid
                                                             PedidoCriacaoDto pedidoCriacaoDto){
        PedidoExibicaoDto pedidoExibicaoDto = this.pedidoService.criar(pedidoCriacaoDto);
        hashTableService.insere(pedidoExibicaoDto);
        return ResponseEntity.created(null).body(pedidoExibicaoDto);
    }

    @PutMapping("/realiza-pagamento")
    @Operation(summary = "Realiza o pagamento do pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<PedidoExibicaoDto> realizarPagamento(@RequestBody PedidoAlteracaoStatus pedidoAlteracaoStatus){
        return ResponseEntity.ok(this.pedidoService.realizarPagamento(pedidoAlteracaoStatus));
    }

    @PutMapping("/fila/{id}")
    @Operation(summary = "Realiza o pagamento do pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Pedido> colocaEmFila(@PathVariable int id){
        Pedido pedido = this.pedidoService.buscarPorId(id);
        return ResponseEntity.ok(this.pedidoService.atualizarStatus(pedido, "Em Fila"));
    }

    @PutMapping("/aceita-pedido")
    @Operation(summary = "Aceita a proposta do aluno")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proposta aceita com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<PedidoExibicaoDto> aceitarPropostaDoAluno(@RequestBody PedidoAlteracaoStatus pedidoAlteracaoStatus){
        PedidoExibicaoDto pedido = this.pedidoService.aceitarPropostaDoAluno(pedidoAlteracaoStatus);
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/recusa-pedido")
    @Operation(summary = "Recusa a proposta do aluno")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proposta recusada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<PedidoExibicaoDto> recusarPropostaDoAluno(@RequestBody PedidoAlteracaoStatus pedidoAlteracaoStatus){
        return ResponseEntity.ok(this.pedidoService.recusarPropostaDoAluno(pedidoAlteracaoStatus));
    }

    @PutMapping("/cancela-pedido")
    @Operation(summary = "Cancela o pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PedidoExibicaoDto> cancelarPedidoPorId(@RequestBody PedidoAlteracaoStatus pedidoAlteracaoStatus){
        return ResponseEntity.ok(pedidoService.cancelarPedido(pedidoAlteracaoStatus));
    }

    @PutMapping("/conclui-pedido")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PedidoExibicaoDto> concluirPedidoPorId(@RequestBody PedidoAlteracaoStatus pedidoAlteracaoStatus){
        return ResponseEntity.ok(pedidoService.concluirPedidoPorId(pedidoAlteracaoStatus));
    }

    @GetMapping("/usuario/{id}")
    @Operation(summary = "Lista todos os pedidos cadastrados por usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pedidos por usuario"),
            @ApiResponse(responseCode = "400", description = "Usuario não encontrado")
    })
    public ResponseEntity<List<PedidoExibicaoDto>> buscarPorUsuarioId(@PathVariable Integer id){
        List<PedidoExibicaoDto> pedidoExibicaoDto = this.pedidoService.buscarPorUsuarioId(id);

        if(pedidoExibicaoDto.isEmpty()) return ResponseEntity.noContent().build();
        //if(this.filaService.isEmpty()) this.filaService.adicionarBanco();
        return ResponseEntity.ok(pedidoExibicaoDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um pedido por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoExibicaoDto> buscarPorId(@PathVariable Integer id){
        PedidoExibicaoDto pedidoExibicaoDto = this.pedidoService.buscarPorIdParaExibicao(id);

        if(pedidoExibicaoDto == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pedidoExibicaoDto);
    }

    @GetMapping("/pedidos-por-data-id-usuario")
    @Operation(summary = "Obtém uma lista de todos os pedidos confirmados de um professor pelo dia", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aulas encontradss."),
            @ApiResponse(responseCode = "204", description = "Este professor não possui aulas cadastradas.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "ID do Professor inválido !", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<PedidoExibicaoDto>> buscarAulasPorIdUsuarioEDataAulaConfirmado(@RequestParam int fkUsuario,
                                                                                    @RequestParam String data) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(data + " 00:00:00", formatter);
        List<PedidoExibicaoDto> ltAulas = this.pedidoService.buscarAulasPorIdUsuarioEDataAulaConfirmado(fkUsuario, localDateTime);

        if (ltAulas.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ltAulas);
    }

    @GetMapping("/pedidos-por-mes-id-usuario")
    @Operation(summary = "Obtém uma lista de todos os pedidos confirmados de um professor pelo dia", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aulas encontradss."),
            @ApiResponse(responseCode = "204", description = "Este professor não possui aulas cadastradas.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "ID do Professor inválido !", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<PedidoExibicaoDto>> buscarAulasPorIdUsuarioEMesAula(@RequestParam int fkUsuario,
                                                                                    @RequestParam String data) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(data + " 00:00:00", formatter);
        List<PedidoExibicaoDto> ltAulas = this.pedidoService.buscarAulasPorIdUsuarioEMesAula(fkUsuario, localDateTime);

        if (ltAulas.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ltAulas);
    }

    @GetMapping("/usuario/{id}/confirmado")
    public ResponseEntity<List<PedidoExibicaoDto>> buscarPorUsuarioIdConcluido(@PathVariable Integer id){
        List<PedidoExibicaoDto> pedidoExibicaoDto = this.pedidoService.buscarPorUsuarioIdConfirmado(id);

        if(pedidoExibicaoDto.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pedidoExibicaoDto);
    }

    // ------------------------------- HashingTable ------------------------------- //

    @GetMapping("/usuario/hashing/{id}")
    public ResponseEntity<List<PedidoExibicaoDto>> buscarPedidosPorUsuarioIdEStatusHashing(@PathVariable Integer id, @RequestParam String status){
        if (this.hashTableService.isEmpty()) this.hashTableService.adicionarBanco();

        List<PedidoExibicaoDto> pedidoExibicaoDto = this.hashTableService.buscarPedidosPorIdEStatus(id, status);

        if (pedidoExibicaoDto.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pedidoExibicaoDto);
    }

    // ------------------------------- FilaEspera ------------------------------- //

    @PostMapping("/fila-espera/{id}")
    public ResponseEntity<PedidoExibicaoDto> adicionarPedidoFilaEspera(@PathVariable int id, @RequestParam String data){
        return ResponseEntity.ok(this.filaService.adicionarPedidoFilaEspera(id, data));
    }

    @PutMapping("/fila-espera/{id}")
    public ResponseEntity<PedidoExibicaoDto> removerPrimeiroPedidoFilaEspera(@PathVariable int id){
        return ResponseEntity.ok(this.filaService.removerPrimeiroPedidoFilaEspera(id));
    }

    @GetMapping("/fila-espera/posicao/{id}")
    public ResponseEntity<Integer> posicaoNaFila(@PathVariable int id){
        return ResponseEntity.ok(this.filaService.pegarPosicaoNaFila(id));
    }

    @GetMapping("/fila-espera/{id}")
    public ResponseEntity<List<PedidoExibicaoDto>> listarPedidosFilaEsperaPorPai(@PathVariable int id){
        return ResponseEntity.ok(this.filaService.listarFilaDeEspera(id));
    }

    @GetMapping("/fila-espera/pai/{id}")
    public ResponseEntity<PedidoExibicaoDto> buscarPaiPorPedidoId(@PathVariable int id){
        return ResponseEntity.ok(this.filaService.buscarPai(id));
    }

    @GetMapping("/fila-espera")
    public ResponseEntity<List<FilaEspera>> listarFilasEspera(){
        return ResponseEntity.ok(this.filaService.listarFilas());
    }

    // ------------------------------- KPIs ------------------------------- //

    @GetMapping("/quantidade-total")
    public ResponseEntity<Integer> obterQuantidadePedidosTotal(@RequestParam String dataInicial, @RequestParam String dataFinal){
        return ResponseEntity.ok(this.pedidoService.obterQuantidadePedidosTotal(
                converterStringParaLocalDateTime(dataInicial).atStartOfDay(),
                converterStringParaLocalDateTime(dataFinal).atTime(23, 59, 59)
        ));
    }

    @GetMapping("/pedido-por-aluno")
    public ResponseEntity<Double> obterPedidosPorAluno(@RequestParam String dataInicial, @RequestParam String dataFinal){
        return ResponseEntity.ok(this.pedidoService.obterPedidosPorAluno(
                converterStringParaLocalDateTime(dataInicial).atStartOfDay(),
                converterStringParaLocalDateTime(dataFinal).atTime(23, 59, 59)
        ));
    }

    @GetMapping("/quantidade-aulas-hist")
    public ResponseEntity<List<Integer>> obterQuantidadeAulasHistorico(@RequestParam String dataInicial, @RequestParam String dataFinal, @RequestParam Integer status){
        Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(1, "Concluído");
        statusMap.put(2, "Confirmado");
        statusMap.put(3, "Em Fila");
        statusMap.put(4, "Aguardando Pagamento");
        statusMap.put(5, "Pendente");
        statusMap.put(6, "Cancelado");
        statusMap.put(7, "Recusado");

        return ResponseEntity.ok(this.pedidoService.obterQuantidadeAulasHistorico(
                converterStringParaLocalDateTime(dataInicial).atStartOfDay(),
                converterStringParaLocalDateTime(dataFinal).atTime(23, 59, 59),
                statusMap.get(status)
        ));
    }

    @GetMapping("/rendimento-professores")
    public ResponseEntity<Double> obterRendimentoProfessores(@RequestParam String dataInicial, @RequestParam String dataFinal){
        return ResponseEntity.ok(this.pedidoService.obterRendimentoProfessores(
                converterStringParaLocalDateTime(dataInicial).atStartOfDay(),
                converterStringParaLocalDateTime(dataFinal).atTime(23, 59, 59)
        ));
    }

    @GetMapping("/instrumentos-mais-pedidos")
    public ResponseEntity<Map<String, Long>> obterInstrumentosMaisPedidos(@RequestParam String dataInicial, @RequestParam String dataFinal){
        return ResponseEntity.ok(this.pedidoService.obterInstrumentosMaisPedidos(
                converterStringParaLocalDateTime(dataInicial).atStartOfDay(),
                converterStringParaLocalDateTime(dataFinal).atTime(23, 59, 59)
        ));
    }

    @GetMapping("/regioes-mais-pedidos")
    public ResponseEntity<Map<String, Long>> obterRegioesMaisPedidos(@RequestParam String dataInicial, @RequestParam String dataFinal){
        return ResponseEntity.ok(this.pedidoService.obterRegioesMaisPedidos(
                converterStringParaLocalDateTime(dataInicial).atStartOfDay(),
                converterStringParaLocalDateTime(dataFinal).atTime(23, 59, 59)
        ));
    }

    private LocalDate converterStringParaLocalDateTime(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(data, formatter);
    }
}

