package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.viagem.Porto;

public interface PortoRepository extends JpaRepository<Porto, Long> {
}