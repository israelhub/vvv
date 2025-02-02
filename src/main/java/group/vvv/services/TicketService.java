package group.vvv.services;

import java.time.LocalDateTime;
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
import group.vvv.models.viagem.Escala;
import group.vvv.models.viagem.Viagem;
import group.vvv.repositories.TicketRepository;

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
        Viagem viagem = reserva.getViagem();
        List<Escala> escalas = viagem.getEscalas();

        for (ReservaPassageiro rp : passageiros) {
            if (escalas == null || escalas.isEmpty()) {
                // Viagem direta - gera um único ticket
                gerarTicketUnico(rp, reserva, viagem);
            } else {
                // Viagem com escalas - gera um ticket para cada trecho
                gerarTicketsComEscalas(rp, reserva, viagem, escalas);
            }
        }
    }

    private void gerarTicketUnico(ReservaPassageiro rp, Reserva reserva, Viagem viagem) {
        Ticket ticket = new Ticket();
        ticket.setTipoPassagem(determinarTipoPassagem(rp.getPassageiro()));
        ticket.setLocalizador(gerarLocalizador());
        ticket.setHoraPartida(viagem.getHorarioPartida());
        ticket.setHoraChegada(viagem.getHorarioChegada());
        ticket.setReserva(reserva);
        ticket.setPassageiro(rp.getPassageiro());
        ticketRepository.save(ticket);
    }

    public void gerarTicketsComEscalas(ReservaPassageiro rp, Reserva reserva, Viagem viagem, List<Escala> escalas) {
        // Primeiro trecho: origem até primeira escala
        Ticket primeiroTicket = new Ticket();
        primeiroTicket.setTipoPassagem(determinarTipoPassagem(rp.getPassageiro()));
        primeiroTicket.setLocalizador(gerarLocalizador());
        primeiroTicket.setHoraPartida(viagem.getHorarioPartida());
        primeiroTicket.setHoraChegada(escalas.get(0).getHorarioPartida()); 
        primeiroTicket.setReserva(reserva);
        primeiroTicket.setPassageiro(rp.getPassageiro());
        primeiroTicket.setOrigem(viagem.getOrigem().getDescricaoCompleta());
        primeiroTicket.setDestino(escalas.get(0).getDestino().getDescricaoCompleta());
        ticketRepository.save(primeiroTicket);
    
        // Trechos intermediários (entre escalas)
        for (int i = 0; i < escalas.size() - 1; i++) {
            Escala atual = escalas.get(i);
            Escala proxima = escalas.get(i + 1);
            
            Ticket ticketIntermediario = new Ticket();
            ticketIntermediario.setTipoPassagem(determinarTipoPassagem(rp.getPassageiro()));
            ticketIntermediario.setLocalizador(gerarLocalizador());
            ticketIntermediario.setHoraPartida(atual.getHorarioPartida());
            ticketIntermediario.setHoraChegada(proxima.getHorarioPartida());
            ticketIntermediario.setReserva(reserva);
            ticketIntermediario.setPassageiro(rp.getPassageiro());
            ticketIntermediario.setOrigem(atual.getDestino().getDescricaoCompleta());
            ticketIntermediario.setDestino(proxima.getDestino().getDescricaoCompleta());
            ticketRepository.save(ticketIntermediario);
        }
    
        // Último trecho: última escala até destino final
        Escala ultimaEscala = escalas.get(escalas.size() - 1);
        Ticket ultimoTicket = new Ticket();
        ultimoTicket.setTipoPassagem(determinarTipoPassagem(rp.getPassageiro()));
        ultimoTicket.setLocalizador(gerarLocalizador());
        ultimoTicket.setHoraPartida(ultimaEscala.getHorarioPartida());
        ultimoTicket.setHoraChegada(viagem.getHorarioChegada());
        ultimoTicket.setReserva(reserva);
        ultimoTicket.setPassageiro(rp.getPassageiro());
        ultimoTicket.setOrigem(ultimaEscala.getDestino().getDescricaoCompleta());
        ultimoTicket.setDestino(viagem.getDestino().getDescricaoCompleta());
        ticketRepository.save(ultimoTicket);
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
        dto.setOrigem(ticket.getOrigem());
        dto.setDestino(ticket.getDestino());
        
        dto.setModalPrincipal(modalService.getDescricaoModal(ticket.getReserva().getViagem().getModal()));
        dto.setCompanhia(modalService.getNomeTransportadora(ticket.getReserva().getViagem().getModal()));

        // Valor total da reserva
        dto.setValorTotal(ticket.getReserva().getValorTotal().doubleValue());
        
        // Calcula valor proporcional do trecho
        int totalTickets = ticketRepository.findByReservaId(ticket.getReserva().getId_reserva()).size();
        double valorTrecho = ticket.getReserva().getValorTotal().doubleValue() / totalTickets;
        dto.setValorTrecho(valorTrecho);

        return dto;
    }
}
