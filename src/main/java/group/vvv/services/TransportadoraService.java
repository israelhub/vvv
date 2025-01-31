package group.vvv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import group.vvv.models.viagem.Transportadora;
import group.vvv.repositories.TransportadoraRepository;

@Service
public class TransportadoraService {

    @Autowired
    private TransportadoraRepository transportadoraRepository;

    public List<Transportadora> listarTodas() {
        return transportadoraRepository.findAll();
    }

    public Transportadora buscarPorId(Long id) {
        return transportadoraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transportadora não encontrada"));
    }

    public Transportadora salvar(Transportadora transportadora) {
        return transportadoraRepository.save(transportadora);
    }

    public void deletar(Long id) {
        transportadoraRepository.deleteById(id);
    }
}