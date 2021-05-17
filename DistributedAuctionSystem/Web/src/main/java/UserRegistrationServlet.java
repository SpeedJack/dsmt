import java.io.*;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/registration")
public class UserRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserRegistrationServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("new_password");
        String password2 = request.getParameter("confirm_password");

        UserUtility userRegistration = new UserUtility();
        String destPage = "registration_page.jsp";
        String message;
        try {
            if(!password.equals(password2)) {
                message = "The two passwords inserted are different. Retry";
                request.setAttribute("message", message);
            }
            else {
                User user = userRegistration.registration(username, password);
                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    request.setAttribute("username", username);
                    destPage = "homeCustomer.jsp";
                } else {
                    message = "Username already used. Please retry";
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
