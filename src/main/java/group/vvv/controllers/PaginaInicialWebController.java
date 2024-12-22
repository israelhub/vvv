package group.vvv.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class PaginaInicialWebController {

    @GetMapping("/paginaInicial")
    public String exibirPaginaInicial() {
        return "paginaInicial";
    }
}