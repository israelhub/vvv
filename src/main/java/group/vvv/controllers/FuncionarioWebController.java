package group.vvv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import group.vvv.models.Funcionario;
import group.vvv.services.FuncionarioService;

@Controller
@RequestMapping("/web/funcionarios")
public class FuncionarioWebController {
    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        return "funcionarios/areaCadastroFuncionario";
    }

    @PostMapping
    public String cadastrarFuncionarioWeb(@ModelAttribute Funcionario funcionario, Model model) {
        Funcionario novoFuncionario = funcionarioService.cadastrar(funcionario);
        model.addAttribute("mensagem", "Funcionário cadastrado com sucesso! Código: " + novoFuncionario.getCodigo_funcionario() + " Senha: " + novoFuncionario.getSenha());
        return "funcionarios/areaCadastroFuncionario";
    }

    @GetMapping("/login")
    public String exibirFormularioLogin(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        return "funcionarios/loginFuncionario";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Funcionario funcionario, Model model) {
        Funcionario funcionarioExistente = funcionarioService.login(funcionario.getEmail(), funcionario.getSenha());
        if (funcionarioExistente != null) {
            if (!funcionarioExistente.isLoginInicialRealizado()) {
                model.addAttribute("funcionario", funcionarioExistente);
                return "funcionarios/atualizarDadosFuncionario";
            } else {
                // Redirecionar para o endpoint de administração (a ser configurado no futuro)
                return "redirect:/web/administracao";
            }
        } else {
            model.addAttribute("mensagem", "Email ou senha inválidos.");
            return "funcionarios/loginFuncionario";
        }
    }

    @GetMapping("/login-inicial")
    public String exibirFormularioLoginInicial(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        return "funcionarios/loginInicialFuncionario";
    }

    @PostMapping("/atualizar-dados")
    public String atualizarDadosFuncionario(@ModelAttribute Funcionario funcionario, Model model) {
        funcionarioService.atualizarDados(funcionario);
        model.addAttribute("mensagem", "Dados atualizados com sucesso!");
        return "funcionarios/atualizarDadosFuncionario";
    }
}