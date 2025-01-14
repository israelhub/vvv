package group.vvv.controllers;

import group.vvv.config.UserSession;
import group.vvv.models.Cliente;
import group.vvv.models.Passageiro;
import group.vvv.models.Reserva;
import group.vvv.models.ReservaPassageiro;
import group.vvv.models.viagem.Viagem;
import group.vvv.services.LocalService;
import group.vvv.services.ModalService;
import group.vvv.services.PassageiroService;
import group.vvv.services.ReservaService;
import group.vvv.services.ViagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;
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
    private LocalService localService;

    @Autowired
    private ModalService modalService;

    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("viagem", new Viagem());
        model.addAttribute("locais", localService.getLocais());
        model.addAttribute("modais", modalService.getModais());
        return "viagem/areaCadastroViagem";
    }

    @PostMapping
    public String cadastrarViagemWeb(@RequestParam Long origemLocal,
            @RequestParam Long destinoLocal,
            @RequestParam(required = false) List<Long> escalaLocal,
            @RequestParam Long modalOrigem,
            @RequestParam(required = false) List<Long> modalEscala,
            @RequestParam LocalTime horarioPartida,
            @RequestParam LocalTime horarioChegada,
            @RequestParam Date dataPartida,
            @RequestParam Date dataChegada,
            @RequestParam BigDecimal valor,
            Model model) {
        try {
            Viagem novaViagem = viagemService.criarViagem(origemLocal, destinoLocal, escalaLocal,
                    modalOrigem, modalEscala,
                    horarioPartida, horarioChegada,
                    dataPartida, dataChegada, valor);
            model.addAttribute("mensagem", "Viagem cadastrada com sucesso! ID: " + novaViagem.getId_viagem());
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao cadastrar viagem: " + e.getMessage());
        }
        model.addAttribute("locais", localService.getLocais());
        model.addAttribute("modais", modalService.getModais());
        return "viagem/areaCadastroViagem";
    }

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
                .add(viagem.getValor().multiply(new BigDecimal("0.6")).multiply(new BigDecimal(passageirosCrianca)));
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
        return "viagem/cadastroPassageiros";
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
            // Criar e salvar passageiros
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

            // Separar crianças e adultos
            List<Passageiro> criancas = passageiros.stream()
                    .filter(p -> p.getIdade() >= 2 && p.getIdade() <= 10)
                    .collect(Collectors.toList());

            List<Passageiro> adultosResponsaveis = passageiros.stream()
                    .filter(p -> p.getIdade() >= 21)
                    .collect(Collectors.toList());

            // Se houver crianças, validar responsáveis
            if (!criancas.isEmpty()) {
                if (adultosResponsaveis.isEmpty()) {
                    model.addAttribute("erro",
                            "Para viajar com crianças (2-10 anos) é necessário haver pelo menos um passageiro com 21 anos ou mais como responsável.");
                    model.addAttribute("viagem", viagemService.getViagemById(id));
                    return "viagem/cadastroPassageiros";
                }
                // Redirecionar para associar responsáveis
                model.addAttribute("criancas", criancas);
                model.addAttribute("adultos", adultosResponsaveis);
                model.addAttribute("passageiros", passageiros);
                model.addAttribute("viagemId", id);
                return "viagem/associarResponsaveis";
            } else {
                // Se não houver crianças, criar reserva diretamente
                Viagem viagem = viagemService.getViagemById(id);
                viagem.setNumReservasAssociadas(viagem.getNumReservasAssociadas() + 1);

                // Criar e salvar a reserva
                Reserva reserva = criarReserva(viagem, passageiros, userSession.getCliente());
                reservaService.salvarReserva(reserva);

                // Associar passageiros à reserva
                associarPassageirosReserva(reserva, passageiros);

                // Atualizar viagem
                viagemService.atualizarViagem(viagem);

                return "redirect:/web/viagens/reserva/sucesso";
            }

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao processar passageiros: " + e.getMessage());
            model.addAttribute("viagem", viagemService.getViagemById(id));
            return "viagem/cadastroPassageiros";
        }
    }

    @PostMapping("/reservar/{id}/responsaveis")
    public String processarResponsaveis(
            @PathVariable Long id,
            @RequestParam(value = "passageiroIds", required = true) String passageiroIdsStr,
            @RequestParam(required = false) Map<String, String> responsaveis,
            RedirectAttributes redirectAttributes) {

        try {
            // Converte string de IDs em List<Long>
            List<Long> passageiroIds = Arrays.stream(passageiroIdsStr.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            if (passageiroIds.isEmpty()) {
                throw new RuntimeException("Lista de passageiros não pode estar vazia");
            }

            // Recupera viagem e incrementa número de reservas
            Viagem viagem = viagemService.getViagemById(id);
            viagem.setNumReservasAssociadas(viagem.getNumReservasAssociadas() + 1);

            // Recupera todos os passageiros
            List<Passageiro> passageiros = passageiroIds.stream()
                    .map(pid -> passageiroService.getPassageiroById(pid))
                    .collect(Collectors.toList());

            // Processa responsáveis se houver
            if (responsaveis != null) {
                processarResponsaveisPassageiros(responsaveis, passageiroService);
            }

            // Criar e salvar a reserva
            Reserva reserva = criarReserva(viagem, passageiros, userSession.getCliente());
            reservaService.salvarReserva(reserva);

            // Associa os passageiros à reserva
            associarPassageirosReserva(reserva, passageiros);

            // Atualiza a viagem com o novo número de reservas
            viagemService.atualizarViagem(viagem);

            redirectAttributes.addFlashAttribute("mensagemSucesso",
                    "Reserva realizada com sucesso! Código da reserva: " + reserva.getId_reserva());
            return "redirect:/web/viagens/reserva/sucesso";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Erro ao processar reserva: " + e.getMessage());
            return "redirect:/web/viagens/detalhes/" + id;
        }
    }

    @GetMapping("/reserva/sucesso")
    public String mostrarPaginaSucesso() {
        return "viagem/reservaSucesso";
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
        reserva.setStatus("CONFIRMADA");
        reserva.setValor(calcularValorTotal(viagem, passageiros));
        reserva.setOrigem(viagem.getOrigemLocal().getLocal().getDescricaoCompleta());
        reserva.setDestino(viagem.getDestinoLocal().getLocal().getDescricaoCompleta());
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
}