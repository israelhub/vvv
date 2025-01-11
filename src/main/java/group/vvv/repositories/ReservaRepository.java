package group.vvv.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
