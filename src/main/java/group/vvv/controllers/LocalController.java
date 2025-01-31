package group.vvv.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import group.vvv.config.FuncionarioSession;
import group.vvv.models.Funcionario;
import group.vvv.models.viagem.Local;
import group.vvv.models.viagem.Cidade;
import group.vvv.models.viagem.Aeroporto;
import group.vvv.models.viagem.Estacao;
import group.vvv.models.viagem.Porto;
import group.vvv.services.LocalService;

@Controller
@RequestMapping("/web/administracao/local")
public class LocalController {

    @Autowired
    private LocalService localService;

    @Autowired
    private FuncionarioSession funcionarioSession;

    @GetMapping
    public String listarLocais(Model model, RedirectAttributes ra) {
        if (!verificarPermissao(ra)) {
            return "redirect:/web/administracao";
        }
        model.addAttribute("locais", localService.getLocais());
        return "admin/local-list";
    }

    @GetMapping("/novo")
    public String localForm(Model model, RedirectAttributes ra) {
        if (!verificarPermissao(ra)) {
            return "redirect:/web/administracao";
        }
        model.addAttribute("editando", false);
        return "admin/local-form";
    }

    @PostMapping
    public String cadastrarLocal(
            @RequestParam String nomeCidade,
            @RequestParam String codigoCidade, 
            @RequestParam String tipo,
            @RequestParam String nomeInfraestrutura,
            @RequestParam(required = false) String codigoAeroporto,
            RedirectAttributes ra) {
        try {
            Local local = new Local();
            Cidade cidade = new Cidade();
            cidade.setNome(nomeCidade);
            cidade.setCodigo(codigoCidade);
            localService.cadastrarCidade(cidade);
            
            local.setId_cidade(cidade);
            atualizarDadosLocal(local, nomeCidade, codigoCidade, tipo, nomeInfraestrutura, codigoAeroporto);
            localService.cadastrar(local);
            
            ra.addFlashAttribute("mensagem", "Local cadastrado com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao cadastrar local: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/local";
    }

    @GetMapping("/editar/{id}")
    public String editarLocal(@PathVariable Long id, Model model, RedirectAttributes ra) {
        if (!verificarPermissao(ra)) {
            return "redirect:/web/administracao";
        }
        Local local = localService.getLocalById(id);
        model.addAttribute("local", local);
        model.addAttribute("editando", true);

        String tipo;
        if (local.getId_aeroporto() != null) {
            tipo = "aeroporto";
        } else if (local.getId_estacao() != null) {
            tipo = "estacao";
        } else if (local.getId_porto() != null) {
            tipo = "porto";
        } else {
            tipo = "";
        }
        model.addAttribute("tipoLocal", tipo);

        return "admin/local-form";
    }

    @PostMapping("/editar/{id}")
    public String atualizarLocal(@PathVariable Long id,
            @RequestParam String nomeCidade,
            @RequestParam String codigoCidade,
            @RequestParam String tipo,
            @RequestParam String nomeInfraestrutura,
            @RequestParam(required = false) String codigoAeroporto,
            RedirectAttributes ra) {
        try {
            Local local = localService.getLocalById(id);
            atualizarDadosLocal(local, nomeCidade, codigoCidade, tipo, nomeInfraestrutura, codigoAeroporto);
            localService.cadastrar(local);
            ra.addFlashAttribute("mensagem", "Local atualizado com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao atualizar local: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/local";
    }

    @DeleteMapping("/{id}")
    public String deletarLocal(@PathVariable Long id, RedirectAttributes ra) {
        try {
            localService.deletar(id);
            ra.addFlashAttribute("mensagem", "Local excluído com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao excluir local: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/local";
    }

    private boolean verificarPermissao(RedirectAttributes ra) {
        if (funcionarioSession.getFuncionario() == null ||
                funcionarioSession.getFuncionario().getCargo() != Funcionario.Cargo.GERENTE) {
            ra.addFlashAttribute("mensagem", "Acesso negado. Apenas gerentes podem gerenciar locais.");
            ra.addFlashAttribute("tipoMensagem", "error");
            return false;
        }
        return true;
    }

    private void atualizarDadosLocal(Local local, String nomeCidade, String codigoCidade,
            String tipo, String nomeInfraestrutura, String codigoAeroporto) {
        Cidade cidade = local.getId_cidade();
        cidade.setNome(nomeCidade);
        cidade.setCodigo(codigoCidade);

        switch (tipo) {
            case "aeroporto":
                Aeroporto aeroporto = local.getId_aeroporto();
                if (aeroporto == null) {
                    aeroporto = new Aeroporto();
                }
                aeroporto.setNome(nomeInfraestrutura);
                aeroporto.setCodigo(Integer.parseInt(codigoAeroporto));
                local.setId_aeroporto(aeroporto);
                local.setId_estacao(null);
                local.setId_porto(null);
                break;
            case "estacao":
                Estacao estacao = local.getId_estacao();
                if (estacao == null) {
                    estacao = new Estacao();
                }
                estacao.setNome(nomeInfraestrutura);
                local.setId_estacao(estacao);
                local.setId_aeroporto(null);
                local.setId_porto(null);
                break;
            case "porto":
                Porto porto = local.getId_porto();
                if (porto == null) {
                    porto = new Porto();
                }
                porto.setNome(nomeInfraestrutura);
                local.setId_porto(porto);
                local.setId_aeroporto(null);
                local.setId_estacao(null);
                break;
        }
    }
}