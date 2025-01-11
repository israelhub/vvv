package group.vvv.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.ReservaPassageiro;

public interface ReservaPassageiroRepository extends JpaRepository<ReservaPassageiro, ReservaPassageiro.ReservaPassageiroId> {
}

