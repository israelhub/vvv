package group.vvv.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import group.vvv.config.FuncionarioSession;
import group.vvv.models.viagem.Viagem;
import group.vvv.services.ViagemService;

@Controller
@RequestMapping("/web/pontos-de-venda/viagens")
public class ViagensPresenciaisController {

    @Autowired
    private ViagemService viagemService;
    
    @Autowired
    private FuncionarioSession funcionarioSession;

    @GetMapping
    public String listarViagens(Model model, RedirectAttributes ra) {
        if (funcionarioSession.getFuncionario() == null) {
            ra.addFlashAttribute("mensagem", "Funcionário precisa estar logado");
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/funcionarios/login";
        }

        List<Viagem> viagens = viagemService.getViagens();
        model.addAttribute("viagens", viagens);
        return "reservaAdmin/viagensDisponiveis";
    }
}