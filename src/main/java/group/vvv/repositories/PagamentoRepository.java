package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import group.vvv.models.Pagamento;


public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    
}
