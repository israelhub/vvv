package group.vvv.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group.vvv.models.viagem.Cidade;
import group.vvv.repositories.CidadeRepository;

@Service
public class CidadeService {
    
    @Autowired
    CidadeRepository cidadeRepository;

    public List<Cidade> listarTodas() {
        return cidadeRepository.findAll();
    }

    public Cidade findById(Long id) {
        return cidadeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cidade não encontrada"));
    }
}
