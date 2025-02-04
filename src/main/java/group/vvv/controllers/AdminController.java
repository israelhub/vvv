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
            Funcionario novoFuncionario = funcionarioService.cadastrar(funcionario);
    
            if (pontoDeVenda != null && diaSemana != null && horarioInicial != null && horarioFinal != null) {
                int currentIndex = 0;
                
                for (int i = 0; i < pontoDeVenda.size(); i++) {
                    Long idPonto = pontoDeVenda.get(i);
                    
                    // Conta quantos dias consecutivos não nulos existem para este ponto
                    int diasCount = 0;
                    while (currentIndex + diasCount < diaSemana.size() && 
                           diaSemana.get(currentIndex + diasCount) != null && 
                           !diaSemana.get(currentIndex + diasCount).isEmpty()) {
                        diasCount++;
                    }
    
                    // Para cada dia deste ponto de venda
                    for (int j = 0; j < diasCount; j++) {
                        String dia = diaSemana.get(currentIndex + j);
                        LocalTime horaInicial = horarioInicial.get(currentIndex + j);
                        LocalTime horaFinal = horarioFinal.get(currentIndex + j);
    
                        PontoFuncionario pontoFuncionario = new PontoFuncionario();
                        PontoFuncionario.PontoFuncionarioId id = new PontoFuncionario.PontoFuncionarioId(
                                novoFuncionario.getId_funcionario(),
                                idPonto,
                                PontoFuncionario.DiaSemana.valueOf(dia));
    
                        pontoFuncionario.setId(id);
                        pontoFuncionario.setFuncionario(novoFuncionario);
                        pontoFuncionario.setPontoDeVenda(pontoDeVendaService.buscarPorId(idPonto));
                        pontoFuncionario.setHorarioInicial(horaInicial);
                        pontoFuncionario.setHorarioFinal(horaFinal);
    
                        funcionarioService.cadastrarPontoFuncionario(pontoFuncionario);
                    }
                    
                    // Atualiza o índice para o próximo conjunto de dias
                    currentIndex += diasCount;
                    // Pula um índice extra para separar os conjuntos de dias de diferentes pontos
                    currentIndex++;
                }
            }
    
            ra.addFlashAttribute("mensagem", 
                    "Funcionário cadastrado com sucesso! Senha inicial: " + novoFuncionario.getSenha());
            ra.addFlashAttribute("tipoMensagem", "success");
            
            return "redirect:/web/administracao/funcionario/lista";
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao cadastrar funcionário: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao/funcionario";
        }
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
            // Buscar funcionário existente
            Funcionario funcionarioExistente = funcionarioService.buscarPorId(id);
            
            // Atualizar dados básicos
            funcionarioExistente.setNome(funcionario.getNome());
            funcionarioExistente.setEmail(funcionario.getEmail());
            funcionarioExistente.setCargo(funcionario.getCargo());
    
            // Atualizar funcionário
            funcionarioService.atualizarDados(funcionarioExistente);
    
            // Remover pontos existentes
            funcionarioService.removerPontosFuncionario(id);
    
            // Criar e salvar novos pontos
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
                            PontoFuncionario.PontoFuncionarioId pontoId = new PontoFuncionario.PontoFuncionarioId(
                                    funcionarioExistente.getId_funcionario(),
                                    idPonto,
                                    PontoFuncionario.DiaSemana.valueOf(dia.toUpperCase()));
                            
                            pontoFuncionario.setId(pontoId);
                            pontoFuncionario.setFuncionario(funcionarioExistente);
                            pontoFuncionario.setPontoDeVenda(pontoDeVendaService.buscarPorId(idPonto));
                            pontoFuncionario.setHorarioInicial(horaInicial);
                            pontoFuncionario.setHorarioFinal(horaFinal);
    
                            funcionarioService.cadastrarPontoFuncionario(pontoFuncionario);
                        }
                    }
                    index += diasCount;
                }
            }
    
            ra.addFlashAttribute("mensagem", "Funcionário atualizado com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao atualizar funcionário: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/funcionario/lista";
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
