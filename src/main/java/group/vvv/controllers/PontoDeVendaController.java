package group.vvv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import group.vvv.config.FuncionarioSession;
import group.vvv.models.Funcionario;
import group.vvv.models.PontoDeVenda;
import group.vvv.services.FuncionarioService;
import group.vvv.services.PontoDeVendaService;

@Controller
@RequestMapping("/web/administracao/ponto-de-venda")
public class PontoDeVendaController {

    @Autowired
    private PontoDeVendaService pontoDeVendaService;

    @Autowired
    private FuncionarioSession funcionarioSession;

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping("/novo") // Alterado de "/ponto-de-venda" para "/novo"
    public String pontoVendaForm(Model model, RedirectAttributes ra) {
        if (!temPermissao()) {
            return redirecionarSemPermissao(ra);
        }
        model.addAttribute("pontoDeVenda", new PontoDeVenda());
        model.addAttribute("gerentes", funcionarioService.listarGerentes());
        model.addAttribute("editando", false); // Adicionado
        return "admin/pontoDeVenda-form";
    }

    @PostMapping
    public String cadastrarPontoVenda(@ModelAttribute PontoDeVenda pontoDeVenda, RedirectAttributes ra) {
        if (!temPermissao()) {
            return redirecionarSemPermissao(ra);
        }
        try {
            pontoDeVendaService.cadastrar(pontoDeVenda);
            ra.addFlashAttribute("mensagem", "Ponto de venda cadastrado com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao cadastrar ponto de venda: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/ponto-de-venda";
    }

    @GetMapping("/editar/{id}")
    public String editarPontoVenda(@PathVariable Long id, Model model, RedirectAttributes ra) {
        if (!temPermissao()) {
            return redirecionarSemPermissao(ra);
        }
        try {
            PontoDeVenda pontoDeVenda = pontoDeVendaService.buscarPorId(id);
            model.addAttribute("pontoDeVenda", pontoDeVenda);
            model.addAttribute("gerentes", funcionarioService.listarGerentes());
            model.addAttribute("editando", true); // Adicionado
            return "admin/pontoDeVenda-form";
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao buscar ponto de venda: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao/ponto-de-venda";
        }
    }

    @PostMapping("/editar/{id}")
    public String atualizarPontoVenda(@PathVariable Long id, @ModelAttribute PontoDeVenda pontoDeVenda,
            RedirectAttributes ra) {
        if (!temPermissao()) {
            return redirecionarSemPermissao(ra);
        }
        try {
            PontoDeVenda pontoExistente = pontoDeVendaService.buscarPorId(id);
            
            pontoExistente.setNome(pontoDeVenda.getNome());
            pontoExistente.setCnpj(pontoDeVenda.getCnpj());
            pontoExistente.setCep(pontoDeVenda.getCep());
            pontoExistente.setRua(pontoDeVenda.getRua());
            pontoExistente.setNumero_rua(pontoDeVenda.getNumero_rua());
            pontoExistente.setTelefone(pontoDeVenda.getTelefone());
            pontoExistente.setGerente(pontoDeVenda.getGerente());
    
            pontoDeVendaService.atualizar(pontoExistente);
            
            ra.addFlashAttribute("mensagem", "Ponto de venda atualizado com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao atualizar ponto de venda: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/ponto-de-venda";
    }

    @DeleteMapping("/{id}")
    public String deletarPontoVenda(@PathVariable Long id, RedirectAttributes ra) {
        if (!temPermissao()) {
            return redirecionarSemPermissao(ra);
        }
        try {
            pontoDeVendaService.deletar(id);
            ra.addFlashAttribute("mensagem", "Ponto de venda excluído com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao excluir ponto de venda: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
        }
        return "redirect:/web/administracao/ponto-de-venda";
    }

    @GetMapping
    public String listarPontosVenda(Model model, RedirectAttributes ra) {
        if (!temPermissao()) {
            return redirecionarSemPermissao(ra);
        }
        model.addAttribute("pontosDeVenda", pontoDeVendaService.listarTodos());
        return "admin/pontoDeVenda-list";
    }

    private boolean temPermissao() {
        return funcionarioSession != null &&
                funcionarioSession.getFuncionario() != null &&
                (funcionarioSession.getFuncionario().getCargo() == Funcionario.Cargo.GERENTE ||
                        funcionarioSession.getFuncionario().getCargo() == Funcionario.Cargo.PADRAO);
    }

    private String redirecionarSemPermissao(RedirectAttributes ra) {
        ra.addFlashAttribute("mensagem", "Só o Gerente e o Funcionário podem acessar essa página");
        ra.addFlashAttribute("tipoMensagem", "error");
        return "redirect:/web/administracao";
    }
}
