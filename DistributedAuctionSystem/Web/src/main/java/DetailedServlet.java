import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/detailed")
public class DetailedServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DetailedServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String destPage;
        if (session != null) {
            destPage = "detailed_object.jsp";
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