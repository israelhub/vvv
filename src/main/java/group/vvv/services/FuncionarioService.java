package group.vvv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import group.vvv.models.Funcionario;
import group.vvv.repositories.FuncionarioRepository;

import java.util.UUID;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public Funcionario cadastrar(Funcionario funcionario) {
        // Gerar código do funcionário e senha
        String codigo_funcionario = UUID.randomUUID().toString().substring(0, 10);
        String senha = UUID.randomUUID().toString().substring(0, 8);

        funcionario.setCodigo_funcionario(codigo_funcionario);
        funcionario.setSenha(senha);

        return funcionarioRepository.save(funcionario);
    }
}
