package group.vvv.services;

import group.vvv.models.viagem.*;
import group.vvv.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViagemService {

    @Autowired
    private ViagemRepository viagemRepository;

    public void salvarViagem(Viagem viagem) {
        viagemRepository.save(viagem);
    }

    public List<Viagem> getViagens() {
        return viagemRepository.findAllWithEscalas();
    }

    public Viagem getViagemById(Long id) {
        return viagemRepository.findByIdWithEscalas(id)
            .orElseThrow(() -> new RuntimeException("Viagem não encontrada"));
    }

    public void deletarViagem(Long id) {
        viagemRepository.deleteById(id);
    }

    public void atualizarViagem(Viagem viagem) {
        viagemRepository.save(viagem);
    }
}