package school.sptech.harmonyospringapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import school.sptech.harmonyospringapi.domain.Professor;
import school.sptech.harmonyospringapi.service.aula.dto.AulaExibicaoDto;
import school.sptech.harmonyospringapi.service.aula.dto.AulaGraficoInformacoesDashboardDto;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoExibicaoDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDashboardDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoHistoricoDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidosMes;
import school.sptech.harmonyospringapi.service.usuario.dto.professor.ProfessorExibicaoResumidoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.professor.ProfessorPopularDto;
import school.sptech.harmonyospringapi.service.usuario.dto.professor_instrumento.ProfessorInstrumentoCriacaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.professor_instrumento.ProfessorInstrumentoExibicaoDto;
import school.sptech.harmonyospringapi.service.usuario.ProfessorService;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioCriacaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioExibicaoDto;
import school.sptech.harmonyospringapi.utils.ArvoreBin;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@RestController
@RequestMapping("/professores")
@Tag(name = "Professores")
@CrossOrigin("*")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    private ArvoreBin arvoreBin;

    @Operation(summary = "Cadastra um professor", description = "")
    @ApiResponse(responseCode = "201", description = "Professor cadastrado.")
    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioExibicaoDto> cadastrar(@RequestBody @Valid UsuarioCriacaoDto usuarioCriacaoDto) {

        UsuarioExibicaoDto professorCadastrado = this.professorService.cadastrar(usuarioCriacaoDto);

        return ResponseEntity.status(201).body(professorCadastrado);
    }

    @Operation(summary = "Obtém uma lista de todos os professores cadastrados", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professores encontrados."),
            @ApiResponse(responseCode = "204", description = "Não há professores cadastrados.", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    public ResponseEntity<List<UsuarioExibicaoDto>> obterTodos() {

        List<UsuarioExibicaoDto> ltUsuariosExibicao = this.professorService.listar();

        if (ltUsuariosExibicao.isEmpty()) {

            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(ltUsuariosExibicao);
    }


    @Operation(summary = "Obtém um professor pelo seu id", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professor encontrado."),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado.")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioExibicaoDto> buscarPorId(@PathVariable Integer id) {

        return ResponseEntity.status(200).body(this.professorService.buscarPorIdParaExibicao(id));
    }


    @Operation(summary = "Obtém uma lista de professores ordenada alfabéticamente", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professores ordenados por ordem alfabética encontrados."),
            @ApiResponse(responseCode = "204", description = "Não há professores cadastrados.")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/ordem-alfabetica")
    public ResponseEntity<List<UsuarioExibicaoDto>> obterTodosEmOrdemAlfabetica() {

        List<UsuarioExibicaoDto> ltUsuariosExibicao = this.professorService.obterTodosEmOrdemAlfabetica();

        if (ltUsuariosExibicao.isEmpty()) {

            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(ltUsuariosExibicao);
    }


    /*
    Recomendo adicionar mais status e
    em vez de retornar apenas 1 usuário, retorna vários, pois daria para utilizar como pesquisa.
    Ass. João
*/
    @Operation(summary = "Obtém um professor pelo seu nome", description = "")
    @ApiResponse(responseCode = "200", description = "Professor encontrado.")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/nome")
    public ResponseEntity<UsuarioExibicaoDto> obterPorNome(@RequestParam String nome) {

        UsuarioExibicaoDto professorEncontrado = this.professorService.buscarPorNome(nome);

        return ResponseEntity.status(200).body(professorEncontrado);
    }

    @Operation(summary = "Deleta um professor através do seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professor deletado do sistema"),
            @ApiResponse(responseCode = "404", description = "ID Inválido. Professor não encontrado")
    })
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/exclusao/conta/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Integer id){

        this.professorService.deletarPorId(id);

        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Lista professores a partir do ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professor(es) encontrado(s) : "),
            @ApiResponse(responseCode = "404", description = "ID Inválido. Professor não encontrado")
    })
    @GetMapping("/{id}/instrumentos")
    public ResponseEntity<List<InstrumentoExibicaoDto>> listarInstrumentos(@PathVariable int id) {
        List<InstrumentoExibicaoDto> instrumentos = this.professorService.listarInstrumentos(id);
        return instrumentos.isEmpty() ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(instrumentos);
    }

    @GetMapping("/{id}/aulas")
    public ResponseEntity<List<AulaExibicaoDto>> listarAulas(@PathVariable int id) {
        List<AulaExibicaoDto> aulas = this.professorService.listarAulasDosInstrumentos(id);
        return aulas.isEmpty() ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(aulas);
    }


    @Operation(summary = "Adiciona Professor a partir do ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professor adicionado. ")
    })
    @PostMapping("/{id}/instrumentos")
    public ResponseEntity<ProfessorInstrumentoExibicaoDto> adicionarInstrumentos(@PathVariable int id, @RequestBody @Valid ProfessorInstrumentoCriacaoDto professorInstrumentoCriacaoDto) {

        ProfessorInstrumentoExibicaoDto professorInstrumentoExibicaoDto = this.professorService.criar(id, professorInstrumentoCriacaoDto);

        return ResponseEntity.status(201).body(professorInstrumentoExibicaoDto);
    }


    @Operation(summary = "Retorna a media de tempo de resposta do mês ", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professores com o valor/aula mais altos: "),
            @ApiResponse(responseCode = "204", description = "Professores não encontrados. ")
    })
    @SecurityRequirement(name = "Bearer")
        @GetMapping("/dashboard/mes-atual/media-tempo-resposta/{id}")
    public ResponseEntity<Long> getMediaTempoResposta(@PathVariable int id){
        Long mediaTempoResposta = this.professorService.getMediaTempoResposta(id);
        return ResponseEntity.status(200).body(mediaTempoResposta);
    }


    @Operation(summary = "Obtém o rendimento do mês atual de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendimento do professor solicitado "),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado. ")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/mes-atual/rendimento/{id}")
    public ResponseEntity<Double> getRendimentoMesAtual(@PathVariable int id){
        Double rendimento = this.professorService.getRendimentoMesAtual(id);
        return ResponseEntity.status(200).body(rendimento);
    }

    @Operation(summary = "Obtém a quantidade de alunos do mês atual de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade de alunos nas ultimas 24 horas do professor solicitado:  "),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado. ")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/mes-atual/qtd-alunos/{id}")
    public ResponseEntity<Integer> getQuantidadeAlunosMesAtual(@PathVariable int id){
        Integer qtdAlunos = this.professorService.getQuantidadeAlunosMesAtual(id);
        return ResponseEntity.status(200).body(qtdAlunos);
    }

    @Operation(summary = "Obtém a quantidade de aulas do mês atual de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade de aulas do mês atual do professor solicitado:  "),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado. ")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/mes-atual/qtd-aulas/{id}")
    public ResponseEntity<Integer> getQuantidadeAulasMesAtual(@PathVariable int id){
        Integer qtdAulas = this.professorService.getQuantidadeAulasMesAtual(id);
        return ResponseEntity.status(200).body(qtdAulas);
    }

    @Operation(summary = "Retorna a média anual de tempo de resposta", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Média anual de tempo de resposta do professor solicitado"),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/ano-atual/media-tempo-resposta/{id}")
    public ResponseEntity<Long> getMediaTempoRespostaAnual(@PathVariable int id){
        Long mediaTempoRespostaAnual = this.professorService.getMediaTempoRespostaAnual(id);
        return ResponseEntity.status(200).body(mediaTempoRespostaAnual);
    }

    @Operation(summary = "Obtém o rendimento anual de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendimento anual do professor solicitado"),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/ano-atual/rendimento/{id}")
    public ResponseEntity<Double> getRendimentoAnual(@PathVariable int id){
        Double rendimentoAnual = this.professorService.getRendimentoAnual(id);
        return ResponseEntity.status(200).body(rendimentoAnual);
    }

    @Operation(summary = "Obtém a quantidade anual de alunos de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade anual de alunos do professor solicitado"),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/ano-atual/qtd-alunos/{id}")
    public ResponseEntity<Integer> getQuantidadeAlunosAnual(@PathVariable int id){
        Integer qtdAlunosAnual = this.professorService.getQuantidadeAlunosAnual(id);
        return ResponseEntity.status(200).body(qtdAlunosAnual);
    }

    @Operation(summary = "Obtém a quantidade anual de aulas de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade anual de aulas do professor solicitado"),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/ano-atual/qtd-aulas/{id}")
    public ResponseEntity<Integer> getQuantidadeAulasAnual(@PathVariable int id){
        Integer qtdAulasAnual = this.professorService.getQuantidadeAulasAnual(id);
        return ResponseEntity.status(200).body(qtdAulasAnual);
    }

    @Operation(summary = "Retorna a média total de tempo de resposta", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Média total de tempo de resposta do professor solicitado"),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/total/media-tempo-resposta/{id}")
    public ResponseEntity<Long> getMediaTempoRespostaTotal(@PathVariable int id){
        Long mediaTempoRespostaTotal = this.professorService.getMediaTempoRespostaTotal(id);
        return ResponseEntity.status(200).body(mediaTempoRespostaTotal);
    }

    @Operation(summary = "Obtém o rendimento total de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendimento total do professor solicitado"),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/total/rendimento/{id}")
    public ResponseEntity<Double> getRendimentoTotal(@PathVariable int id){
        Double rendimentoTotal = this.professorService.getRendimentoTotal(id);
        return ResponseEntity.status(200).body(rendimentoTotal);
    }

    @Operation(summary = "Obtém a quantidade total de alunos de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade total de alunos do professor solicitado"),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/total/qtd-alunos/{id}")
    public ResponseEntity<Integer> getQuantidadeAlunosTotal(@PathVariable int id){
        Integer qtdAlunosTotal = this.professorService.getQuantidadeAlunosTotal(id);
        return ResponseEntity.status(200).body(qtdAlunosTotal);
    }

    @Operation(summary = "Obtém a quantidade total de aulas de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade total de aulas do professor solicitado"),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/total/qtd-aulas/{id}")
    public ResponseEntity<Integer> getQuantidadeAulasTotal(@PathVariable int id){
        Integer qtdAulasTotal = this.professorService.getQuantidadeAulasTotal(id);
        return ResponseEntity.status(200).body(qtdAulasTotal);
    }



    /*  GRAFICO DASH   */

    @Operation(summary = "Obtém o histórico de pedidos de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de pedidos do professor solicitado:  "),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado. ")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/grafico/historico/{id}")
    public ResponseEntity<List<PedidoHistoricoDto>> getHistoricoPedidos(@PathVariable int id){
        List<PedidoHistoricoDto> historico = this.professorService.getHistoricoPedidos(id);
        return  historico.isEmpty()? ResponseEntity.status(204).build() :
                ResponseEntity.status(200).body(historico);

    }

    @Operation(summary = "Obtém a quantidade de aulas solicitadas, canceladas e realizadas de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de pedidos do professor solicitado:  "),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado. ")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/grafico/aulas-realizadas-mes-atual/{id}")
    public ResponseEntity<AulaGraficoInformacoesDashboardDto> getDadosAulasMesAtual(@PathVariable int id){
        AulaGraficoInformacoesDashboardDto historico = this.professorService.getDadosAulasMesAtual(id);
        return
                ResponseEntity.status(200).body(historico);

    }


    @Operation(summary = "Obtém o histórico de aulas realizadas de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de aulas realizadas do professor solicitado:  "),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado. ")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/minhas-aulas-mes/{id}")
    public ResponseEntity<List<PedidoExibicaoDashboardDto>> getAulasRealizadasMensal(@PathVariable int id){
        List<PedidoExibicaoDashboardDto> aulas = this.professorService.getAulasRealizadasMensal(id);
        return  aulas.isEmpty()? ResponseEntity.status(204).build() :
                ResponseEntity.status(200).body(aulas);

    }

    @Operation(summary = "Obtém o histórico de aulas realizadas de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de aulas realizadas do professor solicitado:  "),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado. ")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/minhas-aulas-ano/{id}")
    public ResponseEntity<List<PedidoExibicaoDashboardDto>> getAulasRealizadasAnual(@PathVariable int id){
        List<PedidoExibicaoDashboardDto> aulas = this.professorService.getAulasRealizadasAnual(id);
        return  aulas.isEmpty()? ResponseEntity.status(204).build() :
                ResponseEntity.status(200).body(aulas);

    }

    @Operation(summary = "Obtém o histórico de aulas realizadas de um professor a partir de seu ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de aulas realizadas do professor solicitado:  "),
            @ApiResponse(responseCode = "204", description = "Professor com o ID fornecido não encontrado. ")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dashboard/minhas-aulas-total/{id}")
    public ResponseEntity<List<PedidoExibicaoDashboardDto>> getAulasRealizadasTotal(@PathVariable int id){
        List<PedidoExibicaoDashboardDto> aulas = this.professorService.getAulasRealizadasTotal(id);
        return  aulas.isEmpty()? ResponseEntity.status(204).build() :
                ResponseEntity.status(200).body(aulas);

    }



    @Operation(summary = "Exibe e Filtra um professor a partir dos parãmetros solicitados (Exemplo: você pode pesquisar por 'Joao' para encontrar professores" +
            "chamados João) ", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professores com o parãmetro solicitado encontrados : "),
            @ApiResponse(responseCode = "204", description = "Professores com o parãmetro solicitado não encontrados. ")
    })
    @GetMapping("/busca")
    public ResponseEntity<List<ProfessorExibicaoResumidoDto>> filtrarProfessor(@RequestParam(required = false) String params) {
        List<ProfessorExibicaoResumidoDto> professores = this.professorService.buscarTodosFiltrado(params);

        if (professores.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(professores);
    }

    @Operation(summary = "Exibe os 4 professores mais populares (por melhores avaliações)", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professores encontrados: ")
    })
    @GetMapping("/populares")
    public ResponseEntity<List<ProfessorPopularDto>> getProfessoresPopulares() {
        List<ProfessorPopularDto> professores = this.professorService.buscarProfessoresPopulares();

        if (professores.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(professores);
    }

    @Operation(summary = "Devolve dados sobre mês e quantidade de aulas por status", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados encontrados : "),
            @ApiResponse(responseCode = "204", description = "Dados não encontrados. ")
    })
    @GetMapping("/dashboard/dados-aulas-anual/{id}")
    public ResponseEntity<List<PedidosMes>> dadosAulasAnual(@PathVariable int id) {
        List<PedidosMes> mesesAulas = this.professorService.dadosAulasAnual(id);

        if (mesesAulas.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(mesesAulas);
    }
    @Operation(summary = "Devolve dados sobre mês e quantidade de aulas por status", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados encontrados : "),
            @ApiResponse(responseCode = "204", description = "Dados não encontrados. ")
    })
    @GetMapping("/dashboard/dados-aulas-total/{id}")
    public ResponseEntity<List<PedidosMes>> dadosAulasTotal(@PathVariable int id) {
        List<PedidosMes> mesesAulas = this.professorService.dadosAulasTotal(id);

        if (mesesAulas.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(mesesAulas);
    }
    @GetMapping("/quantidade-cadastrados-semana")
    public ResponseEntity<List<Integer>> obterQuantidadeCadastrados(){
        return ResponseEntity.status(200).body(this.professorService.obterQuantidadeCadastradosSemana());
    }

    @GetMapping("/exportacao-dados-professores")
    public ResponseEntity<Boolean> exportarCsvProfessores(){
            FileWriter arq = null;
            Formatter saida = null;
            boolean deuRuim = false;
            String nomeArq = "professores_" + System.currentTimeMillis();
            List<UsuarioExibicaoDto> todosProfessores = this.professorService.obterTodosEmOrdemAlfabetica();

            try {
                arq = new FileWriter(nomeArq);
                saida = new Formatter(arq);
            } catch (IOException erro) {
                System.out.println("Erro ao abrir o arquivo");
                System.exit(1);
            }

            try {
                for (UsuarioExibicaoDto p : todosProfessores) {
                    Double rendimentoProfessor = this.professorService.getRendimentoMesAtual(p.getId());
                    saida.format("%d;%s;%s;%.2f;\n", p.getId(), p.getNome(),
                            p.getEmail(), rendimentoProfessor);
                }
            } catch (FormatterClosedException erro) {
                System.out.println("Erro ao gravar no arquivo");
                deuRuim = true;
            } finally {
                saida.close();
                try {
                    arq.close();
                } catch (IOException erro) {
                    System.out.println("Erro ao fechar o arquivo");
                    deuRuim = true;
                }
                if (deuRuim) {
                    System.exit(1);
                    return ResponseEntity.badRequest().body(false);
                }
            }

        return ResponseEntity.ok(true);
    }

    @GetMapping("/download-csv")
    public ResponseEntity<byte[]> downloadCSV() throws IOException {
        StringBuilder csvData = new StringBuilder();

        List<UsuarioExibicaoDto> professores = this.professorService.obterTodosEmOrdemAlfabetica();

        csvData.append(String.format("%S;%S;%S;%S\n", "ID", "NOME", "EMAIL", "RENDIMENTO_MES"));

        for (UsuarioExibicaoDto p : professores) {
            Double rendimentoProfessor = this.professorService.getRendimentoMesAtual(p.getId());
            csvData.append(String.format("%d;%s;%s;%.2f\n",p.getId(), p.getNome(),
                    p.getEmail(), rendimentoProfessor));
        }
        byte[] csvBytes = csvData.toString().getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "data.csv");

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/importacao-dados-csv")
    public ResponseEntity<Boolean> importarCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            List<String[]> dados = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] campos = line.split(";\n");
                dados.add(campos);
            }

            return ResponseEntity.ok(true);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping("/rendimento-total-periodo")
    public ResponseEntity<Double> getRendimentoTotal(@RequestParam String dataComeco, @RequestParam String dataFim) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime dataComecoFormatada = LocalDate.parse(dataComeco, formatter).atStartOfDay();
        LocalDateTime dataFimFormatada = LocalDate.parse(dataFim, formatter).atTime(23, 59, 59);

        return ResponseEntity.status(200).body(this.professorService.getRendimentoTotalPeriodo(dataComecoFormatada, dataFimFormatada));

    }

    @GetMapping("/dashboard/rendimentoTotal")
    public ResponseEntity<Double> getRendimentoTotalProfessores() {
        LocalDateTime dataComeco = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime dataFim = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
        return ResponseEntity.status(200).body(this.professorService.getRendimentoTotalPeriodo(dataComeco, dataFim));
    }
}
