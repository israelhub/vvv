package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import group.vvv.models.Parcela;

public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
    
}
