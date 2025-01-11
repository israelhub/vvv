package group.vvv.config;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import group.vvv.models.Cliente;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession {
    private Cliente cliente;
    private boolean authenticated = false;

    public void login(Cliente cliente) {
        this.cliente = cliente;
        this.authenticated = true;
    }

    public void logout() {
        this.cliente = null;
        this.authenticated = false;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}