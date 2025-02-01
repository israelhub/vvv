package group.vvv.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import group.vvv.models.PontoFuncionario;

public interface PontoFuncionarioRepository
        extends JpaRepository<PontoFuncionario, PontoFuncionario.PontoFuncionarioId> {
    @Query("SELECT pf FROM PontoFuncionario pf WHERE pf.funcionario.id = :funcionarioId")
    List<PontoFuncionario> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId);

    @Query("DELETE FROM PontoFuncionario pf WHERE pf.funcionario.id = :funcionarioId")
    @Modifying
    void deleteByFuncionarioId(@Param("funcionarioId") Long funcionarioId);
}