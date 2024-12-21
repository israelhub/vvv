package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}
