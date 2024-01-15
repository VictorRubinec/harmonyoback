package school.sptech.harmonyospringapi.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import school.sptech.harmonyospringapi.domain.Aluno;
import school.sptech.harmonyospringapi.domain.FiltroMinimoMaximo;
import school.sptech.harmonyospringapi.domain.Pedido;
import school.sptech.harmonyospringapi.domain.Professor;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.usuario.AlunoService;
import school.sptech.harmonyospringapi.service.usuario.ProfessorService;
import school.sptech.harmonyospringapi.service.usuario.UsuarioService;
import school.sptech.harmonyospringapi.service.usuario.autenticacao.dto.UsuarioLoginDto;
import school.sptech.harmonyospringapi.service.usuario.autenticacao.dto.UsuarioTokenDto;
import school.sptech.harmonyospringapi.service.usuario.dto.*;
import school.sptech.harmonyospringapi.service.usuario.dto.avaliacao.AvaliacaoCriacaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.avaliacao.AvaliacaoExibicaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.professor.ProfessorExibicaoResumidoDto;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Requisições relacionadas aos usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private AlunoService alunoService;


    @Operation(summary = "Obtém uma lista de todos os usuários cadastrados", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários encontrados."),
            @ApiResponse(responseCode = "204", description = "Não há usuários cadastrados.", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    public ResponseEntity<List<UsuarioExibicaoDto>> listarCadastrados(){

        List<UsuarioExibicaoDto> ltUsuariosExibicao = this.usuarioService.listarCadastrados();

        if (ltUsuariosExibicao.isEmpty()){

            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(ltUsuariosExibicao);
    }

    @Operation(summary = "Entra em uma conta", description = "")
    @ApiResponses( value= {
            @ApiResponse(responseCode= "200", description = "Login realizado."),
            @ApiResponse(responseCode = "404", description = "Email de usuário não cadastrado")
    })
    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto){

        UsuarioTokenDto usuarioTokenDto = this.usuarioService.autenticar(usuarioLoginDto);

        return ResponseEntity.status(200).body(usuarioTokenDto);
    }


    @Operation(summary = "Obtém uma lista de usuários ordenada alfabéticamente", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários ordenados por ordem alfabética encontrados."),
            @ApiResponse(responseCode = "204", description = "Não há usuários cadastrados.")
    })

    @SecurityRequirement(name = "Bearer")
    @GetMapping("/ordem-alfabetica")
    public ResponseEntity<List<UsuarioExibicaoDto>> exibeTodosOrdemAlfabetica(){

        List<UsuarioExibicaoDto> ltUsuariosExibicao = this.usuarioService.exibeTodosOrdemAlfabetica();

        if (ltUsuariosExibicao.isEmpty()){

            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(ltUsuariosExibicao);
    }

    @Operation(summary = "Adiciona uma avaliação a um usuário", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação adicionada."),
            @ApiResponse(responseCode = "404", description = "Não é possível avaliar a si mesmo"),
    })
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/{id}/avaliacoes")
    public ResponseEntity<AvaliacaoExibicaoDto> adicionarAvaliacao(@PathVariable int id, @RequestBody @Valid AvaliacaoCriacaoDto avaliacaoCriacaoDto) {

        AvaliacaoExibicaoDto avaliacaoExibicaoDto = this.usuarioService.criarAvaliacao(id, avaliacaoCriacaoDto);

        return ResponseEntity.created(null).body(avaliacaoExibicaoDto);
    }

    @Operation(summary = "Verifica se pedido já foi avaliado pelo usuário", description = "")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{idPedido}/{idUsuarioAutor}")
    public ResponseEntity<Boolean> valicacaoAvaliacao(@PathVariable int idPedido, @PathVariable int idUsuarioAutor) {
        Boolean validacao = this.usuarioService.existeAvaliacaoNoPedidoPorUsuarioAutor(idPedido, idUsuarioAutor);

        return ResponseEntity.ok(validacao);
    }

    @Operation(summary = "Listar avaliações de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliações encontradas"),
            @ApiResponse(responseCode = "204", description = "Nenhuma avaliação encontrada")
    })
    @GetMapping("/{id}/avaliacoes")
    public ResponseEntity<List<AvaliacaoExibicaoDto>> listarAvaliacao(@PathVariable int id) {

        List<AvaliacaoExibicaoDto> ltAvaliacaoExibicaoDto = this.usuarioService.listarAvaliacoesPorUsuario(id);

        if (ltAvaliacaoExibicaoDto.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(ltAvaliacaoExibicaoDto);
    }

    @GetMapping("/dados-perfil/{id}")
    public ResponseEntity<UsuarioDadosPerfilDto> obterDadosPerfilUsuario(@PathVariable int id){
        return ResponseEntity.ok(this.usuarioService.obterDadosPerfilUsuario(id));
    }

    @GetMapping("/filtro-minimo-maximo")
    public ResponseEntity<FiltroMinimoMaximo> filtroMinimoMaximo(){

        FiltroMinimoMaximo filtroMinimoMaximo = this.usuarioService.filtroMinimoMaximo();

        return ResponseEntity.ok(filtroMinimoMaximo);
    }

    @GetMapping("/{idUsuario}/avaliacoes-recebidas")
    public ResponseEntity<UsuarioTelaFeedback> obterDadosUsuarioTelaFeedback(@PathVariable int idUsuario){
        return ResponseEntity.ok(this.usuarioService.obterDadosUsuarioTelaFeedback(idUsuario));
    }

    @PutMapping("atualiza-dados-pessoais/{id}")
    @Transactional
    public ResponseEntity<Void> atualizarDadosPessoais(@PathVariable int id, @RequestBody @Valid UsuarioAtulizarDadosPessoaisDto dadosUsuario){
        this.usuarioService.atualizarDadosPessoais(id, dadosUsuario);
        return ResponseEntity.status(200).build();
    }

    @PutMapping("atualiza-sobre-mim/{id}")
    @Transactional
    public ResponseEntity<Void> atualizarDadosPessoais(@PathVariable int id, @RequestBody @Valid UsuarioBibliografiaDto usuarioBibliografia){
        this.usuarioService.atualizarBibliografia(id, usuarioBibliografia.getBibliografia());
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/quantidade-cadastrados-periodo")
    public ResponseEntity<Integer> quantidadeCadastradosUsuarios(@RequestParam String dataComeco, @RequestParam String dataFim){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime dataComecoFormatada = LocalDate.parse(dataComeco, formatter).atStartOfDay();
        LocalDateTime dataFimFormatada = LocalDate.parse(dataFim, formatter).atTime(23, 59, 59);

        return ResponseEntity.ok(this.usuarioService.quantidadeCadastradosUsuarios(dataComecoFormatada, dataFimFormatada));
    }

    @GetMapping("/quantidade-cadastrados-semana")
    public ResponseEntity<List<Integer>> obterQuantidadeUsuariosCadastrados(){
        return ResponseEntity.status(200).body(this.usuarioService.obterQuantidadeUsuariosCadastradosSemana());
    }

    @GetMapping("/quantidade-retidos-semana")
    public ResponseEntity<List<Integer>> obterQuantidadeUsuariosReditos(){
        return ResponseEntity.status(200).body(this.usuarioService.obterQuantidadeUsuariosRetidosSemana());
    }

    @GetMapping("/quantidade-cadastrados-mes")
    public ResponseEntity<List<Integer>> obterQuantidadeUsuariosCadastradosMes(){
        return ResponseEntity.status(200).body(this.usuarioService.obterUsuariosCadastradosMes());
    }

    @GetMapping("/quantidade-cadastrados-mes-soma")
    public ResponseEntity<Integer> obterQuantidadeUsuariosCadastradosMesSoma(){
        return ResponseEntity.status(200).body(this.usuarioService.obterUsuariosCadastradosMes().stream().mapToInt(Integer::intValue).sum());
    }

    @GetMapping("/quantidade-cadastrados-mes-anterior")
    public ResponseEntity<List<Integer>> obterQuantidadeUsuariosCadastradosMesAnterior(){
        return ResponseEntity.status(200).body(this.usuarioService.obterUsuariosCadastradosMesAnterior());
    }

    @PostMapping("/importacao-dados-txt")
    public ResponseEntity<Boolean> importarTxt(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }

        try {
            String content = new String(file.getBytes());
            BufferedReader entrada = new BufferedReader(new StringReader(content));
            usuarioService.lerSalvarTxt(entrada);

            return ResponseEntity.ok(true);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(false);
        }
    }
    @PostMapping("/exportacao-dados-txt")
    public ResponseEntity<Boolean> exportarTxt() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date dataHoraAtual = new Date();
        String dataHoraFormatada = sdf.format(dataHoraAtual);
        List<UsuarioExibicaoDto> lista = this.usuarioService.exibeTodosOrdemAlfabetica();

        String header = String.format("00LEAD%s01", dataHoraFormatada);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("arquivo1.txt"))) {
            writer.write(header);
            writer.newLine();
            String corpo = "02";
            for (UsuarioExibicaoDto user : lista) {
                String campoAdicionalProfessor;
                String telefone;
                String sexo;
                Integer id = user.getId();
                String nome = user.getNome();
                String email = user.getEmail();
                String categoria = user.getCategoria();
                String endereco = user.getEndereco().getLogradouro();
                if(user.getCategoria().equalsIgnoreCase("professor")){
                    ProfessorService professorService = new ProfessorService();
                    Professor professor = professorService.buscarPorId(user.getId());
                    campoAdicionalProfessor = professor.getBibliografia().substring(0,10);
                    sexo = professor.getSexo().substring(0,1);
                    telefone = professor.getTelefone();

                    String registroDados =String.format("%s%03d%-40.40s%-50.50s%-1.1s%-10.10s%-13.13s%-10.10s%-40.40s", corpo,id, nome, email,
                            sexo, campoAdicionalProfessor, telefone, categoria, endereco);
                    writer.write(registroDados);
                    writer.newLine();

                } else if (user.getCategoria().equalsIgnoreCase("aluno")) {
                    AlunoService alunoService = new AlunoService();
                    Aluno aluno = alunoService.buscarPorId(user.getId());
                    telefone = aluno.getTelefone();
                    sexo = aluno.getSexo().substring(0,1);

                    String registroDados =String.format("%03d%-40.40s%-50.50s%-1.1s%-13.13s%-10.10s%-40.40s",
                            id, nome, email, sexo, telefone, categoria, endereco);

                    writer.write(registroDados);
                    writer.newLine();
                }
            }

            // Escreve o registro de trailer
            String trailer = String.format("0100005");
            writer.write(trailer);
            return ResponseEntity.ok(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/download-txt-professor")
    public ResponseEntity<byte[]> downloadTXTProfessor() throws IOException {
        StringBuilder txtData = new StringBuilder();

        List<UsuarioExibicaoDto> usuarios = this.professorService.listar();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date dataHoraAtual = new Date();
        String dataHoraFormatada = sdf.format(dataHoraAtual);

        txtData.append(String.format("00LEAD%s01\n", dataHoraFormatada));
        int contador = 0;
        String corpo = "03";
        for (UsuarioExibicaoDto user : usuarios) {
            Integer id = user.getId();
            Professor professor = professorService.buscarPorId(id);

            String nome = user.getNome();
            String email = user.getEmail();
            String sexo = "I";
            String telefone = professor.getTelefone();
            String cpf = professor.getCpf();
            String logradouro = user.getEndereco().getLogradouro();
            String numeroLogradouro = user.getEndereco().getNumero();
            String complemento = user.getEndereco().getComplemento();
            String cidade = user.getEndereco().getCidade();
            String bairro = user.getEndereco().getBairro();


            if(professor.getSexo() != null){
                sexo = professor.getSexo().substring(0, 1);
            }

            txtData.append(String.format("%s%03d%-40.40s%-50.50s%-1.1s%-15.15s%-14.14s%-30.30s%-5.5s%-30.30s%-30.30s%-30.30s\n",corpo,
                    id, nome, email,
                    sexo, telefone, cpf,
                    logradouro, numeroLogradouro, complemento, cidade
                    , bairro));
            contador++;
        }

        String trailer = "01";
        trailer += String.format("%05d\n", contador);


        txtData.append(trailer);

        byte[] txtBytes = txtData.toString().getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "data.txt");

        return new ResponseEntity<>(txtBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/download-txt-aluno")
    public ResponseEntity<byte[]> downloadTXTAluno() throws IOException {
        StringBuilder txtData = new StringBuilder();

        List<UsuarioExibicaoDto> alunos = this.alunoService.listar();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date dataHoraAtual = new Date();
        String dataHoraFormatada = sdf.format(dataHoraAtual);
        int contador = 0;
        txtData.append(String.format("00LEAD%s01\n", dataHoraFormatada));
        String corpo = "02";
        for (UsuarioExibicaoDto user : alunos) {
            Integer id = user.getId();
            Aluno aluno = alunoService.buscarPorId(id);

            String nome = user.getNome();
            String email = user.getEmail();
            String sexo = "I";
            String telefone = aluno.getTelefone();
            String cpf = aluno.getCpf();
            String logradouro = user.getEndereco().getLogradouro();
            String numeroLogradouro = user.getEndereco().getNumero();
            String complemento = user.getEndereco().getComplemento();
            String cidade = user.getEndereco().getCidade();
            String bairro = user.getEndereco().getBairro();


            if(aluno.getSexo() != null){
                sexo = aluno.getSexo().substring(0, 1);
            }
            contador++;
            boolean autenticado = aluno.isAutenticado();

            txtData.append(String.format("%s%03d%-40.40s%-50.50s%-1.1s%-15.15s%-14.14s%-30.30s%-5.5s%-30.30s%-30.30s%-30.30s\n",corpo,
                    id, nome, email,
                    sexo, telefone, cpf,
                    logradouro, numeroLogradouro, complemento, cidade
                    , bairro));

        }


        String trailer = "01";
        trailer += String.format("%05d\n", contador);
        txtData.append(trailer);
        byte[] txtBytes = txtData.toString().getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "data.txt");

        return new ResponseEntity<>(txtBytes, headers, HttpStatus.OK);
    }
}
