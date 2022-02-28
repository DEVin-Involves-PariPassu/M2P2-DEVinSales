package br.com.senai.p2m02.devinsales.interceptor;

import br.com.senai.p2m02.devinsales.model.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String authentication = request.getHeader("Authorization");

        UserEntity loggedUser = authService.tryToAuthenticate(authentication);

        if (loggedUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        } else {
            request.setAttribute("loggedUser", loggedUser);
        }

        return true;
    }
}
