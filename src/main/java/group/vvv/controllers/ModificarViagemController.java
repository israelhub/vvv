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
