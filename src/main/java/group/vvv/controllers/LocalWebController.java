package group.vvv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import group.vvv.models.viagem.*;
import group.vvv.services.LocalService;

@Controller
@RequestMapping("/web/locais")
public class LocalWebController {

    @Autowired
    private LocalService localService;

    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("local", new Local());
        model.addAttribute("cidades", localService.getCidades());
        return "viagem/areaCadastroLocal";
    }

    @PostMapping
    public String cadastrarLocalWeb(@RequestParam String nomeCidade, @RequestParam String codigoCidade,
                                    @RequestParam String tipo, @RequestParam String nomeInfraestrutura,
                                    @RequestParam(required = false) String codigoAeroporto, Model model) {
        try {
            Cidade cidade = new Cidade();
            cidade.setNome(nomeCidade);
            cidade.setCodigo(codigoCidade);
            localService.cadastrarCidade(cidade);

            Local local = new Local();
            local.setId_cidade(cidade);

            switch (tipo) {
                case "aeroporto":
                    Aeroporto aeroporto = new Aeroporto();
                    aeroporto.setNome(nomeInfraestrutura);
                    aeroporto.setCodigo(Integer.parseInt(codigoAeroporto));
                    localService.cadastrarAeroporto(aeroporto);
                    local.setId_aeroporto(aeroporto);
                    break;
                case "estacao":
                    Estacao estacao = new Estacao();
                    estacao.setNome(nomeInfraestrutura);
                    localService.cadastrarEstacao(estacao);
                    local.setId_estacao(estacao);
                    break;
                case "porto":
                    Porto porto = new Porto();
                    porto.setNome(nomeInfraestrutura);
                    localService.cadastrarPorto(porto);
                    local.setId_porto(porto);
                    break;
            }

            localService.cadastrar(local);
            model.addAttribute("mensagem", "Local cadastrado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao cadastrar local: " + e.getMessage());
        }
        return "viagem/areaCadastroLocal";
    }
}