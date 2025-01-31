package group.vvv.repositories;

import group.vvv.models.viagem.Escala;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EscalaRepository extends JpaRepository<Escala, Long> {
    @Query("SELECT e FROM Escala e WHERE e.viagem.id_viagem = :idViagem ORDER BY e.ordem ASC")
    List<Escala> findByViagemIdOrderByOrdemAsc(@Param("idViagem") Long idViagem);
}