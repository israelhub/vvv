package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
}
