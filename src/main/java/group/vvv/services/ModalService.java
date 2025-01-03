package group.vvv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group.vvv.models.Modal;
import group.vvv.repositories.ModalRepository;

@Service
public class ModalService {
    
    @Autowired
    private ModalRepository modalRepository;

    public void cadastrar(Modal modal){
        modalRepository.save(modal);
    }
}
