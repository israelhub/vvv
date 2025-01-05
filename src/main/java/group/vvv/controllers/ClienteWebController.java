package group.vvv.controllers;

import group.vvv.models.Cliente;
import group.vvv.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/clientes")
public class ClienteWebController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/login")
    public String exibirFormularioLogin(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/loginCliente";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Cliente cliente, Model model) {
        Cliente clienteLogado = clienteService.login(cliente.getEmail(), cliente.getSenha());
        if (clienteLogado != null) {
            return "redirect:/web/paginaInicial";
        } else {
            model.addAttribute("mensagem", "Email ou senha inválidos.");
            return "cliente/loginCliente";
        }
    }
}