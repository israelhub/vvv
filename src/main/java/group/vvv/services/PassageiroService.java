package group.vvv.services;

import group.vvv.models.Passageiro;
import group.vvv.repositories.PassageiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassageiroService {

    @Autowired
    private PassageiroRepository passageiroRepository;

    public void salvarPassageiro(Passageiro passageiro) {
        passageiroRepository.save(passageiro);
    }

    public List<Passageiro> getPassageirosByViagem(Long idViagem) {
        return passageiroRepository.findByReservaViagemId(idViagem);
    }

    public Passageiro getPassageiroById(Long id) {
        return passageiroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Passageiro não encontrado"));
    }
}