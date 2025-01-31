package group.vvv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import group.vvv.config.FuncionarioSession;
import group.vvv.models.Funcionario;
import group.vvv.models.viagem.Modal;
import group.vvv.services.ModalService;
import group.vvv.services.TransportadoraService;

@Controller
@RequestMapping("/web/administracao/modal")
public class ModalController {

    @Autowired
    private ModalService modalService;

    @Autowired
    private FuncionarioSession funcionarioSession;

    @Autowired
    private TransportadoraService transportadoraService;

    @GetMapping
    public String listarModais(Model model, RedirectAttributes ra) {
        if (!verificarPermissao(ra)) {
            return "redirect:/web/administracao";
        }
        model.addAttribute("modais", modalService.getModais());
        return "admin/modal-list";
    }

    @GetMapping("/novo")
    public String modalForm(Model model, RedirectAttributes ra) {
        if (!verificarPermissao(ra)) {
            return "redirect:/web/administracao";
        }
        model.addAttribute("modal", new Modal());
        model.addAttribute("transportadoras", transportadoraService.listarTodas());
        return "admin/modal-form";
    }

    @PostMapping
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

    @GetMapping("/editar/{id}")
    public String editarModal(@PathVariable Long id, Model model, RedirectAttributes ra) {
        if (!verificarPermissao(ra)) {
            return "redirect:/web/administracao";
        }
        
        Modal modal = modalService.getModalById(id);
        model.addAttribute("modal", modal);
        model.addAttribute("transportadoras", transportadoraService.listarTodas());
        return "admin/modal-form";
    }

    @PostMapping("/editar/{id}")
    public String atualizarModal(@PathVariable Long id, @ModelAttribute Modal modal, RedirectAttributes ra) {
        try {
            modal.setId_modal(id); // Garante que o ID está correto
            modalService.cadastrar(modal);
            ra.addFlashAttribute("mensagem", "Modal atualizado com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao atualizar modal: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/modal";
    }

    @DeleteMapping("/{id}")
    public String deletarModal(@PathVariable Long id, RedirectAttributes ra) {
        try {
            modalService.deletar(id);
            ra.addFlashAttribute("mensagem", "Modal excluído com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao excluir modal: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/modal";
    }

    private boolean verificarPermissao(RedirectAttributes ra) {
        if (funcionarioSession == null || funcionarioSession.getFuncionario() == null ||
                funcionarioSession.getFuncionario().getCargo() != Funcionario.Cargo.GERENTE
                        && funcionarioSession.getFuncionario().getCargo() != Funcionario.Cargo.PADRAO) {
            ra.addFlashAttribute("mensagem", "Só o Gerente e o Funcionário podem acessar essa página");
            ra.addFlashAttribute("tipoMensagem", "error");
            return false;
        }
        return true;
    }
}