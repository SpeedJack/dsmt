import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/sell")
public class SellServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SellServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String destPage;
        if (session != null) {
            destPage = "sell.jsp";
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