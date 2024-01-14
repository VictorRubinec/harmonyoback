package school.sptech.harmonyospringapi.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import school.sptech.harmonyospringapi.domain.*;
import school.sptech.harmonyospringapi.repository.*;
import school.sptech.harmonyospringapi.service.aula.AulaService;
import school.sptech.harmonyospringapi.service.aula.dto.AulaExibicaoDto;
import school.sptech.harmonyospringapi.service.aula.dto.AulaGraficoInformacoesDashboardDto;
import school.sptech.harmonyospringapi.service.aula.dto.AulaMapper;
import school.sptech.harmonyospringapi.service.instrumento.InstrumentoService;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoExibicaoDto;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoMapper;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDashboardDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoHistoricoDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidosMes;
import school.sptech.harmonyospringapi.service.usuario.dto.avaliacao.AvaliacaoCriacaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.avaliacao.AvaliacaoExibicaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.professor.ProfessorExibicaoResumidoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.professor.ProfessorMapper;
import school.sptech.harmonyospringapi.service.usuario.dto.professor.ProfessorPopularDto;
import school.sptech.harmonyospringapi.service.usuario.dto.professor_instrumento.ProfessorInstrumentoCriacaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.professor_instrumento.ProfessorInstrumentoExibicaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.professor_instrumento.ProfessorInstrumentoMapper;
import school.sptech.harmonyospringapi.utils.FiltroAvancado.SpecificationManager;
import school.sptech.harmonyospringapi.utils.ListaGenericaObj;
import school.sptech.harmonyospringapi.service.exceptions.EntidadeConflitanteException;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioCriacaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioExibicaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioMapper;
import school.sptech.harmonyospringapi.utils.FiltroAvancado.ProfessorSpecificationBuilder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private ProfessorInstrumentoRepository professorInstrumentoRepository;

    @Autowired
    private InstrumentoService instrumentoService;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    @Lazy
    private AulaService aulaService;


    /* ================ PROFESSOR ================ */

    public UsuarioExibicaoDto cadastrar(UsuarioCriacaoDto novoProfessorDto){
        if (this.usuarioService.existeUsuarioPorEmail(novoProfessorDto.getEmail())) throw new EntidadeConflitanteException("Erro ao cadastrar. Email já cadastrado !");
        else if (this.usuarioService.existeUsuarioPorCpf(novoProfessorDto.getCpf())) throw new EntidadeConflitanteException("Erro ao cadastrar. CPF já cadastrado !");

        String senhaCriptofrada = passwordEncoder.encode(novoProfessorDto.getSenha());

        novoProfessorDto.setSenha(senhaCriptofrada);

        final Professor novoProfessor = UsuarioMapper.ofProfessorCriacao(novoProfessorDto);

        Professor professorCadastrado = this.professorRepository.save(novoProfessor);

        return UsuarioMapper.ofUsuarioExibicao(professorCadastrado);
    }

    public List<UsuarioExibicaoDto> listar(){

        List<Professor> ltProfessores = this.professorRepository.findAll();

        return ltProfessores.stream().map(UsuarioMapper::ofUsuarioExibicao).toList();
    }

    public void encontrarProfessores(){

        List<Professor> professores = this.professorRepository.findAll();
        List<ProfessorExibicaoResumidoDto> professorExibicaoDto = new ArrayList<>();

        for (Professor professor: professores) {
           // double mediaAvaliacao = this.avaliacaoRepository.obterMediaAvaliacaoProfessor(professor.getId());
          //  List<Instrumento> instrumentos = this.professorInstrumentoRepository.listarInstrumentosPeloIdDoProfessor(professor.getId());

        }

    }

    /* ================ AVALIAÇÃO ================ */

    public AvaliacaoExibicaoDto criarAvaliacao(AvaliacaoCriacaoDto avaliacaoCriacaoDto) {
        return null;
    }

    /* ================ PESQUISA ================ */

    public List<ProfessorExibicaoResumidoDto> buscarTodosFiltrado(String listaDeParametros) {
        Specification<Professor> spec = new SpecificationManager().obterProfessoresFiltrados(listaDeParametros);

        return this.professorRepository.findAll(spec).stream().map(
                professor -> {
                    Endereco endereco = professor.getEndereco();
                    Double mediaAvaliacao = this.obterMediaAvaliacao(professor.getId());
                    Double valorMinimo = this.obterValorMinimoAula(professor.getId());
                    Double valorMaximo = this.obterValorMaximoAula(professor.getId());
                    Boolean emprestaInstrumento = this.emprestaInstrumento(professor.getId());
                    String bairro = endereco.getBairro();
                    String cidade = endereco.getCidade();
                    String estado = endereco.getEstado();
                    Double distancia = 0.0;
                    List<InstrumentoExibicaoDto> instrumentosConhecidos = this.listarAulasDosInstrumentos(professor.getId()).stream().map(AulaMapper::ofInstrumentoExibicao).toList();
                    Integer qtdAvaliacoes = this.obterQuantidadeAvaliacoes(professor.getId());
                    return ProfessorMapper.of(professor,
                            instrumentosConhecidos,
                            valorMinimo,
                            valorMaximo,
                            emprestaInstrumento,
                            mediaAvaliacao,
                            qtdAvaliacoes,
                            distancia,
                            bairro,
                            cidade,
                            estado);
                }
        ).toList();
    }


    public UsuarioExibicaoDto buscarPorIdParaExibicao(Integer id){
        Optional<Professor> professorOpt = this.professorRepository.findById(id);

        if (professorOpt.isEmpty()){
            throw new EntitadeNaoEncontradaException(
                    String.format(
                            "Professor com o id %d não encontrado !",
                            id
                    ));
        }

        return UsuarioMapper.ofUsuarioExibicao(professorOpt.get());
    }

    public UsuarioExibicaoDto buscarPorNome(String nome){

        List<Professor> ltProfessores = this.professorRepository.findAll();

        ListaGenericaObj<Usuario> ltProfessoresGenerica = new ListaGenericaObj<>(ltProfessores.size());


        ltProfessores.forEach(ltProfessoresGenerica::adiciona);


        ltProfessoresGenerica = new UsuarioComparador(ltProfessoresGenerica).ordenacaoAlfabetica();


        int indiceUsuarioEncontrado = new UsuarioComparador(ltProfessoresGenerica).pesquisaBinariaPorNome(nome);


        if (indiceUsuarioEncontrado == -1){
            throw new EntitadeNaoEncontradaException("Professor com o nome " + nome + " não encontrado !");
        }

        return UsuarioMapper.ofUsuarioExibicao(ltProfessoresGenerica.getElemento(indiceUsuarioEncontrado));

    }

    public Professor buscarPorId(Integer id){
        Optional<Professor> professorOpt = this.professorRepository.findById(id);

        if (professorOpt.isEmpty()){
            throw new EntitadeNaoEncontradaException(
                    String.format(
                            "Professor com o id %d não encontrado !",
                            id
                    ));
        }

        return professorOpt.get();
    }


    public List<UsuarioExibicaoDto> obterTodosEmOrdemAlfabetica(){

        List<Professor> ltProfessores = this.professorRepository.findAll();

        ListaGenericaObj<Usuario> ltProfessoresGenerica = new ListaGenericaObj<>(ltProfessores.size());

        ltProfessores.forEach(ltProfessoresGenerica::adiciona);

        ltProfessoresGenerica = new UsuarioComparador(ltProfessoresGenerica).ordenacaoAlfabetica();

        ltProfessores.clear();

        for (int i = 0; i < ltProfessoresGenerica.size(); i ++){
            ltProfessores.add((Professor)ltProfessoresGenerica.getElemento(i));
        }

        return ltProfessores.stream().map(UsuarioMapper::ofUsuarioExibicao).toList();
    }

    public boolean existeProfessorPorId(Integer id){
        return this.professorRepository.existsById(id);
    }

    /* ================ REMOVER ================ */

    public void deletarPorId(Integer id){

        if (this.professorRepository.existsById(id)){
            this.professorRepository.deleteById(id);
        }
        else {
            throw new EntitadeNaoEncontradaException(
                    String.format("Professor com o id %d não encontrado !", id)
            );
        }
    }

    /* ================ INSTRUMENTOS ================ */

    public List<InstrumentoExibicaoDto> listarInstrumentos(int professorId) {
        List<Instrumento> instrumentos = this.professorInstrumentoRepository.listarInstrumentosPeloIdDoProfessor(professorId);

        if (instrumentos.isEmpty()) throw new EntitadeNaoEncontradaException("Professor não possui instrumentos cadastrados !");

        return instrumentos.stream().map(InstrumentoMapper::ofInstrumentoExibicao).toList();
    }

    public List<AulaExibicaoDto> listarAulasDosInstrumentos(int professorId) {
        List<AulaExibicaoDto> aulasComInstrumentosInsinados = this.aulaService.buscarAulasPorIdProfessor(professorId);

        if (aulasComInstrumentosInsinados.isEmpty()) throw new EntitadeNaoEncontradaException("Professor não possui aulas cadastradas !");

        return aulasComInstrumentosInsinados;
    }

    public ProfessorInstrumentoExibicaoDto criar(Integer professorId, ProfessorInstrumentoCriacaoDto professorInstrumentoCriacaoDto) {
        Professor professor = buscarPorId(professorId);
        Instrumento instrumento = this.instrumentoService.buscarPorId(professorInstrumentoCriacaoDto.getInstrumentoId());

        if (this.professorInstrumentoRepository.existsByProfessor_idAndInstrumento_id(professor.getId(), instrumento.getId())){
                    throw new EntidadeConflitanteException(
                        String.format(
                                "Professor com o id %d já possui o instrumento com o id %d cadastrado !",
                                professorId,
                                professorInstrumentoCriacaoDto.getInstrumentoId()
                        ));
        }

        ProfessorInstrumento professorInstrumentoCadastrado = this.professorInstrumentoRepository
                .save(ProfessorInstrumentoMapper.of(professorInstrumentoCriacaoDto, professor, instrumento));

        return ProfessorInstrumentoMapper.ofProfessorInstrumentoExibicao(professorInstrumentoCadastrado);
    }

    public Boolean emprestaInstrumento(Integer idProfessor){
        return this.professorRepository.emprestaInstrumento(idProfessor).orElse(false);
    }

    /* =============== AULAS ================== */
    public Double obterValorMinimoAula (Integer professorId){
        return this.aulaRepository.obterValorMinimoAula(professorId).orElse(0d);
    }

    public Double obterValorMaximoAula (Integer professorId){
        return this.aulaRepository.obterValorMaximoAula(professorId).orElse(0d);
    }



    /* =============== AVALIAÇÃO ================== */

    public Double obterMediaAvaliacao(Integer professorId){
        Double media = avaliacaoRepository.getMediaAvaliacaoProfessor(professorId).orElse(0d);
        return media;
    }

    public Integer obterQuantidadeAvaliacoes(Integer id) {
        return this.avaliacaoRepository.getQuantidadeAvaliacoes(id).orElse(0);
    }

    /* ========================= DASHBOARD ======================= */



    public Double getRendimentoMesAtual(int idProfessor){

        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime comeco = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),1,0,0);
        LocalDateTime fim = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),30,23,59);
        Double rendimento = this.professorRepository.getRendimentoPorPeriodo(idProfessor,comeco, fim).orElse(0d);
        return rendimento;
    }

    public Integer getQuantidadeAlunosMesAtual(int id) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime comeco = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),1,0,0);
        LocalDateTime fim = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),30,23,59);
        Integer quantidadeAlunos = this.professorRepository.getQuantidadeAlunosPorPeriodo(id,comeco, fim).orElse(0);
        return quantidadeAlunos;
    }

    public Integer getQuantidadeAulasMesAtual(int id) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime comeco = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),1,0,0);
        LocalDateTime fim = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),30,23,59);
        Integer quantidadeAulas = this.professorRepository.getQuantidadeAulasPorPeriodo(id,comeco, fim).orElse(0);
        return quantidadeAulas;
    }

    public Long getMediaTempoResposta(int id) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        List<Pedido> pedidos = this.pedidoRepository.buscarPorUsuarioId(id);
        Long somaTempoResposta = pedidos.stream().filter(p -> p.getHoraCriacao().getMonth() == now.getMonth() && p.getHoraResposta() != null).mapToLong(p -> Duration.between(p.getHoraCriacao(), p.getHoraResposta()).toMinutes()).sum();
        Long mediaTempoResposta = somaTempoResposta / pedidos.stream().filter(p -> p.getHoraResposta() != null).count();
        return mediaTempoResposta;
    }


    public List<PedidoHistoricoDto> getHistoricoPedidos(int id) {
        List<PedidoHistoricoDto> pedidos = this.professorRepository.getHistoricoPedidos(id);
        return pedidos;
    }

    public List<PedidoExibicaoDashboardDto> getAulasRealizadasMensal(int id){
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime comeco = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),1,0,0);
        LocalDateTime fim = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),30,23,59);

        List<PedidoExibicaoDashboardDto> pedidos = this.professorRepository.getAulasRealizadasAgrupadasPorInstrumentoMesAtual(id, comeco, fim);
        return pedidos;
    }
    public List<PedidoExibicaoDashboardDto> getAulasRealizadasAnual(int id){
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime comeco = LocalDateTime.of(now.getYear(), 1,1,0,0);
        LocalDateTime fim = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),30,23,59);

        List<PedidoExibicaoDashboardDto> pedidos = this.professorRepository.getAulasRealizadasAgrupadasPorInstrumentoMesAtual(id, comeco, fim);
        return pedidos;
    }

    public List<PedidoExibicaoDashboardDto> getAulasRealizadasTotal(int id){
        List<PedidoExibicaoDashboardDto> pedidos = this.professorRepository.getAulasRealizadasAgrupadasPorInstrumentoTotal(id);
        return pedidos;
    }

    public AulaGraficoInformacoesDashboardDto getDadosAulasMesAtual(int id) {

        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime comeco = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),1,0,0);
        LocalDateTime fim = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),30,23,59);

        return this.pedidoRepository.getDadosAulasPeriodoPorIdProfessor(id, comeco,fim);
    }

    public List<ProfessorPopularDto> buscarProfessoresPopulares() {
        List<Professor> professores = this.professorRepository.buscarProfessoresPopulares();

        return professores.stream().map(p -> ProfessorMapper.ofPopular(p, this.obterMediaAvaliacao(p.getId()))).toList();
    }

    public Long getMediaTempoRespostaAnual(int id) {

        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

        List<Pedido> pedidos = this.pedidoRepository.buscarPorUsuarioIdAnual(id);
        Long somaTempoResposta = pedidos.stream().filter(p -> p.getHoraCriacao().getYear() == now.getYear() && p.getHoraResposta() != null ).mapToLong(p -> Duration.between(p.getHoraCriacao(), p.getHoraResposta()).toMinutes()).sum();
        return somaTempoResposta / pedidos.size();
    }

    public Double getRendimentoAnual(int id) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime comeco = LocalDateTime.of(now.getYear(), 1,1,0,0);
        LocalDateTime fim = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),30,23,59);
        return this.professorRepository.getRendimentoPorPeriodo(id,comeco, fim).orElse(0d);
    }

    public Integer getQuantidadeAlunosAnual(int id) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime comeco = LocalDateTime.of(now.getYear(), 1,1,0,0);
        LocalDateTime fim = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),30,23,59);
        return this.professorRepository.getQuantidadeAlunosPorPeriodo(id,comeco, fim).orElse(0);
    }

    public Integer getQuantidadeAulasAnual(int id) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime comeco = LocalDateTime.of(now.getYear(), 1,1,0,0);
        LocalDateTime fim = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),30,23,59);
        return this.professorRepository.getQuantidadeAulasPorPeriodo(id,comeco, fim).orElse(0);
    }

    public List<PedidosMes> dadosAulasAnual(int id) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime comeco = LocalDateTime.of(now.getYear(), 1,1,0,0);
        LocalDateTime fim = LocalDateTime.of(now.getYear(), now.getMonth().getValue(),30,23,59);
        return this.professorRepository.getAulasAgrupadasPorMes(id, comeco, fim);
    }




    public Long getMediaTempoRespostaTotal(int id) {

        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

        List<Pedido> pedidos = this.pedidoRepository.buscarPorUsuarioId(id);
        Long somaTempoResposta = pedidos.stream().filter(p -> p.getHoraCriacao().getYear() == now.getYear() && p.getHoraResposta() != null ).mapToLong(p -> Duration.between(p.getHoraCriacao(), p.getHoraResposta()).toMinutes()).sum();
        return somaTempoResposta / pedidos.size();
    }

    public Double getRendimentoTotal(int id) {
        return this.professorRepository.getRendimento(id).orElse(0d);
    }

    public Integer getQuantidadeAlunosTotal(int id) {
        return this.professorRepository.getQuantidadeAlunos(id).orElse(0);
    }

    public Integer getQuantidadeAulasTotal(int id) {
        return this.professorRepository.getQuantidadeAulas(id).orElse(0);
    }

    public List<PedidosMes> dadosAulasTotal(int id) {
        return this.professorRepository.getAulasAgrupadasPorMes(id);
    }



    /* =========== Métricas Dashboard Admin ========= */

    public List<Integer> obterQuantidadeCadastradosSemana() {
        List<Integer> ltQtdUsuario = new ArrayList<>();

        LocalDateTime dataAtual = obterPrimeiroDiaSemana();
        LocalDateTime dataInicialAux, dataFinalAux;

        for (int i = 0; i < 7; i++) {
            dataInicialAux = dataAtual.withHour(0).withMinute(0).withSecond(0);
            dataFinalAux = dataAtual.withHour(23).withMinute(59).withSecond(59);

            ltQtdUsuario.add(this.professorRepository.obterQuantidadeCadastradosEntre(dataInicialAux, dataFinalAux));


            dataAtual = dataAtual.plusDays(1);
        }

        return ltQtdUsuario;
    }

    private LocalDateTime obterPrimeiroDiaSemana() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        LocalDateTime dataInicial = c.getTime().toInstant().atZone(c.getTimeZone().toZoneId()).toLocalDateTime();

        dataInicial = dataInicial.withHour(0).withMinute(0).withSecond(0);

        return dataInicial;
    }

    public Double getRendimentoTotalPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return this.professorRepository.getRendimentoTotalPeriodo(dataInicial, dataFinal).orElse(0d);
    }
}
