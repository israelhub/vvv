package group.vvv.controllers;

import group.vvv.models.PontoDeVenda;
import group.vvv.models.Reserva;
import group.vvv.models.viagem.Aeroporto;
import group.vvv.models.viagem.Cidade;
import group.vvv.models.viagem.Estacao;
import group.vvv.models.viagem.Local;
import group.vvv.models.viagem.Modal;
import group.vvv.models.viagem.Porto;
import group.vvv.models.viagem.Viagem;
import group.vvv.services.FuncionarioService;
import group.vvv.services.LocalService;
import group.vvv.services.ModalService;
import group.vvv.services.PontoDeVendaService;
import group.vvv.services.ReservaService;
import group.vvv.services.ViagemService;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/administracao")
public class AdminController {

    @Autowired
    private ModalService modalService;

    @Autowired
    private LocalService localService;

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private PontoDeVendaService pontoDeVendaService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public String index() {
        return "admin/layout";
    }

    // Modal
    @GetMapping("/modal")
    public String modalForm(Model model) {
        model.addAttribute("modal", new Modal());
        return "admin/modal-form";
    }

    @PostMapping("/modal")
    public String salvarModal(@ModelAttribute Modal modal, RedirectAttributes ra) {
        try {
            modalService.cadastrar(modal);
            ra.addFlashAttribute("mensagem", "Modal cadastrado com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao cadastrar modal: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/modal";
    }

    // Local
    @GetMapping("/local")
    public String localForm(Model model) {
        model.addAttribute("local", new Local());
        model.addAttribute("cidades", localService.getCidades());
        return "admin/local-form";
    }

    @PostMapping("/local")
    public String cadastrarLocal(@RequestParam String nomeCidade,
            @RequestParam String codigoCidade,
            @RequestParam String tipo,
            @RequestParam String nomeInfraestrutura,
            @RequestParam(required = false) String codigoAeroporto,
            RedirectAttributes ra) {
        try {

            Cidade cidade = new Cidade();
            cidade.setNome(nomeCidade);
            cidade.setCodigo(codigoCidade);
            localService.cadastrarCidade(cidade);

            Local local = new Local();
            local.setId_cidade(cidade);

            switch (tipo) {
                case "aeroporto":
                    Aeroporto aeroporto = new Aeroporto();
                    aeroporto.setNome(nomeInfraestrutura);
                    aeroporto.setCodigo(Integer.parseInt(codigoAeroporto));
                    localService.cadastrarAeroporto(aeroporto);
                    local.setId_aeroporto(aeroporto);
                    break;
                case "estacao":
                    Estacao estacao = new Estacao();
                    estacao.setNome(nomeInfraestrutura);
                    localService.cadastrarEstacao(estacao);
                    local.setId_estacao(estacao);
                    break;
                case "porto":
                    Porto porto = new Porto();
                    porto.setNome(nomeInfraestrutura);
                    localService.cadastrarPorto(porto);
                    local.setId_porto(porto);
                    break;
            }

            localService.cadastrar(local);

            ra.addFlashAttribute("mensagem", "Local cadastrado com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao cadastrar local: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/local";
    }

    // Viagem
    @GetMapping("/viagem")
    public String viagemForm(Model model) {
        model.addAttribute("viagem", new Viagem());
        model.addAttribute("locais", localService.getLocais());
        model.addAttribute("modais", modalService.getModais());
        return "admin/viagem-form";
    }

    @PostMapping("/viagem")
    public String cadastrarViagem(@RequestParam Long origemLocal,
            @RequestParam Long destinoLocal,
            @RequestParam(required = false) List<Long> escalaLocal,
            @RequestParam Long modalOrigem,
            @RequestParam(required = false) List<Long> modalEscala,
            @RequestParam LocalTime horarioPartida,
            @RequestParam LocalTime horarioChegada,
            @RequestParam Date dataPartida,
            @RequestParam Date dataChegada,
            @RequestParam BigDecimal valor,
            RedirectAttributes ra) {
        try {
            // Usando o viagemService para criar a viagem
            Viagem novaViagem = viagemService.criarViagem(origemLocal, destinoLocal, escalaLocal,
                    modalOrigem, modalEscala,
                    horarioPartida, horarioChegada,
                    dataPartida, dataChegada, valor);

            ra.addFlashAttribute("mensagem", "Viagem cadastrada com sucesso! ID: " + novaViagem.getId_viagem());
            ra.addFlashAttribute("tipoMensagem", "success");

        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao cadastrar viagem: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/viagem";
    }

    // Ponto de Venda
    @GetMapping("/ponto-de-venda")
    public String pontoVendaForm(Model model) {
        model.addAttribute("pontoDeVenda", new PontoDeVenda());
        model.addAttribute("gerentes", funcionarioService.listarGerentes());
        return "admin/pontoDeVenda-form";
    }

    @PostMapping("/ponto-de-venda")
    public String cadastrarPontoVenda(@ModelAttribute PontoDeVenda pontoDeVenda, RedirectAttributes ra) {
        try {
            pontoDeVendaService.cadastrar(pontoDeVenda);
            ra.addFlashAttribute("mensagem", "Ponto de venda cadastrado com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao cadastrar ponto de venda: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/ponto-de-venda";
    }

    // Aprovação de Vendas Online

    @GetMapping("/reservas-pendentes")
    public String listarReservasPendentes(Model model) {
        model.addAttribute("reservas", reservaService.getReservasPendentesAprovacao());
        return "admin/aprovacaoVendaOnline-form";
    }

    @GetMapping("/reservas/{id}")
    public String verDetalhesReserva(@PathVariable Long id, Model model) {
        Reserva reserva = reservaService.getReservaById(id);
        model.addAttribute("reserva", reserva);
        model.addAttribute("passageiros", reservaService.getPassageiros(reserva));
        return "admin/detalhesReserva-form";
    }

    @PostMapping("/reservas/{id}/confirmar")
    public String confirmarReserva(@PathVariable Long id, RedirectAttributes ra) {
        try {
            reservaService.confirmarReserva(id);
            ra.addFlashAttribute("mensagem", "Reserva confirmada com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao confirmar reserva: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/reservas-pendentes";
    }
}