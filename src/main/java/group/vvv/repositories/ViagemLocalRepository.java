package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.viagem.ViagemLocal;
import group.vvv.models.viagem.ViagemLocal.ViagemLocalId;

public interface ViagemLocalRepository extends JpaRepository<ViagemLocal, ViagemLocalId> {
}