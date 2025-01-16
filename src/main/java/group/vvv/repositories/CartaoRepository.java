package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.Cartao;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {   
}
