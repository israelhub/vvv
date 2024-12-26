package group.vvv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import group.vvv.models.viagem.Viagem;
import group.vvv.models.viagem.Local;
import group.vvv.services.ViagemService;
import group.vvv.repositories.LocalRepository;
import group.vvv.repositories.CidadeRepository;

import java.util.List;

@Controller
@RequestMapping("/web/viagens")
public class ViagemWebController {

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("viagem", new Viagem());
        model.addAttribute("cidades", cidadeRepository.findAll());
        return "viagem/areaCadastroViagem";
    }

    @PostMapping
    public String cadastrarViagemWeb(@RequestParam Long origemLocal, @RequestParam Long destinoLocal, @RequestParam(required = false) Long escalaLocal, Model model) {
        try {
            Viagem novaViagem = viagemService.criarViagem(origemLocal, destinoLocal, escalaLocal);
            model.addAttribute("mensagem", "Viagem cadastrada com sucesso! ID: " + novaViagem.getId_viagem());
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao cadastrar viagem: " + e.getMessage());
        }
        return "viagem/areaCadastroViagem";
    }
}