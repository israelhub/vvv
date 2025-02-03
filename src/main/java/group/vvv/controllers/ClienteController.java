package group.vvv.controllers;

import group.vvv.config.UserSession;
import group.vvv.models.Cliente;
import group.vvv.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UserSession userSession;

    public ClienteController(ClienteService clienteService, UserSession userSession) {
        this.clienteService = clienteService;
        this.userSession = userSession;
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/areaCadastroCliente";
    }

    @PostMapping
    public String cadastrarClienteWeb(@ModelAttribute Cliente cliente, Model model) {
        try {
            clienteService.cadastrar(cliente);
            model.addAttribute("mensagem", "Cliente cadastrado com sucesso!");
            return "cliente/areaCadastroCliente";
        } catch (Exception e) {
            model.addAttribute("mensagem", "Erro ao cadastrar: " + e.getMessage());
            return "cliente/areaCadastroCliente";
        }
    }

    @GetMapping("/login")
    public String exibirFormularioLogin(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/loginCliente";
    }

    @PostMapping("/login")
    public String processarLogin(@RequestParam String email,
            @RequestParam String senha,
            @RequestParam(required = false) String returnUrl,
            @RequestParam(required = false) Integer passageirosNormal,
            @RequestParam(required = false) Integer passageirosCrianca,
            RedirectAttributes ra) {
        try {
            Cliente cliente = clienteService.login(email, senha);
            userSession.login(cliente);

            if (returnUrl != null && !returnUrl.isEmpty()) {
                String redirectUrl = returnUrl;
                if (passageirosNormal != null && passageirosCrianca != null) {
                    redirectUrl += "?passageirosNormal=" + passageirosNormal +
                            "&passageirosCrianca=" + passageirosCrianca;
                }
                return "redirect:" + redirectUrl;
            }

            return "redirect:/web/paginaInicial";
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", "Email ou senha inválidos");
            return "redirect:/web/clientes/login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        userSession.logout();
        return "redirect:/web/paginaInicial";
    }
}