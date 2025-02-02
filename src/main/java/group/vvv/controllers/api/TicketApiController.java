package group.vvv.controllers.api;

import group.vvv.dto.TicketDTO;
import group.vvv.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketApiController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/ano/{ano}")
    public ResponseEntity<List<TicketDTO>> getTicketsPorAno(@PathVariable int ano) {
        return ResponseEntity.ok(ticketService.buscarTicketsOnlinePorAno(ano));
    }

    @GetMapping("/ano/{ano}/mes/{mes}")
    public ResponseEntity<List<TicketDTO>> getTicketsPorAnoMes(
            @PathVariable int ano, 
            @PathVariable int mes) {
        return ResponseEntity.ok(ticketService.buscarTicketsOnlinePorAnoMes(ano, mes));
    }

    @GetMapping("/companhia/{companhia}")
    public ResponseEntity<List<TicketDTO>> getTicketsPorCompanhia(
            @PathVariable String companhia) {
        return ResponseEntity.ok(ticketService.buscarTicketsOnlinePorCompanhia(companhia)); 
    }
}