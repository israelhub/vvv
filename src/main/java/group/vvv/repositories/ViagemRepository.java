package group.vvv.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import group.vvv.models.viagem.Viagem;

public interface ViagemRepository extends JpaRepository<Viagem, Long> {
    @Query("SELECT v FROM Viagem v LEFT JOIN FETCH v.escalas WHERE v.id_viagem = :id")
    Optional<Viagem> findByIdWithEscalas(Long id);

    @Query("SELECT v FROM Viagem v LEFT JOIN FETCH v.escalas")
    List<Viagem> findAllWithEscalas();
}