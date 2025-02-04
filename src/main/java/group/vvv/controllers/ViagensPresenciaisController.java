package group.vvv.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String listarViagens(
            @RequestParam(required = false) String origem,
            @RequestParam(required = false) String destino,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            Model model, 
            RedirectAttributes ra) {

        if (funcionarioSession.getFuncionario() == null) {
            ra.addFlashAttribute("mensagem", "Funcionário precisa estar logado");
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/funcionarios/login";
        }

        List<Viagem> viagens;
        if (origem != null || destino != null || data != null) {
            viagens = viagemService.buscarViagens(origem, destino, data);
        } else {
            viagens = viagemService.getViagens();
        }

        model.addAttribute("viagens", viagens);
        return "reservaAdmin/viagensDisponiveis";
    }
}