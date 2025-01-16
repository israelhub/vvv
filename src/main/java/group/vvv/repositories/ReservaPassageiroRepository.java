package group.vvv.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import group.vvv.models.Reserva;
import group.vvv.models.ReservaPassageiro;

public interface ReservaPassageiroRepository extends JpaRepository<ReservaPassageiro, ReservaPassageiro.ReservaPassageiroId> {
    List<ReservaPassageiro> findByReserva(Reserva reserva);
}
