package group.vvv.controllers;

import group.vvv.models.viagem.Viagem;
import group.vvv.services.ViagemService;

import java.util.ArrayList;
import java.util.List;

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
    public String paginaInicial(Model model) {
        List<Viagem> viagens = viagemService.getViagens();
        model.addAttribute("viagens", viagens);
        return "paginaInicial";
    }
}