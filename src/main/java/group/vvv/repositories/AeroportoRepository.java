package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.viagem.Aeroporto;

public interface AeroportoRepository extends JpaRepository<Aeroporto, Long> {
}