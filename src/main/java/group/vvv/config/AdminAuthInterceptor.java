package group.vvv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {
    
    @Autowired
    private FuncionarioSession funcionarioSession;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        
        if (path.contains("/web/funcionarios/login")) {
            return true;
        }

        if (path.startsWith("/web/administracao")) {
            if (!funcionarioSession.isAuthenticated()) {
                response.sendRedirect("/web/funcionarios/login");
                return false;
            }
        }

        return true;
    }
}
