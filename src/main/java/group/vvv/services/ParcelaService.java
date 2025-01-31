package group.vvv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import group.vvv.models.Parcela;
import group.vvv.repositories.ParcelaRepository;

@Service
public class ParcelaService {

    @Autowired
    private ParcelaRepository parcelaRepository;

    public Parcela salvarParcela(Parcela parcela) {
        return parcelaRepository.save(parcela);
    }
}