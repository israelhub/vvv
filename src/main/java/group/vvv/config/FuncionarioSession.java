package group.vvv.config;

import group.vvv.models.Funcionario;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class FuncionarioSession {
    private Funcionario funcionario;
    private boolean authenticated;

    public void login(Funcionario funcionario) {
        this.funcionario = funcionario;
        this.authenticated = true;
    }

    public void logout() {
        this.funcionario = null;
        this.authenticated = false;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }
}