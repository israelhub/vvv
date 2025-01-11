package group.vvv.services;

import group.vvv.models.Reserva;
import group.vvv.models.ReservaPassageiro;
import group.vvv.repositories.ReservaPassageiroRepository;
import group.vvv.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReservaPassageiroRepository reservaPassageiroRepository;

    public void salvarReserva(Reserva reserva) {
        reservaRepository.save(reserva);
    }

    public void salvarReservaPassageiro(ReservaPassageiro reservaPassageiro) {
        reservaPassageiroRepository.save(reservaPassageiro);
    }
}