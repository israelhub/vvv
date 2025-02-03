package group.vvv.controllers;

import group.vvv.models.viagem.Viagem;
import group.vvv.services.ViagemService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web")
public class PaginaInicialController {

    @Autowired
    private ViagemService viagemService;

    @GetMapping("/paginaInicial")
    public String paginaInicial(
            @RequestParam(required = false) String origem,
            @RequestParam(required = false) String destino,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate data,
            Model model) {
        
        List<Viagem> viagens;
        if (origem != null || destino != null || data != null) {
            viagens = viagemService.buscarViagens(origem, destino, data);
        } else {
            viagens = viagemService.getViagensDisponiveis();
        }
        
        model.addAttribute("viagens", viagens);
        return "paginaInicial";
    }
}