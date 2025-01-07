package group.vvv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import group.vvv.models.PontoDeVenda;
import group.vvv.services.PontoDeVendaService;
import group.vvv.services.FuncionarioService;

@Controller
@RequestMapping("/web/pontos-de-venda")
public class PontoDeVendaWebController {

    @Autowired
    private PontoDeVendaService pontoDeVendaService;

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("pontoDeVenda", new PontoDeVenda());
        model.addAttribute("gerentes", funcionarioService.listarGerentes());
        return "areaCadastroPontoDeVenda";
    }

    @PostMapping
    public String cadastrarPontoDeVendaWeb(@ModelAttribute PontoDeVenda pontoDeVenda, Model model) {
        try {
            pontoDeVendaService.cadastrar(pontoDeVenda);
            model.addAttribute("mensagem", "Ponto de venda cadastrado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("mensagem", "Erro ao cadastrar: " + e.getMessage());
        }
        return "areaCadastroPontoDeVenda";
    }
}