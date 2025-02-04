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

    @Query("SELECT t FROM Ticket t JOIN t.reserva r WHERE r.cliente IS NOT NULL AND YEAR(t.horaPartida) = :ano")
    List<Ticket> findByAnoAndOnline(@Param("ano") int ano);
    
    @Query("SELECT t FROM Ticket t JOIN t.reserva r WHERE r.cliente IS NOT NULL AND YEAR(t.horaPartida) = :ano AND MONTH(t.horaPartida) = :mes")
    List<Ticket> findByAnoMesAndOnline(@Param("ano") int ano, @Param("mes") int mes);
    
    @Query("SELECT t FROM Ticket t JOIN t.reserva r JOIN r.viagem v JOIN v.modal m JOIN m.transportadora tr WHERE r.cliente IS NOT NULL AND tr.nome = :companhia")
    List<Ticket> findByCompanhiaAndOnline(@Param("companhia") String companhia);

    @Query("SELECT t FROM Ticket t " +
       "JOIN t.reserva r " +
       "JOIN r.viagem v " + 
       "JOIN v.modal m " +
       "JOIN m.transportadora tr " +
       "WHERE r.cliente IS NOT NULL " +
       "AND tr.nome = :companhia " +
       "AND YEAR(t.horaPartida) = :ano " +
       "AND MONTH(t.horaPartida) = :mes")
List<Ticket> findByCompanhiaAnoMesAndOnline(
    @Param("companhia") String companhia,
    @Param("ano") int ano, 
    @Param("mes") int mes);
}