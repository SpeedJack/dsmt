package it.unipi.dsmt.das.web;
import it.unipi.dsmt.das.model.User;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CustomerServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String destPage;

        if (session != null) {
            destPage = "homeCustomer.jsp";
            User sessionUser = (User)session.getAttribute("user");
            String username = sessionUser.getUsername();
            request.setAttribute("username", username);
        }
        else
            destPage = "index.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request, response);
    }
}