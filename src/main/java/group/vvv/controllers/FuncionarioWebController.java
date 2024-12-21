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
        return "areaCadastroFuncionario";
    }

    @PostMapping
    public String cadastrarFuncionarioWeb(@ModelAttribute Funcionario funcionario, Model model) {
        Funcionario novoFuncionario = funcionarioService.cadastrar(funcionario);
        model.addAttribute("mensagem", "Funcionário cadastrado com sucesso! Código: " + novoFuncionario.getCodigo_funcionario() + " Senha: " + novoFuncionario.getSenha());
        return "areaCadastroFuncionario";
    }
}