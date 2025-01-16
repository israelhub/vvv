package group.vvv.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.Reserva;
import group.vvv.models.Reserva.StatusReserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByStatus(StatusReserva status);
}
