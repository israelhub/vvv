package group.vvv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Autowired
    private UserSession userSession;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        
        // Permitir acesso à página de login e cadastro
        if (path.contains("/login") || path.contains("/novo") || path.startsWith("/web/paginaInicial")) {
            return true;
        }

        // Verificar autenticação para outras páginas
        if (!userSession.isAuthenticated()) {
            response.sendRedirect("/web/clientes/login");
            return false;
        }

        return true;
    }
}