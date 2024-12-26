package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.viagem.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {
    
}