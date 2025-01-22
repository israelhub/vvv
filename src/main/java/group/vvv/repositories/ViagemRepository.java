package group.vvv.repositories;

import group.vvv.models.viagem.Viagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, Long> {
    
    @Query("SELECT DISTINCT v FROM Viagem v " +
           "LEFT JOIN FETCH v.roteiros r " +
           "LEFT JOIN FETCH r.local l " +
           "LEFT JOIN FETCH l.id_cidade c " +
           "LEFT JOIN FETCH l.id_aeroporto a " +
           "LEFT JOIN FETCH l.id_estacao e " +
           "LEFT JOIN FETCH l.id_porto p " +
           "LEFT JOIN FETCH r.modal m")
    List<Viagem> findAllWithDetails();

    @Query("SELECT DISTINCT v FROM Viagem v " +
           "LEFT JOIN FETCH v.roteiros r " +
           "LEFT JOIN FETCH r.local l " +
           "LEFT JOIN FETCH l.id_cidade c " +
           "LEFT JOIN FETCH l.id_aeroporto a " +
           "LEFT JOIN FETCH l.id_estacao e " +
           "LEFT JOIN FETCH l.id_porto p " +
           "LEFT JOIN FETCH r.modal m " +
           "WHERE v.id_viagem = ?1")
    Optional<Viagem> findByIdWithDetails(Long id);
}