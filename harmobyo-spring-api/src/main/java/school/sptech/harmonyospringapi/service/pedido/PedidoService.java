package school.sptech.harmonyospringapi.service.pedido;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import school.sptech.harmonyospringapi.domain.*;
import school.sptech.harmonyospringapi.repository.*;
import school.sptech.harmonyospringapi.service.aula.AulaService;
import school.sptech.harmonyospringapi.service.exceptions.EntidadeConflitanteException;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.notificacao.NotificacaoService;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoAlteracaoStatus;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoCriacaoDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoMapper;
import school.sptech.harmonyospringapi.service.pedido.fila.FilaEsperaService;
import school.sptech.harmonyospringapi.service.pedido.hashing.HashTableService;
import school.sptech.harmonyospringapi.service.socket.WebSocketService;
import school.sptech.harmonyospringapi.service.status.StatusService;
import school.sptech.harmonyospringapi.service.usuario.AlunoService;
import school.sptech.harmonyospringapi.service.usuario.ProfessorService;
import school.sptech.harmonyospringapi.service.usuario.UsuarioService;
import school.sptech.harmonyospringapi.utils.PilhaObj;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.round;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private AulaService aulaService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private HashTableService hashTableService;
//    @Autowired
//    private FilaEsperaService filaEsperaService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    @Lazy
    private WebSocketService webSocketService;

    public List<PedidoExibicaoDto> obterTodos() {

        return this.repository.findAll()
                .stream()
                .map(PedidoMapper::ofPedidoExibicaoDto)
                .toList();
    }

    public List<PedidoExibicaoDto> inverterLista(List<PedidoExibicaoDto> pedidos) {
        List<PedidoExibicaoDto> pedidosInvertidos = new ArrayList<>();

        for (int i = pedidos.size() - 1; i >= 0; i--) {
            pedidosInvertidos.add(pedidos.get(i));
        }

        return pedidosInvertidos;
    }

    public PilhaObj<PedidoExibicaoDto> obterPedidosPendentes(int idProfessor){

        List<Pedido> pedidosPendentes = repository.encontrarPedidosPendentesPorIdProfessor(idProfessor);
        PilhaObj<PedidoExibicaoDto> pilhaPedidos = new PilhaObj<>(pedidosPendentes.size());

        pedidosPendentes.stream().map(PedidoMapper::ofPedidoExibicaoDto).forEach(pilhaPedidos::push);

        return pilhaPedidos;
    }

    public PedidoExibicaoDto criar(PedidoCriacaoDto pedidoCriacaoDto) {

        Aluno aluno = this.alunoService.buscarPorId(pedidoCriacaoDto.getAlunoId());
        Professor professor = this.professorService.buscarPorId(pedidoCriacaoDto.getProfessorId());
        Aula aula = this.aulaService.buscarPorId(pedidoCriacaoDto.getAulaId());
        Status status = this.statusService.buscarPorDescricao("Pendente");

        pedidoCriacaoDto.setDataAula(pedidoCriacaoDto.getDataAula().withSecond(0));

        Pedido pedido = this.repository.save(PedidoMapper.of(pedidoCriacaoDto, aluno, professor, status, aula));
        notificarProfessor("O aluno %s fez uma proposta de aula no dia %s às %s", pedido);
        notificarAluno("Você fez uma proposta de aula para %s no dia %s às %s", pedido);

        PedidoExibicaoDto pedidoExibicaoDto = PedidoMapper.ofPedidoExibicaoDto(pedido);
        webSocketService.adicionaNovoPedido(pedido.getProfessor().getId(), pedidoExibicaoDto);
        webSocketService.adicionaNovoPedido(pedido.getAluno().getId(), pedidoExibicaoDto);

        return PedidoMapper.ofPedidoExibicaoDto(pedido);
    }

    /* ============= PESQUISA ============== */

    public Pedido buscarPorId(Integer integer) {
        return repository.findById(integer)
                .orElseThrow(() -> new EntitadeNaoEncontradaException("Pedido não encontrado"));
    }

    public List<PedidoExibicaoDto> buscarPorUsuarioIdConfirmado(Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id);

        List<Pedido> pedidos = this.repository.buscarPorUsuarioIdConfirmado(usuario.getId());

        return pedidos.stream().map(PedidoMapper::ofPedidoExibicaoDto).toList();
    }

    public List<PedidoExibicaoDto> buscarPorUsuarioId(Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id);

        List<Pedido> pedidos = this.repository.buscarPorUsuarioId(usuario.getId());

        return pedidos.stream().map(PedidoMapper::ofPedidoExibicaoDto).toList();
    }

    public PedidoExibicaoDto buscarPorIdParaExibicao(Integer id) {
        Pedido pedido = this.buscarPorId(id);

        return PedidoMapper.ofPedidoExibicaoDto(pedido);
    }

    /* ============= ACEITAR / RECUSAR / CANCELAR / CONCLUIR ============== */
    public PedidoExibicaoDto aceitarPropostaDoAluno(PedidoAlteracaoStatus pedidoAlteracaoStatus) {
        Integer idPedido = pedidoAlteracaoStatus.getIdPedido();
        Integer idUsuario = pedidoAlteracaoStatus.getIdUsuario();
        String motivo = pedidoAlteracaoStatus.getMotivo();

        Pedido pedido = this.buscarPorId(idPedido);

        switch (pedido.getStatus().getDescricao()) {
            case "Confirmado" -> throw new EntitadeNaoEncontradaException("Pedido já confirmado");
            case "Recusado" -> throw new EntitadeNaoEncontradaException("Pedido já recusado");
            case "Cancelado" -> throw new EntitadeNaoEncontradaException("Pedido já cancelado");
            case "Aguardando Pagamento" -> throw new EntitadeNaoEncontradaException("Pedido já está aguardando pagamento");
        }

        this.hashTableService.atualizarStatusPedidoPorId(idPedido, pedido, "Aguardando Pagamento");
        pedido = atualizarStatus(pedido, "Aguardando Pagamento");

        notificarAluno("O professor aceitou sua proposta de aula no dia %s às %s", pedido);

        notificarProfessor("Você aceitou a proposta de aula do aluno %s no dia %s às %s", pedido);

        return PedidoMapper.ofPedidoExibicaoDto(pedido);
    }

    public PedidoExibicaoDto recusarPropostaDoAluno(PedidoAlteracaoStatus pedidoAlteracaoStatus) {
        Integer idPedido = pedidoAlteracaoStatus.getIdPedido();
        Integer idUsuario = pedidoAlteracaoStatus.getIdUsuario();
        String motivo = pedidoAlteracaoStatus.getMotivo();

        Pedido pedido = this.buscarPorId(idPedido);

        switch (pedido.getStatus().getDescricao()) {
            case "Recusado" -> throw new EntidadeConflitanteException("Pedido já recusado");
            case "Cancelado" -> throw new EntidadeConflitanteException("Pedido já cancelado");
            case "Aguardando Pagamento" ->
                    throw new EntidadeConflitanteException("Pedido já está aguardando pagamento");
        }

        this.hashTableService.atualizarStatusPedidoPorId(idPedido, pedido, "Recusado");
//        PedidoExibicaoDto pai = this.filaEsperaService.buscarPai(idPedido);
//        if (pai != null){
//            this.filaEsperaService.removerPrimeiroPedidoFilaEspera(idPedido);
//        }
        pedido = atualizarStatus(pedido, "Recusado");

        notificarAluno("O professor recusou sua proposta de aula no dia %s às %s", pedido);
        notificarProfessor("Você recusou a proposta de aula do aluno %s no dia %s às %s", pedido);

        return PedidoMapper.ofPedidoExibicaoDto(pedido);
    }

    public PedidoExibicaoDto cancelarPedido(PedidoAlteracaoStatus pedidoAlteracaoStatus) {
        Integer idPedido = pedidoAlteracaoStatus.getIdPedido();
        Integer idUsuario = pedidoAlteracaoStatus.getIdUsuario();
        String motivo = pedidoAlteracaoStatus.getMotivo();

        Pedido pedido = this.buscarPorId(idPedido);

        switch (pedido.getStatus().getDescricao()) {
            case "Cancelado" -> throw new EntidadeConflitanteException("Pedido já cancelado");
            case "Recusado" -> throw new EntidadeConflitanteException("Pedido já recusado");
        }

        this.hashTableService.atualizarStatusPedidoPorId(idPedido, pedido, "Cancelado");
//        PedidoExibicaoDto pai = this.filaEsperaService.buscarPai(idPedido);
//        if (pai != null){
//            this.filaEsperaService.removerPrimeiroPedidoFilaEspera(idPedido);
//        }
        pedido = atualizarStatus(pedido, "Cancelado");

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        if (usuario.getCategoria().equals("Aluno")) {
            notificacaoService.criarNotificacao(String.format(
                    "O aluno %s cancelou a aula no dia %s às %s",
                    pedido.getAluno().getNome(),
                    pedido.getDataAula().toLocalDate(),
                    pedido.getDataAula().toLocalTime().toString().substring(0, 5)
            ), "", pedido.getProfessor());

            notificacaoService.criarNotificacao(String.format(
                    "Você cancelou a aula com o professor %s no dia %s às %s",
                    pedido.getProfessor().getNome(),
                    pedido.getDataAula().toLocalDate(),
                    pedido.getDataAula().toLocalTime().toString().substring(0, 5)
            ), "", pedido.getAluno());
        } else {
            notificarAluno("O professor %s cancelou a aula no dia %s às %s", pedido);
            notificarProfessor("Você cancelou a aula com o aluno %s no dia %s às %s", pedido);
        }

        return PedidoMapper.ofPedidoExibicaoDto(pedido);
    }

    public PedidoExibicaoDto realizarPagamento(PedidoAlteracaoStatus pedidoAlteracaoStatus) {
        Integer idPedido = pedidoAlteracaoStatus.getIdPedido();
        Integer idUsuario = pedidoAlteracaoStatus.getIdUsuario();
        String motivo = pedidoAlteracaoStatus.getMotivo();

        Pedido pedido = this.buscarPorId(idPedido);

        switch (pedido.getStatus().getDescricao()) {
            case "Confirmado" -> throw new EntidadeConflitanteException("Pedido já confirmado");
            case "Recusado" -> throw new EntidadeConflitanteException("Pedido já recusado");
            case "Cancelado" -> throw new EntidadeConflitanteException("Pedido já cancelado");
            case "Concluído" -> throw new EntidadeConflitanteException("Pedido já concluído");
        }

        this.hashTableService.atualizarStatusPedidoPorId(idPedido, pedido, "Confirmado");
        pedido = atualizarStatus(pedido, "Confirmado");

        notificarProfessor("O aluno %s confirmou o pagamento da aula no dia %s às %s", pedido);
        notificarAluno("Você realizou o pagamento da aula com o professor %s", pedido);

        return PedidoMapper.ofPedidoExibicaoDto(pedido);
    }

    public PedidoExibicaoDto concluirPedidoPorId(PedidoAlteracaoStatus pedidoAlteracaoStatus) {
        Integer idPedido = pedidoAlteracaoStatus.getIdPedido();
        Integer idUsuario = pedidoAlteracaoStatus.getIdUsuario();
        String motivo = pedidoAlteracaoStatus.getMotivo();

        Pedido pedido = this.buscarPorId(idPedido);

        switch (pedido.getStatus().getDescricao()) {
            case "Recusado" -> throw new EntidadeConflitanteException("Pedido já recusado");
            case "Cancelado" -> throw new EntidadeConflitanteException("Pedido já cancelado");
            case "Concluído" -> throw new EntidadeConflitanteException("Pedido já concluído");
        }

        this.hashTableService.atualizarStatusPedidoPorId(idPedido, pedido, "Concluído");
        pedido = atualizarStatus(pedido, "Concluído");

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        if (usuario.getCategoria().equals("Aluno")) {
            notificarProfessor("O aluno %s concluiu a aula no dia %s às %s", pedido);
            notificarAluno("Você concluiu a aula com o professor %s no dia %s às %s", pedido);
        } else {
            notificarAluno("O professor %s concluiu a aula no dia %s às %s", pedido);
            notificarProfessor("Você concluiu a aula com o aluno %s no dia %s às %s", pedido);
        }

        return PedidoMapper.ofPedidoExibicaoDto(pedido);
    }

    private void notificarAluno(String mensagem, Pedido pedido) {
        notificacaoService.criarNotificacao(String.format(mensagem,
                pedido.getProfessor().getNome(),
                pedido.getDataAula().toLocalDate(),
                pedido.getDataAula().toLocalTime().toString().substring(0, 5)
                ), "", pedido.getAluno());
    }

    private void notificarProfessor(String mensagem, Pedido pedido) {
        notificacaoService.criarNotificacao(String.format(mensagem,
                pedido.getAluno().getNome(),
                pedido.getDataAula().toLocalDate(),
                pedido.getDataAula().toLocalTime().toString().substring(0, 5)
                ), "", pedido.getProfessor());
    }

    public Pedido atualizarStatus(Pedido pedido, String nomeStatus) {
        Status status = this.statusService.buscarPorDescricao(nomeStatus);

        pedido.setStatus(status);
        pedido.setHoraResposta(LocalDateTime.now());
//      Colocar para salvar mudança de status
        return this.repository.save(pedido);
    }

    public List<PedidoExibicaoDto> buscarAulasPorIdUsuarioEDataAulaConfirmado(int fkProfessor, LocalDateTime data) {
        List<Pedido> aulas = repository.findAllByUsuarioIdAndAulaDataConfirmado(fkProfessor, data);
        return aulas.stream().map(PedidoMapper::ofPedidoExibicaoDto).toList();
    }

    public List<PedidoExibicaoDto> buscarAulasPorIdUsuarioEDataAula(int fkProfessor, LocalDateTime data) {
        List<Pedido> aulas = repository.findAllByUsuarioIdAndAulaData(fkProfessor, data);
        return aulas.stream().map(PedidoMapper::ofPedidoExibicaoDto).toList();
    }

    public PedidoExibicaoDto buscarPorIdUsuarioEDataAula(int fkProfessor, LocalDateTime data) {
        Pedido aula = repository.findByUsuarioIdAndAulaData(fkProfessor, data);
        PedidoExibicaoDto pedido = PedidoMapper.ofPedidoExibicaoDto(aula);
        return pedido;
    }

    public List<PedidoExibicaoDto> buscarAulasPorIdUsuarioEMesAula(int fkUsuario, LocalDateTime localDateTime) {
        List<Pedido> aulas = repository.findAllByUsuarioIdAndAulaDataMes(fkUsuario, localDateTime);
        return aulas.stream().map(PedidoMapper::ofPedidoExibicaoDto).toList();
    }

    /* ============= METRICAS ADMIN ================ */

    public List<Integer> obterQuantidadePedidosRealizadosSemana() {
        List<Integer> valoresDaSemana = new ArrayList<>();
        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        LocalDateTime diaInicial = c.getTime().toInstant().atZone(c.getTimeZone().toZoneId()).toLocalDateTime();
        diaInicial = diaInicial.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime diaFinal = diaInicial.withHour(23).withMinute(59).withSecond(59);
        for (int i = 0; i < 7; i++) {

            valoresDaSemana.add(this.repository.obterQuantidadePedidosRealizadosDuranteDatas(diaInicial, diaFinal).orElse(0));
            c.add(Calendar.DATE, 1);
            diaInicial = c.getTime().toInstant().atZone(c.getTimeZone().toZoneId()).toLocalDateTime();
            diaInicial = diaInicial.withHour(0).withMinute(0).withSecond(0);
            diaFinal = diaInicial.withHour(23).withMinute(59).withSecond(59);
        }

        return valoresDaSemana;
    }

    public List<Integer> obterQuantidadePedidosPendentesSemana() {
        List<Integer> valoresDaSemana = new ArrayList<>();

        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        LocalDateTime diaInicial = c.getTime().toInstant().atZone(c.getTimeZone().toZoneId()).toLocalDateTime();

        diaInicial = diaInicial.withHour(0).withMinute(0).withSecond(0);

        LocalDateTime diaFinal = diaInicial.withHour(23).withMinute(59).withSecond(59);

        for (int i = 0; i < 7; i++) {

            valoresDaSemana.add(this.repository.obterQuantidadePedidosPendentesDuranteDatas(diaInicial, diaFinal));

            c.add(Calendar.DATE, 1);

            diaInicial = c.getTime().toInstant().atZone(c.getTimeZone().toZoneId()).toLocalDateTime();

            diaInicial = diaInicial.withHour(0).withMinute(0).withSecond(0);

            diaFinal = diaInicial.withHour(23).withMinute(59).withSecond(59);
        }

        return valoresDaSemana;
    }

    public List<Integer> obterQuantidadePedidosCanceladosSemana() {
        List<Integer> valoresDaSemana = new ArrayList<>();

        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        LocalDateTime diaInicial = c.getTime().toInstant().atZone(c.getTimeZone().toZoneId()).toLocalDateTime();

        diaInicial = diaInicial.withHour(0).withMinute(0).withSecond(0);

        LocalDateTime diaFinal = diaInicial.withHour(23).withMinute(59).withSecond(59);

        for (int i = 0; i < 7; i++) {

            valoresDaSemana.add(this.repository.obterQuantidadePedidosCanceladosDuranteDatas(diaInicial, diaFinal));

            c.add(Calendar.DATE, 1);

            diaInicial = c.getTime().toInstant().atZone(c.getTimeZone().toZoneId()).toLocalDateTime();

            diaInicial = diaInicial.withHour(0).withMinute(0).withSecond(0);

            diaFinal = diaInicial.withHour(23).withMinute(59).withSecond(59);
        }

        return valoresDaSemana;
    }

    public Integer obterQuantidadePedidosRealizadosTotalnaSemana() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        LocalDateTime diaInicial = c.getTime().toInstant().atZone(c.getTimeZone().toZoneId()).toLocalDateTime();
        diaInicial = diaInicial.withHour(0).withMinute(0).withSecond(0);

        LocalDateTime diaFinal = diaInicial.plusDays(6).withHour(23).withMinute(59).withSecond(59);

        return this.repository.obterQuantidadePedidosRealizadosDuranteDatas(diaInicial, diaFinal).orElse(0);
    }

    public Integer obterQuantidadePedidosPendentesTotalnaSemana() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        LocalDateTime diaInicial = c.getTime().toInstant().atZone(c.getTimeZone().toZoneId()).toLocalDateTime();
        diaInicial = diaInicial.withHour(0).withMinute(0).withSecond(0);

        LocalDateTime diaFinal = diaInicial.plusDays(6).withHour(23).withMinute(59).withSecond(59);

        return this.repository.obterQuantidadePedidosPendentesDuranteDatas(diaInicial, diaFinal);
    }

    public Integer obterQuantidadePedidosCanceladosTotalnaSemana() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        LocalDateTime diaInicial = c.getTime().toInstant().atZone(c.getTimeZone().toZoneId()).toLocalDateTime();
        diaInicial = diaInicial.withHour(0).withMinute(0).withSecond(0);

        c.add(Calendar.DATE, 6);

        LocalDateTime diaFinal = diaInicial.plusDays(6).withHour(23).withMinute(59).withSecond(59);

        return this.repository.obterQuantidadePedidosCanceladosDuranteDatas(diaInicial, diaFinal);
    }

    public Integer obterQuantidadePedidosTotalPeriodo(LocalDateTime dataComeco, LocalDateTime dataFim) {
        return this.repository.obterQuantidadePedidosDuranteDatas(dataComeco, dataFim);
    }

    public List<Double> obterRendimentoMesPorDia() {
        LocalDateTime dataComeco = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime dataFim = LocalDateTime.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        List<Double> valoresDoMes = new ArrayList<>();

        while (dataComeco.isBefore(dataFim)) {
            valoresDoMes.add(this.repository.obterRendimentoPeriodo(dataComeco, dataComeco.withHour(23).withMinute(59).withSecond(59)).orElse(0d));
            dataComeco = dataComeco.plusDays(1);
        }

        return valoresDoMes;
    }

    public List<Integer> obterQuantidadePedidoMesPorDia() {
        LocalDateTime dataComeco = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime dataFim = LocalDateTime.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        List<Integer> valoresDoMes = new ArrayList<>();

        while (dataComeco.isBefore(dataFim)) {
            valoresDoMes.add(this.repository.obterQuantidadePedidosRealizadosDuranteDatas(dataComeco, dataComeco.withHour(23).withMinute(59).withSecond(59)).orElse(0));
            dataComeco = dataComeco.plusDays(1);
        }

        return valoresDoMes;
    }

    public Page<PedidoExibicaoDto> obterTodosPedidosPorPaginaPeloIdUsuario(Integer idUsuario, Pageable pageable) {
        Page<Pedido> pedidos = this.repository.obterTodosPedidosPorPaginaPeloIdUsuario(idUsuario, pageable);
        return pedidos.map(PedidoMapper::ofPedidoExibicaoDto);
    }

    public Integer obterQuantidadePedidosTotal(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        if ((int) ChronoUnit.DAYS.between(dataInicial, dataFinal) > dataFinal.getMonth().length(dataFinal.toLocalDate().isLeapYear())) {
            dataInicial = dataInicial.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            dataFinal = dataFinal.withDayOfMonth(dataFinal.getMonth().length(dataFinal.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
        }

        return this.repository.obterQuantidadePedidosDuranteDatas(dataInicial, dataFinal);
    }
    public Double obterRendimentoProfessores(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        if ((int) ChronoUnit.DAYS.between(dataInicial, dataFinal) > dataFinal.getMonth().length(dataFinal.toLocalDate().isLeapYear())) {
            dataInicial = dataInicial.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            dataFinal = dataFinal.withDayOfMonth(dataFinal.getMonth().length(dataFinal.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
        }

        return this.repository.obterRendimentoProfessores(dataInicial, dataFinal).orElse(0d);
    }

    public Double obterPedidosPorAluno(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        Double quantidadePedidos, quantidadeUsuarios;

        if ((int) ChronoUnit.DAYS.between(dataInicial, dataFinal) > dataFinal.getMonth().length(dataFinal.toLocalDate().isLeapYear())) {
            dataInicial = dataInicial.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            dataFinal = dataFinal.withDayOfMonth(dataFinal.getMonth().length(dataFinal.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
        }

        quantidadePedidos = (double) this.repository.obterQuantidadePedidosRealizadosDuranteDatas(dataInicial, dataFinal).orElse(0);
        quantidadeUsuarios = (double) this.usuarioService.obterQuantidadeAlunosCadastradosEntre(dataInicial, dataFinal);

        if (quantidadeUsuarios == 0 || quantidadePedidos == 0) return 0d;
        return quantidadePedidos / quantidadeUsuarios;
    }

    public List<Integer> obterQuantidadeAulasHistorico(LocalDateTime dataInicial, LocalDateTime dataFinal, String status) {
        LocalDateTime aux = dataInicial;
        List<Integer> valores = new ArrayList<>();

        LocalDateTime primeiroDiaMes, ultimoDiaMes;
        while (aux.isBefore(dataFinal)) {
            if ((int) ChronoUnit.DAYS.between(dataInicial, dataFinal) > dataFinal.getMonth().length(dataFinal.toLocalDate().isLeapYear())) {
                primeiroDiaMes = aux.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                ultimoDiaMes = aux.withDayOfMonth(aux.getMonth().length(aux.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
                valores.add(this.repository.obterQuantidadeAulasHistorico(primeiroDiaMes, ultimoDiaMes, status).orElse(0));
                aux = aux.withDayOfMonth(1).plusMonths(1);
            } else {
                valores.add(this.repository.obterQuantidadeAulasHistorico(aux.withHour(0).withMinute(0).withSecond(0), aux.withHour(23).withMinute(59).withSecond(59), status).orElse(0));
                aux = aux.plusDays(1);
            }
        }


        return valores;
    }

    public Map<String, Long> obterInstrumentosMaisPedidos(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        if ((int) ChronoUnit.DAYS.between(dataInicial, dataFinal) > dataFinal.getMonth().length(dataFinal.toLocalDate().isLeapYear())) {
            LocalDateTime primeiroDiaMes = dataInicial.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime ultimoDiaMes = dataFinal.withDayOfMonth(dataFinal.getMonth().length(dataFinal.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
            return this.repository.obterQuantidadePedidosInstrumentoPorPeriodo(primeiroDiaMes, ultimoDiaMes).stream().collect(Collectors.toMap(o -> (String) ((Object[]) o)[0], o -> (Long) ((Object[]) o)[1]));
        } else {
            return this.repository.obterQuantidadePedidosInstrumentoPorPeriodo(dataInicial, dataFinal).stream().collect(Collectors.toMap(o -> (String) ((Object[]) o)[0], o -> (Long) ((Object[]) o)[1]));
        }
    }

    public Map<String, Long> obterRegioesMaisPedidos(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        if ((int) ChronoUnit.DAYS.between(dataInicial, dataFinal) > dataFinal.getMonth().length(dataFinal.toLocalDate().isLeapYear())) {
            LocalDateTime primeiroDiaMes = dataInicial.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime ultimoDiaMes = dataFinal.withDayOfMonth(dataFinal.getMonth().length(dataFinal.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
            return this.repository.obterQuantidadePedidosRegiaoPorPeriodo(primeiroDiaMes, ultimoDiaMes).stream().collect(Collectors.toMap(o -> (String) ((Object[]) o)[0], o -> (Long) ((Object[]) o)[1]));
        } else {
            return this.repository.obterQuantidadePedidosRegiaoPorPeriodo(dataInicial, dataFinal).stream().collect(Collectors.toMap(o -> (String) ((Object[]) o)[0], o -> (Long) ((Object[]) o)[1]));
        }
    }
}
