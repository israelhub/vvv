package group.vvv.controllers;

import group.vvv.config.FuncionarioSession;
import group.vvv.models.Funcionario;
import group.vvv.models.viagem.*;
import group.vvv.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/web/administracao/viagem")
public class ModificarViagemController {

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private LocalService localService;

    @Autowired
    private ModalService modalService;

    @Autowired
    private FuncionarioSession funcionarioSession;

    @GetMapping
    public String exibirFormulario(Model model, RedirectAttributes ra) {
        if (!verificarPermissao(ra)) {
            return "redirect:/web/administracao";
        }

        prepararModelParaFormulario(model);
        model.addAttribute("editando", false); // Adicionar flag editando
        model.addAttribute("viagem", new Viagem()); // Adicionar viagem vazia
        return "admin/viagem-form";
    }

    @PostMapping
    public String cadastrarViagem(
            @RequestParam Long origemId,
            @RequestParam Long destinoId,
            @RequestParam Long modalId,
            @RequestParam LocalDateTime horarioPartida,
            @RequestParam LocalDateTime horarioChegada,
            @RequestParam String valor,
            @RequestParam(required = false) List<Long> escalaOrigem,
            @RequestParam(required = false) List<Long> escalaDestino,
            @RequestParam(required = false) List<Long> escalaModal,
            @RequestParam(required = false) List<LocalDateTime> escalaHorarioPartida,
            @RequestParam(required = false) List<LocalDateTime> escalaHorarioChegada,
            RedirectAttributes ra) {

        try {
            Viagem viagem = new Viagem();
            viagem.setOrigem(localService.getLocalById(origemId));
            viagem.setDestino(localService.getLocalById(destinoId));
            viagem.setModal(modalService.getModalById(modalId));
            viagem.setHorarioPartida(horarioPartida);
            viagem.setHorarioChegada(horarioChegada);
            viagem.setValor(new java.math.BigDecimal(valor));
            viagem.setNumReservasAssociadas(0);

            List<Escala> escalas = new ArrayList<>();
            if (escalaOrigem != null) {
                for (int i = 0; i < escalaOrigem.size(); i++) {
                    Escala escala = new Escala();
                    escala.setViagem(viagem);
                    escala.setOrigem(localService.getLocalById(escalaOrigem.get(i)));
                    escala.setDestino(localService.getLocalById(escalaDestino.get(i)));
                    escala.setModal(modalService.getModalById(escalaModal.get(i)));
                    escala.setHorarioPartida(escalaHorarioPartida.get(i));
                    escala.setHorarioChegada(escalaHorarioChegada.get(i));
                    escala.setOrdem(i + 1);
                    escalas.add(escala);
                }
            }
            viagem.setEscalas(escalas);

            viagemService.salvarViagem(viagem);

            ra.addFlashAttribute("mensagem", "Viagem cadastrada com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
            return "redirect:/web/administracao/viagem";

        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao cadastrar viagem: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao/viagem";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarViagem(@PathVariable Long id, Model model, RedirectAttributes ra) {
        if (!verificarPermissao(ra)) {
            return "redirect:/web/administracao";
        }

        try {
            Viagem viagem = viagemService.getViagemById(id);
            prepararModelParaFormulario(model);
            model.addAttribute("viagem", viagem);
            model.addAttribute("editando", true);
            return "admin/viagem-form";
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao buscar viagem: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao/viagem/lista";
        }
    }

    @PostMapping("/editar/{id}")
    public String atualizarViagem(
            @PathVariable Long id,
            @RequestParam Long origemId,
            @RequestParam Long destinoId,
            @RequestParam Long modalId,
            @RequestParam LocalDateTime horarioPartida,
            @RequestParam LocalDateTime horarioChegada,
            @RequestParam String valor,
            @RequestParam(required = false) List<Long> escalaOrigem,
            @RequestParam(required = false) List<Long> escalaDestino,
            @RequestParam(required = false) List<Long> escalaModal,
            @RequestParam(required = false) List<LocalDateTime> escalaHorarioPartida,
            @RequestParam(required = false) List<LocalDateTime> escalaHorarioChegada,
            RedirectAttributes ra, Model model) {

        try {
            Viagem viagem = viagemService.getViagemById(id);
            viagem.setOrigem(localService.getLocalById(origemId));
            viagem.setDestino(localService.getLocalById(destinoId));
            viagem.setModal(modalService.getModalById(modalId));
            viagem.setHorarioPartida(horarioPartida);
            viagem.setHorarioChegada(horarioChegada);
            viagem.setValor(new BigDecimal(valor));

            List<Escala> escalas = new ArrayList<>();
            if (escalaOrigem != null && escalaDestino != null && escalaModal != null
                    && escalaHorarioPartida != null && escalaHorarioChegada != null) {

                for (int i = 0; i < escalaOrigem.size(); i++) {
                    Escala escala = new Escala();
                    escala.setViagem(viagem);
                    escala.setOrigem(localService.getLocalById(escalaOrigem.get(i)));
                    escala.setDestino(localService.getLocalById(escalaDestino.get(i)));
                    escala.setModal(modalService.getModalById(escalaModal.get(i)));
                    escala.setHorarioPartida(escalaHorarioPartida.get(i));
                    escala.setHorarioChegada(escalaHorarioChegada.get(i));
                    escala.setOrdem(i + 1);
                    escalas.add(escala);
                }
            }
            viagem.setEscalas(escalas);

            viagemService.salvarViagem(viagem);

            ra.addFlashAttribute("mensagem", "Viagem atualizada com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");

            ra.addAttribute("activePage", "-lista");
            return "redirect:/web/administracao/viagem/lista";

        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao atualizar viagem: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao/viagem/editar/" + id;
        }
    }

    @DeleteMapping("/{id}")
    public String deletarViagem(@PathVariable Long id, RedirectAttributes ra) {
        if (!verificarPermissao(ra)) {
            return "redirect:/web/administracao";
        }

        try {
            viagemService.deletarViagem(id);
            ra.addFlashAttribute("mensagem", "Viagem excluída com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao excluir viagem: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/viagem/lista";
    }

    @GetMapping("/lista")
    public String listarViagens(Model model, RedirectAttributes ra) {
        if (!verificarPermissao(ra)) {
            return "redirect:/web/administracao";
        }

        model.addAttribute("viagens", viagemService.getViagens());
        return "admin/viagem-list";
    }

    private boolean verificarPermissao(RedirectAttributes ra) {
        if (funcionarioSession.getFuncionario() == null ||
                funcionarioSession.getFuncionario().getCargo() != Funcionario.Cargo.GERENTE) {
            ra.addFlashAttribute("mensagem", "Acesso negado. Apenas gerentes podem cadastrar viagens.");
            ra.addFlashAttribute("tipoMensagem", "error");
            return false;
        }
        return true;
    }

    private void prepararModelParaFormulario(Model model) {
        model.addAttribute("locais", localService.getLocais());
        model.addAttribute("modais", modalService.getModais());
    }
}
