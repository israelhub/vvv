package group.vvv.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.Funcionario;
import group.vvv.models.Funcionario.Cargo;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Funcionario findByEmailAndSenha(String email, String senha);
    List<Funcionario> findByCargo(Cargo cargo);
}
