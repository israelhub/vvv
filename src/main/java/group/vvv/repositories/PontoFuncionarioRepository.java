package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.PontoFuncionario;

public interface PontoFuncionarioRepository extends JpaRepository<PontoFuncionario, PontoFuncionario.PontoFuncionarioId> {
}