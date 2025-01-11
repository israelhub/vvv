package group.vvv.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import group.vvv.models.Passageiro;

public interface PassageiroRepository extends JpaRepository<Passageiro, Long> {
    @Query("SELECT DISTINCT rp.passageiro FROM ReservaPassageiro rp WHERE rp.reserva.viagem.id_viagem = :idViagem")
    List<Passageiro> findByReservaViagemId(@Param("idViagem") Long idViagem);
}
