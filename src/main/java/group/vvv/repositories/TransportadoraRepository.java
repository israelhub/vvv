package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import group.vvv.models.viagem.Transportadora;

@Repository
public interface TransportadoraRepository extends JpaRepository<Transportadora, Long> {
}