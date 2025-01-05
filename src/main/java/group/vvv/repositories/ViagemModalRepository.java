package group.vvv.repositories;

import group.vvv.models.viagem.ViagemModal;
import group.vvv.models.viagem.ViagemModal.ViagemModalId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViagemModalRepository extends JpaRepository<ViagemModal, ViagemModalId> {
}