package group.vvv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import group.vvv.models.Funcionario;
import group.vvv.models.PontoFuncionario;
import group.vvv.models.Funcionario.Cargo;
import group.vvv.repositories.FuncionarioRepository;
import group.vvv.repositories.PontoFuncionarioRepository;

import java.util.List;
import java.util.UUID;

@Service
public class FuncionarioService {
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PontoFuncionarioRepository pontoFuncionarioRepository;

    public Funcionario cadastrar(Funcionario funcionario) {
        String codigo_funcionario = UUID.randomUUID().toString().substring(0, 10);
        String senha = UUID.randomUUID().toString().substring(0, 8);

        funcionario.setCodigo_funcionario(codigo_funcionario);
        funcionario.setSenha(senha);

        return funcionarioRepository.save(funcionario);
    }

    public void cadastrarPontoFuncionario(PontoFuncionario pontoFuncionario) {
        pontoFuncionarioRepository.save(pontoFuncionario);
    }

    public Funcionario login(String email, String senha) {
        return funcionarioRepository.findByEmailAndSenha(email, senha);
    }

    public void atualizarDados(Funcionario funcionario) {
        Funcionario funcionarioExistente = funcionarioRepository.findById(funcionario.getId_funcionario()).orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));
        funcionarioExistente.setCep(funcionario.getCep());
        funcionarioExistente.setRua(funcionario.getRua());
        funcionarioExistente.setNumero_rua(funcionario.getNumero_rua());
        funcionarioExistente.setSenha(funcionario.getSenha());
        funcionarioExistente.setLoginInicialRealizado(true);
        funcionarioRepository.save(funcionarioExistente);
    }

    public List<Funcionario> listarGerentes() {
        return funcionarioRepository.findByCargo(Cargo.GERENTE);
    }
}