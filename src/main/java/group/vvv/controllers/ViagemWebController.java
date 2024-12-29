package group.vvv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import group.vvv.models.viagem.*;
import group.vvv.services.ViagemService;
import group.vvv.services.ApiService;
import group.vvv.repositories.*;

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

    @Autowired
    private AeroportoRepository aeroportoRepository;

    @Autowired
    private PortoRepository portoRepository;

    @Autowired
    private EstacaoRepository estacaoRepository;

    @Autowired
    private ApiService apiService;

    @GetMapping("/novo")
    public String exibirFormularioCadastro(
            @RequestParam(required = false) String estadoSigla,
            @RequestParam(required = false) String cidadeNome,
            Model model) {

        model.addAttribute("viagem", new Viagem());
        model.addAttribute("estados", apiService.getEstados());
        /*
         * model.addAttribute("aeroportos",
         * apiService.getAeroportosPorCidade(estadoSigla));
         * model.addAttribute("portos", apiService.getPortos());
         * model.addAttribute("estacoes", apiService.getEstacoes());
         */
        return "viagem/areaCadastroViagem";
    }

    @PostMapping
    public String cadastrarViagemWeb(@RequestParam Long origemLocal, @RequestParam Long destinoLocal,
            @RequestParam(required = false) List<Long> escalaLocal, Model model) {
        try {
            Viagem novaViagem = viagemService.criarViagem(origemLocal, destinoLocal, escalaLocal);
            model.addAttribute("mensagem", "Viagem cadastrada com sucesso! ID: " + novaViagem.getId_viagem());
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao cadastrar viagem: " + e.getMessage());
        }
        return "viagem/areaCadastroViagem";
    }
}