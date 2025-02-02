package group.vvv.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group.vvv.dto.TicketDTO;
import group.vvv.models.Passageiro;
import group.vvv.models.Reserva;
import group.vvv.models.ReservaPassageiro;
import group.vvv.models.Ticket;
import group.vvv.repositories.TicketRepository;
import group.vvv.services.ModalService;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ModalService modalService;
    
    public List<Ticket> getTicketsByReserva(Long idReserva) {
        return ticketRepository.findByReservaId(idReserva);
    }
    
    public void gerarTickets(Reserva reserva) {
        List<ReservaPassageiro> passageiros = reservaService.getPassageiros(reserva);
        
        for (ReservaPassageiro rp : passageiros) {
            Ticket ticket = new Ticket();
            ticket.setTipoPassagem(determinarTipoPassagem(rp.getPassageiro()));
            ticket.setLocalizador(gerarLocalizador());
            
            // Usando os novos campos LocalDateTime da viagem
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

    public List<TicketDTO> buscarTicketsOnlinePorAno(int ano) {
        return ticketRepository.findByAnoAndOnline(ano)
            .stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }

    public List<TicketDTO> buscarTicketsOnlinePorAnoMes(int ano, int mes) {
        return ticketRepository.findByAnoMesAndOnline(ano, mes)
            .stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }

    public List<TicketDTO> buscarTicketsOnlinePorCompanhia(String companhia) {
        return ticketRepository.findByCompanhiaAndOnline(companhia)
            .stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }

    private TicketDTO converterParaDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setLocalizador(ticket.getLocalizador());
        dto.setTipoPassagem(ticket.getTipoPassagem());
        dto.setHoraPartida(ticket.getHoraPartida());
        dto.setHoraChegada(ticket.getHoraChegada());
        dto.setOrigem(ticket.getReserva().getOrigem());
        dto.setDestino(ticket.getReserva().getDestino());
        
        dto.setModalPrincipal(modalService.getDescricaoModal(ticket.getReserva().getViagem().getModal()));
        dto.setCompanhia(modalService.getNomeTransportadora(ticket.getReserva().getViagem().getModal()));
        
        dto.setValor(ticket.getReserva().getValorTotal().doubleValue());
        dto.setTipoPassagem(ticket.getTipoPassagem());
        return dto;
    }
}
