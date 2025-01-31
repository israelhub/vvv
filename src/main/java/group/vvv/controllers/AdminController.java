package group.vvv.controllers;

import group.vvv.config.FuncionarioSession;
import group.vvv.models.Funcionario;
import group.vvv.models.PontoDeVenda;
import group.vvv.models.PontoFuncionario;
import group.vvv.models.Reserva;
import group.vvv.models.viagem.Aeroporto;
import group.vvv.models.viagem.Cidade;
import group.vvv.models.viagem.Estacao;
import group.vvv.models.viagem.Local;
import group.vvv.models.viagem.Modal;
import group.vvv.models.viagem.Porto;
import group.vvv.services.FuncionarioService;
import group.vvv.services.LocalService;
import group.vvv.services.ModalService;
import group.vvv.services.PontoDeVendaService;
import group.vvv.services.ReservaService;

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
    private PontoDeVendaService pontoDeVendaService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private FuncionarioSession funcionarioSession;

    @GetMapping
    public String index() {
        return "admin/layout";
    }

    // Ponto de Venda
    @GetMapping("/ponto-de-venda")
    public String pontoVendaForm(Model model, RedirectAttributes ra) {
        if (funcionarioSession == null || funcionarioSession.getFuncionario() == null ||
                funcionarioSession.getFuncionario().getCargo() != Funcionario.Cargo.GERENTE &&
                        funcionarioSession.getFuncionario().getCargo() != Funcionario.Cargo.PADRAO) {
            ra.addFlashAttribute("mensagem", "Só o Gerente e o Funcionário pode acessar essa página");
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao";
        }
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
    public String reservasPendentes(Model model, RedirectAttributes ra) {
        if (funcionarioSession.getFuncionario().getCargo() != Funcionario.Cargo.GERENTE_DE_NEGOCIOS_VIRTUAIS) {
            ra.addFlashAttribute("mensagem", "Só o Gerente de Negócios Virtuais pode acessar essa página");
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao";
        }
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

    // Cadastro de Funcionario

    @GetMapping("/funcionario")
    public String funcionarioForm(Model model, RedirectAttributes ra) {
        if (funcionarioSession == null || funcionarioSession.getFuncionario() == null ||
                funcionarioSession.getFuncionario().getCargo() != Funcionario.Cargo.GERENTE) {
            ra.addFlashAttribute("mensagem", "Só o Gerente pode acessar essa página");
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao";
        }

        model.addAttribute("pontosDeVenda", pontoDeVendaService.listarTodos());
        return "admin/cadastroFuncionario-form";
    }

    @PostMapping("/funcionario")
    public String cadastrarFuncionario(@ModelAttribute Funcionario funcionario,
            @RequestParam(required = false) List<Long> pontoDeVenda,
            @RequestParam(required = false) List<String> diaSemana,
            @RequestParam(required = false) List<LocalTime> horarioInicial,
            @RequestParam(required = false) List<LocalTime> horarioFinal,
            RedirectAttributes ra) {
        try {
            Funcionario novoFuncionario = funcionarioService.cadastrar(funcionario);

            if (pontoDeVenda != null && diaSemana != null &&
                    horarioInicial != null && horarioFinal != null) {

                int index = 0;
                for (int i = 0; i < pontoDeVenda.size(); i++) {
                    Long idPonto = pontoDeVenda.get(i);

                    int diasCount = 0;
                    while (index + diasCount < diaSemana.size() &&
                            diaSemana.get(index + diasCount) != null &&
                            !diaSemana.get(index + diasCount).isEmpty()) {
                        diasCount++;
                    }

                    for (int j = 0; j < diasCount; j++) {
                        String dia = diaSemana.get(index + j);
                        LocalTime horaInicial = horarioInicial.get(index + j);
                        LocalTime horaFinal = horarioFinal.get(index + j);

                        if (idPonto != null && dia != null && !dia.isEmpty() &&
                                horaInicial != null && horaFinal != null) {

                            PontoFuncionario pontoFuncionario = new PontoFuncionario();
                            PontoFuncionario.PontoFuncionarioId id = new PontoFuncionario.PontoFuncionarioId(
                                    novoFuncionario.getId_funcionario(),
                                    idPonto,
                                    PontoFuncionario.DiaSemana.valueOf(dia.toUpperCase()));
                            pontoFuncionario.setId(id);
                            pontoFuncionario.setFuncionario(novoFuncionario);
                            pontoFuncionario.setPontoDeVenda(pontoDeVendaService.buscarPorId(idPonto));
                            pontoFuncionario.setHorarioInicial(horaInicial);
                            pontoFuncionario.setHorarioFinal(horaFinal);

                            funcionarioService.cadastrarPontoFuncionario(pontoFuncionario);
                        }
                    }
                    index += diasCount;
                }
            }

            ra.addFlashAttribute("mensagem",
                    "Funcionário cadastrado com sucesso! Senha inicial: " + novoFuncionario.getSenha());
            ra.addFlashAttribute("tipoMensagem", "success");

        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao cadastrar funcionário: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/funcionario";
    }
}