package group.vvv.controllers;

import group.vvv.config.FuncionarioSession;
import group.vvv.models.Funcionario;
import group.vvv.models.PontoFuncionario;
import group.vvv.models.Reserva;
import group.vvv.models.Reserva.StatusReserva;
import group.vvv.services.FuncionarioService;
import group.vvv.services.PontoDeVendaService;
import group.vvv.services.ReservaService;

import java.time.LocalTime;
import java.util.ArrayList;
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

    // Gerenciar Funcionario

    @GetMapping("/funcionario") 
    public String funcionarioForm(Model model, RedirectAttributes ra) {
        if (funcionarioSession == null || funcionarioSession.getFuncionario() == null ||
                funcionarioSession.getFuncionario().getCargo() != Funcionario.Cargo.GERENTE) {
            ra.addFlashAttribute("mensagem", "Só o Gerente pode acessar essa página");
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao";
        }
    
        model.addAttribute("editando", false);
        model.addAttribute("funcionario", new Funcionario());
        model.addAttribute("pontosDeVenda", pontoDeVendaService.listarTodos());
        return "admin/funcionario-form";
    }

    @PostMapping("/funcionario")
    public String cadastrarFuncionario(@ModelAttribute Funcionario funcionario,
            @RequestParam(required = false) List<Long> pontoDeVenda,
            @RequestParam(required = false) List<String> diaSemana,
            @RequestParam(required = false) List<LocalTime> horarioInicial,
            @RequestParam(required = false) List<LocalTime> horarioFinal,
            RedirectAttributes ra) {
        try {
            // Validar número máximo de pontos de venda
            if (pontoDeVenda != null && pontoDeVenda.size() > 2) {
                ra.addFlashAttribute("mensagem", "Máximo de 2 pontos de venda permitidos");
                ra.addFlashAttribute("tipoMensagem", "error");
                return "redirect:/web/administracao/funcionario";
            }
    
            Funcionario novoFuncionario = funcionarioService.cadastrar(funcionario);
    
            if (pontoDeVenda != null && diaSemana != null && 
                horarioInicial != null && horarioFinal != null) {
    
                // Processar primeiro ponto de venda (obrigatório)
                Long idPonto = pontoDeVenda.get(0);
                processarPontoDeVenda(novoFuncionario, idPonto, diaSemana, horarioInicial, horarioFinal, 0);
    
                // Processar segundo ponto de venda (opcional)
                if (pontoDeVenda.size() > 1 && pontoDeVenda.get(1) != null && !pontoDeVenda.get(1).toString().isEmpty()) {
                    int primeirosPontoDias = contarDiasPonto(diaSemana, 0);
                    processarPontoDeVenda(novoFuncionario, pontoDeVenda.get(1), diaSemana, horarioInicial, horarioFinal, primeirosPontoDias);
                }
            }
    
            ra.addFlashAttribute("mensagem", "Funcionário cadastrado com sucesso! Senha inicial: " + novoFuncionario.getSenha());
            ra.addFlashAttribute("tipoMensagem", "success");
            return "redirect:/web/administracao/funcionario/lista";
    
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao cadastrar funcionário: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao/funcionario";
        }
    }
    
    private void processarPontoDeVenda(Funcionario funcionario, Long idPonto, 
            List<String> diaSemana, List<LocalTime> horarioInicial, 
            List<LocalTime> horarioFinal, int startIndex) {
        
        int diasCount = contarDiasPonto(diaSemana, startIndex);
    
        for (int j = 0; j < diasCount; j++) {
            String dia = diaSemana.get(startIndex + j);
            if (dia != null && !dia.isEmpty()) {
                LocalTime horaInicial = horarioInicial.get(startIndex + j);
                LocalTime horaFinal = horarioFinal.get(startIndex + j);
    
                PontoFuncionario pontoFuncionario = new PontoFuncionario();
                PontoFuncionario.PontoFuncionarioId id = new PontoFuncionario.PontoFuncionarioId(
                        funcionario.getId_funcionario(),
                        idPonto,
                        PontoFuncionario.DiaSemana.valueOf(dia.toUpperCase()));
    
                pontoFuncionario.setId(id);
                pontoFuncionario.setFuncionario(funcionario);
                pontoFuncionario.setPontoDeVenda(pontoDeVendaService.buscarPorId(idPonto));
                pontoFuncionario.setHorarioInicial(horaInicial);
                pontoFuncionario.setHorarioFinal(horaFinal);
    
                funcionarioService.cadastrarPontoFuncionario(pontoFuncionario);
            }
        }
    }
    
    private int contarDiasPonto(List<String> diaSemana, int startIndex) {
        int count = 0;
        while (startIndex + count < diaSemana.size() && 
               diaSemana.get(startIndex + count) != null && 
               !diaSemana.get(startIndex + count).isEmpty()) {
            count++;
        }
        return count;
    }

    @GetMapping("/funcionario/lista")
    public String listarFuncionarios(Model model, RedirectAttributes ra) {
        if (funcionarioSession.getFuncionario().getCargo() != Funcionario.Cargo.GERENTE) {
            ra.addFlashAttribute("mensagem", "Só o Gerente pode acessar essa página");
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao";
        }
        model.addAttribute("funcionarios", funcionarioService.listarTodos());
        return "admin/funcionario-list";
    }

    private List<PontoFuncionario> criarPontosFuncionario(
            Funcionario funcionario,
            List<Long> pontoDeVenda,
            List<String> diaSemana,
            List<LocalTime> horarioInicial,
            List<LocalTime> horarioFinal) {

        List<PontoFuncionario> pontos = new ArrayList<>();

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
                    PontoFuncionario pf = new PontoFuncionario();
                    PontoFuncionario.PontoFuncionarioId id = new PontoFuncionario.PontoFuncionarioId(
                            funcionario.getId_funcionario(),
                            idPonto,
                            PontoFuncionario.DiaSemana.valueOf(diaSemana.get(index + j).toUpperCase()));

                    pf.setId(id);
                    pf.setFuncionario(funcionario);
                    pf.setPontoDeVenda(pontoDeVendaService.buscarPorId(idPonto));
                    pf.setHorarioInicial(horarioInicial.get(index + j));
                    pf.setHorarioFinal(horarioFinal.get(index + j));

                    pontos.add(pf);
                }
                index += diasCount;
            }
        }

        return pontos;
    }

    @GetMapping("/funcionario/editar/{id}")
    public String editarFuncionario(@PathVariable Long id, Model model, RedirectAttributes ra) {
        if (funcionarioSession.getFuncionario().getCargo() != Funcionario.Cargo.GERENTE) {
            ra.addFlashAttribute("mensagem", "Só o Gerente pode editar funcionários");
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao";
        }
    
        try {
            Funcionario funcionario = funcionarioService.buscarPorId(id);
            List<PontoFuncionario> pontosFuncionario = funcionarioService.buscarPontosFuncionario(id);
    
            model.addAttribute("funcionario", funcionario);
            model.addAttribute("pontosFuncionario", pontosFuncionario);
            model.addAttribute("pontosDeVenda", pontoDeVendaService.listarTodos());
            model.addAttribute("editando", true);
    
            return "admin/funcionario-form";
    
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao buscar funcionário: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao/funcionario/lista";
        }
    }

    @PostMapping("/funcionario/editar/{id}") 
    public String atualizarFuncionario(@PathVariable Long id,
            @ModelAttribute Funcionario funcionario,
            @RequestParam(required = false) List<Long> pontoDeVenda,
            @RequestParam(required = false) List<String> diaSemana,
            @RequestParam(required = false) List<LocalTime> horarioInicial,
            @RequestParam(required = false) List<LocalTime> horarioFinal,
            RedirectAttributes ra) {
        try {
            // Validar número máximo de pontos de venda
            if (pontoDeVenda != null && pontoDeVenda.size() > 2) {
                ra.addFlashAttribute("mensagem", "Máximo de 2 pontos de venda permitidos");
                ra.addFlashAttribute("tipoMensagem", "error");
                return "redirect:/web/administracao/funcionario/editar/" + id;
            }
    
            Funcionario funcionarioExistente = funcionarioService.buscarPorId(id);
            
            funcionarioExistente.setNome(funcionario.getNome());
            funcionarioExistente.setEmail(funcionario.getEmail());
            funcionarioExistente.setCargo(funcionario.getCargo());
    
            funcionarioService.atualizarDados(funcionarioExistente);
    
            // Remover pontos existentes
            funcionarioService.removerPontosFuncionario(id);
    
            if (pontoDeVenda != null && diaSemana != null && 
                horarioInicial != null && horarioFinal != null) {
    
                // Processar primeiro ponto de venda (obrigatório)
                Long idPonto = pontoDeVenda.get(0);
                processarPontoDeVenda(funcionarioExistente, idPonto, diaSemana, horarioInicial, horarioFinal, 0);
    
                // Processar segundo ponto de venda (opcional) 
                if (pontoDeVenda.size() > 1 && pontoDeVenda.get(1) != null && !pontoDeVenda.get(1).toString().isEmpty()) {
                    int primeirosPontoDias = contarDiasPonto(diaSemana, 0);
                    processarPontoDeVenda(funcionarioExistente, pontoDeVenda.get(1), diaSemana, horarioInicial, horarioFinal, primeirosPontoDias);
                }
            }
    
            ra.addFlashAttribute("mensagem", "Funcionário atualizado com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
            return "redirect:/web/administracao/funcionario/lista";
    
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao atualizar funcionário: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao/funcionario/editar/" + id;
        }
    }

    @DeleteMapping("/funcionario/{id}")
    public String deletarFuncionario(@PathVariable Long id, RedirectAttributes ra) {
        try {
            funcionarioService.deletar(id);
            ra.addFlashAttribute("mensagem", "Funcionário excluído com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao excluir funcionário: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/funcionario/lista";
    }
}
