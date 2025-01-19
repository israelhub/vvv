package group.vvv.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import group.vvv.models.Ticket;
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE t.reserva.id_reserva = :idReserva")
    List<Ticket> findByReservaId(@Param("idReserva") Long idReserva);
}