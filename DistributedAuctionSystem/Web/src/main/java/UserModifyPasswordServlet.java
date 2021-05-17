import java.io.*;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/modify")
public class UserModifyPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

        UserUtility userModify = new UserUtility();
        String destPage = "password_modify.jsp";
        String message;

        try {
            if(!newPassword.equals(newPassword2)) {
                message = "The two passwords inserted are different. Retry";
                request.setAttribute("message", message);
            }
            else {
                User user = userModify.modify(username, oldPassword, newPassword);
                if (user != null) {
                    message = "Password modified correctly";
                    request.setAttribute("message", message);
                } else {
                    message = "Error in the inserted password";
                    request.setAttribute("message", message);
                }
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
            dispatcher.forward(request, response);

        } catch (SQLException | ClassNotFoundException ex) {
            throw new ServletException(ex);
        }
    }
}
