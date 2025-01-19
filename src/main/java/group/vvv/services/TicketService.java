package group.vvv.services;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group.vvv.models.Passageiro;
import group.vvv.models.Reserva;
import group.vvv.models.ReservaPassageiro;
import group.vvv.models.Ticket;
import group.vvv.repositories.TicketRepository;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private ReservaService reservaService;
    
    public List<Ticket> getTicketsByReserva(Long idReserva) {
        return ticketRepository.findByReservaId(idReserva);
    }
    
    
    public void gerarTickets(Reserva reserva) {
        List<ReservaPassageiro> passageiros = reservaService.getPassageiros(reserva);
        
        for (ReservaPassageiro rp : passageiros) {
            Ticket ticket = new Ticket();
            ticket.setTipoPassagem(determinarTipoPassagem(rp.getPassageiro()));
            ticket.setLocalizador(gerarLocalizador());
            ticket.setHoraPartida(reserva.getViagem().getHorarioPartida());
            ticket.setHoraChegada(reserva.getViagem().getHorarioChegada());
            ticket.setReserva(reserva);
            ticket.setPassageiro(rp.getPassageiro());
            
            ticketRepository.save(ticket);
        }
    }
    
    private String gerarLocalizador() {
        return "LOC" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
    
    private String determinarTipoPassagem(Passageiro passageiro) {
        return passageiro.getIdade() <= 10 ? "INFANTIL" : "ADULTO";
    }
}