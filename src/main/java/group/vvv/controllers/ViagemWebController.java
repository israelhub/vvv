package group.vvv.controllers;

import group.vvv.models.viagem.Viagem;
import group.vvv.services.LocalService;
import group.vvv.services.ModalService;
import group.vvv.services.ViagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web/viagens")
public class ViagemWebController {

    @Autowired
    private ViagemService viagemService;

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
    public String cadastrarViagemWeb(@RequestParam Long origemLocal, @RequestParam Long destinoLocal,
                                     @RequestParam(required = false) List<Long> escalaLocal,
                                     @RequestParam Long modalOrigem,
                                     @RequestParam(required = false) List<Long> modalEscala, Model model) {
        try {
            Viagem novaViagem = viagemService.criarViagem(origemLocal, destinoLocal, escalaLocal, modalOrigem, modalEscala);
            model.addAttribute("mensagem", "Viagem cadastrada com sucesso! ID: " + novaViagem.getId_viagem());
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao cadastrar viagem: " + e.getMessage());
        }
        model.addAttribute("locais", localService.getLocais());
        model.addAttribute("modais", modalService.getModais());
        return "viagem/areaCadastroViagem";
    }
}