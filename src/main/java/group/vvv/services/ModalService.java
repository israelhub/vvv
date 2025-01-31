package group.vvv.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group.vvv.models.viagem.Modal;
import group.vvv.repositories.ModalRepository;

@Service
public class ModalService {
    
    @Autowired
    private ModalRepository modalRepository;

    public void cadastrar(Modal modal){
        modalRepository.save(modal);
    }

    public Modal getModalById(Long id) {
        return modalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modal não encontrado"));
    }

    public List<Modal> getModais() {
        return modalRepository.findAll();
    }
    
    public void deletar(Long id) {
        modalRepository.deleteById(id);
    }
}