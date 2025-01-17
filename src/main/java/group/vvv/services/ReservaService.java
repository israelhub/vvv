package group.vvv.services;

import group.vvv.models.Cliente;
import group.vvv.models.Reserva;
import group.vvv.models.Reserva.StatusReserva;
import group.vvv.models.ReservaPassageiro;
import group.vvv.repositories.ReservaPassageiroRepository;
import group.vvv.repositories.ReservaRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReservaPassageiroRepository reservaPassageiroRepository;

    public List<ReservaPassageiro> getPassageiros(Reserva reserva) {
        return reservaPassageiroRepository.findByReserva(reserva);
    }

    public Reserva getReservaById(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
    }

    public void salvarReserva(Reserva reserva) {
        reservaRepository.save(reserva);
    }

    public void salvarReservaPassageiro(ReservaPassageiro reservaPassageiro) {
        reservaPassageiroRepository.save(reservaPassageiro);
    }

    public List<Reserva> getReservasPendentesAprovacao() {
        return reservaRepository.findByStatus(StatusReserva.PENDENTE_AO_GERENTE_DE_NEGOCIOS_VIRTUAIS);
    }

    public void confirmarReserva(Long id) {
        Reserva reserva = getReservaById(id);
        reserva.setStatus(StatusReserva.CONFIRMADA);
        reservaRepository.save(reserva);
    }

    public List<Reserva> getReservasByCliente(Cliente cliente) {
        return reservaRepository.findByClienteOrderByDataDesc(cliente);
    }
}