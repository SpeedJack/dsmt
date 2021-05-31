package it.unipi.dsmt.das.web;

import it.unipi.dsmt.das.ejbs.beans.interfaces.UserManager;
import it.unipi.dsmt.das.model.User;

import java.io.*;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class UserLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private UserManager userManager;
    public UserLoginServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userManager.login(username, password);
        RequestDispatcher dispatcher;
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            request.setAttribute("username", user.getUsername());
            request.setAttribute("ID", user.getId());
            dispatcher = request.getServletContext().getRequestDispatcher("/customer");
        } else {
            String message = "Credentials not valid. Please retry";
            request.setAttribute("message", message);
            dispatcher = request.getRequestDispatcher("index.jsp");
        }

        dispatcher.forward(request, response);

    }
}
