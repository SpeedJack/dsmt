package it.unipi.dsmt.das.web;

import it.unipi.dsmt.das.ejbs.beans.interfaces.UserManager;
import it.unipi.dsmt.das.model.User;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "UserServlet", urlPatterns = { "/modifyPassword", "/login", "/register", "/logout"})
public class UserServlet extends HttpServlet {
    @EJB
    private UserManager userManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURI().substring(request.getContextPath().length());
        String page = url.substring(1);
        if(page.equals("login") || page.equals("register") || page.equals("modifyPassword")){
            request.setAttribute("target", page);
            request.getRequestDispatcher("credentials.jsp").forward(request, response);
        }
        else if(page.equals("logout"))
            doLogout(request, response);
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        session.removeAttribute("user");
        session.invalidate();
        response.sendRedirect(request.getContextPath() + "/login");
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURI().substring(request.getContextPath().length());
        switch (url) {

            case "/register":
                doRegister(request, response);
                break;
            case "/modifyPassword":
                doModifyPassword(request, response);
                break;
            case "/login":
                doLogin(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath());
        }
    }

    private void doModifyPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User sessionUser = (User)session.getAttribute("user");
        String username = sessionUser.getUsername();
        String oldPassword = request.getParameter("old_password");
        String newPassword = request.getParameter("new_password");
        String newPassword2 = request.getParameter("confirm_password");

        String destPage = "credentials.jsp";
        String message;

        if(!newPassword.equals(newPassword2)) {
            message = "The two passwords inserted are different. Retry";
        }
        else {
            User user = userManager.modify(username, oldPassword, newPassword);
            if (user != null) {
                message = "Password modified correctly";
            } else {
                message = "Error in the inserted password";
            }
        }
        request.setAttribute("target", "modifyPassword");
        request.setAttribute("message", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request, response);

    }

    private void doRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("new_password");
        String password2 = request.getParameter("confirm_password");

        String message;
        String destination = "credentials.jsp";
        try {
            if(!password.equals(password2)) {
                message = "The two passwords inserted are different. Retry";
            }
            else {
                User user = userManager.registration(username, password);
                if (user != null) {
                    message = "You are registered!";
                } else {
                    message = "Username already used. Please retry";

                }
            }
            request.setAttribute("target", "register");
            request.setAttribute("message", message);
            request.getRequestDispatcher(destination).forward(request, response);

        } catch (SQLException | IOException ex) {
            throw new ServletException(ex);
        }
    }

    private void doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userManager.login(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/auction");
        } else {
            String message = "Credentials not valid. Please retry";
            request.setAttribute("target", "login");
            request.setAttribute("message", message);
            request.getRequestDispatcher("credentials.jsp").forward(request,response);
        }
    }
}
