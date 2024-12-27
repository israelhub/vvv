package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import group.vvv.models.viagem.Estacao;

public interface EstacaoRepository extends JpaRepository<Estacao, Long> {
}