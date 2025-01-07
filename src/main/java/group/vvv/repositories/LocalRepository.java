package group.vvv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import group.vvv.models.viagem.Local;

public interface LocalRepository extends JpaRepository<Local, Long> {
}