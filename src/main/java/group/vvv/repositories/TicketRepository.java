package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    
}