package group.vvv.controllers;

import group.vvv.config.UserSession;
import group.vvv.models.Cartao;
import group.vvv.models.Cartao.TipoCartao;
import group.vvv.models.Cliente;
import group.vvv.models.Passageiro;
import group.vvv.models.Reserva;
import group.vvv.models.Reserva.StatusReserva;
import group.vvv.models.ReservaPassageiro;
import group.vvv.models.viagem.Viagem;
import group.vvv.services.PagamentoService;
import group.vvv.services.CartaoService;
import group.vvv.services.PassageiroService;
import group.vvv.services.ReservaService;
import group.vvv.services.TicketService;
import group.vvv.services.ViagemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/viagens")
public class ViagemWebController {

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private PassageiroService passageiroService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UserSession userSession;

    @Autowired
    private CartaoService cartaoService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping("/detalhes/{id}")
    public String mostrarDetalhesViagem(@PathVariable Long id,
            @RequestParam(defaultValue = "0") int passageirosNormal,
            @RequestParam(defaultValue = "0") int passageirosCrianca,
            Model model) {
        Viagem viagem = viagemService.getViagemById(id);
        BigDecimal valorTotal = calcularValorTotal(viagem, passageirosNormal, passageirosCrianca);

        model.addAttribute("viagem", viagem);
        model.addAttribute("passageirosNormal", passageirosNormal);
        model.addAttribute("passageirosCrianca", passageirosCrianca);
        model.addAttribute("valorTotal", valorTotal);

        return "viagem/detalhesViagem";
    }

    private BigDecimal calcularValorTotal(Viagem viagem, int passageirosNormal, int passageirosCrianca) {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add(viagem.getValor().multiply(new BigDecimal(passageirosNormal)));
        total = total
                .add(viagem.getValor().multiply(new BigDecimal("0.6"))
                        .multiply(new BigDecimal(passageirosCrianca)));
        return total;
    }

    @GetMapping("/reservar/{id}")
    public String exibirFormularioCadastroPassageiros(
            @PathVariable Long id,
            @RequestParam int passageirosNormal,
            @RequestParam int passageirosCrianca,
            Model model) {

        if (!userSession.isAuthenticated()) {
            return "redirect:/web/clientes/login";
        }

        Viagem viagem = viagemService.getViagemById(id);
        model.addAttribute("viagem", viagem);
        model.addAttribute("passageirosNormal", passageirosNormal);
        model.addAttribute("passageirosCrianca", passageirosCrianca);
        return "reservaCliente/cadastroPassageiros";
    }

    @PostMapping("/reservar/{id}/passageiros")
    public String processarPassageiros(
            @PathVariable Long id,
            @RequestParam("nome[]") String[] nomes,
            @RequestParam("data_nascimento[]") String[] datasNascimento,
            @RequestParam("cpf[]") String[] cpfs,
            @RequestParam("telefone[]") String[] telefones,
            @RequestParam("profissao[]") String[] profissoes,
            Model model) {

        List<Passageiro> passageiros = new ArrayList<>();

        try {
            for (int i = 0; i < nomes.length; i++) {
                Passageiro passageiro = new Passageiro();
                passageiro.setNome(nomes[i]);
                passageiro.setData_nascimento(Date.valueOf(datasNascimento[i]));
                passageiro.setCpf(cpfs[i]);
                passageiro.setTelefone(telefones[i]);
                passageiro.setProfissao(profissoes[i]);

                passageiroService.salvarPassageiro(passageiro);
                passageiros.add(passageiro);
            }

            List<Passageiro> criancas = passageiros.stream()
                    .filter(p -> p.getIdade() >= 2 && p.getIdade() <= 10)
                    .collect(Collectors.toList());

            List<Passageiro> adultosResponsaveis = passageiros.stream()
                    .filter(p -> p.getIdade() >= 21)
                    .collect(Collectors.toList());

            if (!criancas.isEmpty()) {
                if (adultosResponsaveis.isEmpty()) {
                    model.addAttribute("erro",
                            "Para viajar com crianças (2-10 anos) é necessário haver pelo menos um passageiro com 21 anos ou mais como responsável.");
                    model.addAttribute("viagem", viagemService.getViagemById(id));
                    return "reservaCliente/cadastroPassageiros";
                }
                model.addAttribute("criancas", criancas);
                model.addAttribute("adultos", adultosResponsaveis);
                model.addAttribute("passageiros", passageiros);
                model.addAttribute("viagemId", id);
                return "reservaCliente/associarResponsaveisCliente";
            }

            // Se não houver crianças, criar reserva diretamente
            Viagem viagem = viagemService.getViagemById(id);
            viagem.setNumReservasAssociadas(viagem.getNumReservasAssociadas() + 1);

            Reserva reserva = criarReserva(viagem, passageiros, userSession.getCliente());
            reservaService.salvarReserva(reserva);

            associarPassageirosReserva(reserva, passageiros);

            viagemService.atualizarViagem(viagem);

            return "redirect:/web/viagens/reserva/" + reserva.getId_reserva() + "/pagamento";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao processar passageiros: " + e.getMessage());
            model.addAttribute("viagem", viagemService.getViagemById(id));
            return "reservaCliente/cadastroPassageiros";
        }
    }

    @PostMapping("/reservar/{id}/responsaveis")
    public String processarResponsaveis(
            @PathVariable Long id,
            @RequestParam(value = "passageiroIds", required = true) String passageiroIdsStr,
            @RequestParam(required = false) Map<String, String> responsaveis,
            RedirectAttributes redirectAttributes) {

        try {
            List<Long> passageiroIds = Arrays.stream(passageiroIdsStr.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            if (passageiroIds.isEmpty()) {
                throw new RuntimeException("Lista de passageiros não pode estar vazia");
            }

            Viagem viagem = viagemService.getViagemById(id);
            viagem.setNumReservasAssociadas(viagem.getNumReservasAssociadas() + 1);

            List<Passageiro> passageiros = passageiroIds.stream()
                    .map(pid -> passageiroService.getPassageiroById(pid))
                    .collect(Collectors.toList());

            if (responsaveis != null) {
                processarResponsaveisPassageiros(responsaveis, passageiroService);
            }

            Reserva reserva = criarReserva(viagem, passageiros, userSession.getCliente());
            reservaService.salvarReserva(reserva);

            associarPassageirosReserva(reserva, passageiros);

            viagemService.atualizarViagem(viagem);

            return "redirect:/web/viagens/reserva/" + reserva.getId_reserva() + "/pagamento";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Erro ao processar reserva: " + e.getMessage());
            return "redirect:/web/viagens/detalhes/" + id;
        }
    }

    @GetMapping("/reserva/sucesso")
    public String mostrarPaginaSucesso() {
        return "reservaCliente/reservaSucesso";
    }

    private void processarResponsaveisPassageiros(Map<String, String> responsaveis,
            PassageiroService passageiroService) {
        responsaveis.forEach((key, value) -> {
            if (key.startsWith("responsaveis[")) {
                Long idCrianca = Long.parseLong(key.replaceAll("responsaveis\\[(\\d+)\\]", "$1"));
                Long idResponsavel = Long.parseLong(value);

                Passageiro crianca = passageiroService.getPassageiroById(idCrianca);
                Passageiro responsavel = passageiroService.getPassageiroById(idResponsavel);

                crianca.setResponsavel(responsavel);
                passageiroService.salvarPassageiro(crianca);
            }
        });
    }

    private Reserva criarReserva(Viagem viagem, List<Passageiro> passageiros, Cliente cliente) {
        Reserva reserva = new Reserva();
        reserva.setViagem(viagem);
        reserva.setData(new Date(System.currentTimeMillis()));
        reserva.setStatus(Reserva.StatusReserva.PENDENTE_PAGAMENTO);
        reserva.setValorTotal(calcularValorTotal(viagem, passageiros));
        reserva.setOrigem(viagem.getOrigem().getDescricaoCompleta());
        reserva.setDestino(viagem.getDestino().getDescricaoCompleta());
        reserva.setCliente(cliente);
        return reserva;
    }

    private void associarPassageirosReserva(Reserva reserva, List<Passageiro> passageiros) {
        for (Passageiro passageiro : passageiros) {
            ReservaPassageiro reservaPassageiro = new ReservaPassageiro();
            reservaPassageiro.setId(new ReservaPassageiro.ReservaPassageiroId(
                    reserva.getId_reserva(),
                    passageiro.getId_passageiro()));
            reservaPassageiro.setReserva(reserva);
            reservaPassageiro.setPassageiro(passageiro);
            reservaService.salvarReservaPassageiro(reservaPassageiro);
        }
    }

    private BigDecimal calcularValorTotal(Viagem viagem, List<Passageiro> passageiros) {
        BigDecimal total = BigDecimal.ZERO;
        for (Passageiro passageiro : passageiros) {
            if (passageiro.getIdade() >= 2 && passageiro.getIdade() <= 10) {
                total = total.add(viagem.getValor().multiply(new BigDecimal("0.6")));
            } else {
                total = total.add(viagem.getValor());
            }
        }
        return total;
    }

    @GetMapping("/reserva/{id}/pagamento")
    public String exibirPaginaPagamento(@PathVariable Long id, Model model) {
        Reserva reserva = reservaService.getReservaById(id);
        model.addAttribute("reserva", reserva);
        return "reservaCliente/pagamentoCliente";
    }

    @PostMapping("/reserva/pagamento")
    public String processarPagamento(
            @RequestParam Long reservaId,
            @RequestParam String numero,
            @RequestParam String cvv,
            @RequestParam String validade,
            @RequestParam String nomeTitular,
            @RequestParam TipoCartao tipoCartao,
            @RequestParam(defaultValue = "1") Integer numParcelas,
            RedirectAttributes redirectAttributes) {

        try {
            // Salva o cartão
            Cliente cliente = userSession.getCliente();
            Cartao cartao = cartaoService.salvarCartao(numero, cvv, validade, nomeTitular, tipoCartao, cliente);

            // Cria o pagamento e as parcelas
            Reserva reserva = reservaService.getReservaById(reservaId);
            pagamentoService.criarPagamento(reserva, cartao, numParcelas);

            // Atualiza status da reserva
            reserva.setStatus(StatusReserva.PENDENTE_AO_GERENTE_DE_NEGOCIOS_VIRTUAIS);
            reservaService.salvarReserva(reserva);

            // Gera os tickets
            ticketService.gerarTickets(reserva);

            redirectAttributes.addFlashAttribute("mensagemSucesso",
                    "Pagamento processado com sucesso! Aguardando aprovação.");
            return "redirect:/web/viagens/reserva/sucesso";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Erro ao processar pagamento: " + e.getMessage());
            return "redirect:/web/viagens/reserva/" + reservaId + "/pagamento";
        }
    }
}