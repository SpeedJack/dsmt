package it.unipi.dsmt.das.web;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.model.Auction;
import it.unipi.dsmt.das.model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/sell")
@MultipartConfig
public class SellServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Utility utility = new Utility();
    @EJB
    private AuctionManager auctionManager;
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
            request.setAttribute("username", sessionUser.getUsername());
            request.setAttribute("ID", sessionUser.getId());
        }
        else
            destPage = "index.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String destPage;

        if (session != null) {
            User sessionUser = (User)session.getAttribute("user");

            ServletContext context = getServletContext();
            String filePath = context.getInitParameter("file-upload");
            Part filePart = request.getPart("userfile"); // Retrieves <input type="file" name="userfile">
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
            InputStream fileContent = filePart.getInputStream();
            File f = new File(filePath + "\\" + sessionUser.getId() + "_" + fileName);
            OutputStream out = new FileOutputStream(f);
            IOUtils.copy(fileContent, out);
            fileContent.close();
            out.close();

            destPage = "sell.jsp";
            String message;
            request.setAttribute("username", sessionUser.getUsername());
            request.setAttribute("ID", sessionUser.getId());
            Auction auction = new Auction(  sessionUser.getId(),
                                            request.getParameter("name"),
                                      filePath + "\\" + sessionUser.getId() + "_" + fileName,
                                            request.getParameter("description"),
                                            utility.getTimestamp(request.getParameter("day") + " " +
                                                    request.getParameter("hour")),
                                            Double.parseDouble(request.getParameter("minimum_bid")),
                                            Double.parseDouble(request.getParameter("minimum_raise")),
                                            Long.parseLong(request.getParameter("object")));
            String res = auctionManager.createAuction(auction);
            if(res.equals("ok"))
                message = "Object correctly inserted";
            else
                message = "Error in inserting new object";
            request.setAttribute("message", message);
        }
        else
            destPage = "index.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request, response);
    }
}