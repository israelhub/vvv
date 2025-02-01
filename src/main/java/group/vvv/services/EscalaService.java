package group.vvv.services;

import group.vvv.models.viagem.Escala;
import group.vvv.repositories.EscalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EscalaService {

    @Autowired
    private EscalaRepository escalaRepository;

    @Transactional
    public void salvarEscala(Escala escala) {
        escalaRepository.save(escala);
    }

    public List<Escala> getEscalasPorViagem(Long idViagem) {
        return escalaRepository.findByViagemIdOrderByOrdemAsc(idViagem);
    }

    @Transactional
    public void salvarEscalas(List<Escala> escalas) {
        escalaRepository.saveAll(escalas);
    }

    @Transactional
    public void deletarEscalasDaViagem(Long idViagem) {
        List<Escala> escalas = escalaRepository.findByViagemIdOrderByOrdemAsc(idViagem);
        escalaRepository.deleteAll(escalas);
    }
}