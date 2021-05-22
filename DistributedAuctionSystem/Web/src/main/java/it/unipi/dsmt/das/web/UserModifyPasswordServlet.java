package it.unipi.dsmt.das.web;

import it.unipi.dsmt.das.ejbs.beans.interfaces.UserManager;
import it.unipi.dsmt.das.model.User;

import java.io.*;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/modify")
public class UserModifyPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private UserManager userManager;
    public UserModifyPasswordServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User sessionUser = (User)session.getAttribute("user");
        String username = sessionUser.getUsername();
        String oldPassword = request.getParameter("old_password");
        String newPassword = request.getParameter("new_password");
        String newPassword2 = request.getParameter("confirm_password");

        String destPage = "password_modify.jsp";
        String message;

        if(!newPassword.equals(newPassword2)) {
            message = "The two passwords inserted are different. Retry";
            request.setAttribute("message", message);
        }
        else {
            User user = userManager.modify(username, oldPassword, newPassword);
            if (user != null) {
                message = "Password modified correctly";
            } else {
                message = "Error in the inserted password";
            }
            request.setAttribute("message", message);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request, response);

    }
}
