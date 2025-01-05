package group.vvv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import group.vvv.models.viagem.Modal;
import group.vvv.services.ModalService;

@Controller
@RequestMapping("/web/modal")
public class ModalWebController {

    @Autowired
    private ModalService modalService;

    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("modal", new Modal());
        return "areaCadastroModal";
    }

    @PostMapping
    public String cadastrarModalWeb(@ModelAttribute Modal modal, Model model) {
        modalService.cadastrar(modal);
        model.addAttribute("mensagem", "Modal cadastrado com sucesso!");
        return "areaCadastroModal";
    }

}
