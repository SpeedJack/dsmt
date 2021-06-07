package it.unipi.dsmt.das.web;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    HttpServletRequest httpRequest;
    public void init(FilterConfig config) {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();
        HttpSession session = ((HttpServletRequest)request).getSession(false);
        boolean isLogged =  (session != null && session.getAttribute("user") != null);
        String loginURI = httpRequest.getContextPath() + "/login";
        String registerURI = httpRequest.getContextPath() + "/register";
        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
        boolean isRegisterRequest = httpRequest.getRequestURI().equals(registerURI);
        boolean isStaticResource = (path.contains("/style") || path.contains("/js"));
        if(isLogged || isStaticResource || isLoginRequest || isRegisterRequest){
            if(isLogged)
                request.setAttribute("user", session.getAttribute("user"));
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendRedirect(loginURI);
        }
    }
}
