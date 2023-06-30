package app.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class GlobalAttributesHandlerInterceptor implements HandlerInterceptor {
    private final UserService userService;

    @Autowired
    public GlobalAttributesHandlerInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler,
                           ModelAndView modelAndView) {
        if (modelAndView != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                String login = auth.getName(); // get logged in username
                String username = userService.getUsernameByLogin(login);
                boolean isadmin = auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"));
                request.setAttribute("isAdmin", isadmin);
                request.setAttribute("username", username);
            }
        }
    }
}
