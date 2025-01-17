package group.vvv.controllers;

import group.vvv.services.ViagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class PaginaInicialWebController {

    @Autowired
    private ViagemService viagemService;

    @GetMapping("/paginaInicial")
    public String exibirPaginaInicial(Model model) {
        model.addAttribute("viagens", viagemService.getViagens());
        return "paginaInicial";
    }
}