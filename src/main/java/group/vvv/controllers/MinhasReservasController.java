package group.vvv.controllers;

import group.vvv.config.UserSession;
import group.vvv.models.Reserva;
import group.vvv.services.ReservaService;
import group.vvv.services.TicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/minhas-reservas")
public class MinhasReservasController {

    @Autowired
    private UserSession userSession;

    @Autowired
    private ReservaService reservaService;
    
    @Autowired 
    private TicketService ticketService;

    @GetMapping
    public String exibirReservas(Model model) {
        if (!userSession.isAuthenticated()) {
            return "redirect:/web/clientes/login";
        }
        
        model.addAttribute("reservas", 
            reservaService.getReservasByCliente(userSession.getCliente()));
        return "cliente/minhasReservas";
    }

    @GetMapping("/ticket/{id}")
    public String downloadTicket(@PathVariable Long id) {
        // Implementar download do ticket
        return "redirect:/web/minhas-reservas";
    }
}
