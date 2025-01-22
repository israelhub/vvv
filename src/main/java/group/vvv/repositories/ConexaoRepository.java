package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.viagem.Conexao;
import group.vvv.models.viagem.Conexao.ViagemLocalId;

public interface ConexaoRepository extends JpaRepository<Conexao, ViagemLocalId> {
}