package group.vvv.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/administracao")
public class PaginaAdminWebController {

    @GetMapping
    public String exibirPaginaAdmin() {
        return "paginaAdministracao";
    }
    
    // Futuramente irei adicionar:
    // - Verificação de autenticação do funcionário
    // - Carregamento de dados específicos para dashboard
    // - Controle de sessão administrativa
}