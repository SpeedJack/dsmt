import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/seller")
public class SellerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SellerServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String destPage;
        if (session != null) {
            destPage = "homeSeller.jsp";
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