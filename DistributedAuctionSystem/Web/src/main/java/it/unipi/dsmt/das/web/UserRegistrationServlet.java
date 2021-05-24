package it.unipi.dsmt.das.web;

import it.unipi.dsmt.das.ejbs.beans.interfaces.UserManager;
import it.unipi.dsmt.das.model.User;

import java.io.*;
import java.sql.SQLException;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/registration")
public class UserRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    UserManager userManager;
    public UserRegistrationServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("new_password");
        String password2 = request.getParameter("confirm_password");

        String message;
        RequestDispatcher dispatcher;
        try {
            if(!password.equals(password2)) {
                message = "The two passwords inserted are different. Retry";
                request.setAttribute("message", message);
                dispatcher = request.getRequestDispatcher("registration_page.jsp");
            }
            else {
                User user = userManager.registration(username, password);
                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    request.setAttribute("username", username);
                    dispatcher = request.getServletContext().getRequestDispatcher("/customer");
                } else {
                    message = "Username already used. Please retry";
                    request.setAttribute("message", message);
                    dispatcher = request.getRequestDispatcher("registration_page.jsp");
                }
            }

            dispatcher.forward(request, response);

        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
