package group.vvv.controllers;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import group.vvv.config.FuncionarioSession;
import group.vvv.models.Funcionario;
import group.vvv.models.PontoFuncionario;
import group.vvv.services.FuncionarioService;
import group.vvv.services.PontoDeVendaService;

@Controller
@RequestMapping("/web/funcionarios")
public class FuncionarioWebController {
    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private PontoDeVendaService pontoDeVendaService;

    @Autowired
    private FuncionarioSession funcionarioSession;

    @GetMapping("/login")
    public String exibirFormularioLogin(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        return "funcionario/loginFuncionario";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Funcionario funcionario, Model model) {
        Funcionario funcionarioExistente = funcionarioService.login(funcionario.getEmail(), funcionario.getSenha());
        if (funcionarioExistente != null) {
            funcionarioSession.login(funcionarioExistente);
            if (!funcionarioExistente.isLoginInicialRealizado()) {
                model.addAttribute("funcionario", funcionarioExistente);
                return "funcionario/atualizarDadosFuncionario";
            }
            return "redirect:/web/administracao";
        }
        model.addAttribute("mensagem", "Email ou senha inválidos.");
        return "funcionario/loginFuncionario";
    }

    @GetMapping("/logout")
    public String logout() {
        funcionarioSession.logout();
        return "redirect:/web/funcionarios/login";
    }

    @GetMapping("/login-inicial")
    public String exibirFormularioLoginInicial(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        return "funcionario/loginInicialFuncionario";
    }

    @PostMapping("/atualizar-dados")
    public String atualizarDadosFuncionario(@ModelAttribute Funcionario funcionario, Model model) {
        funcionarioService.atualizarDados(funcionario);
        model.addAttribute("mensagem", "Dados atualizados com sucesso!");
        return "funcionario/atualizarDadosFuncionario";
    }
}