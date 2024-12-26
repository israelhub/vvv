package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.viagem.Viagem;

public interface ViagemRepository extends JpaRepository<Viagem, Long> {
}