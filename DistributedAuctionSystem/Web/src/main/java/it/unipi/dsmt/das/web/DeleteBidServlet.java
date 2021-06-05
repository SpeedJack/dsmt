package it.unipi.dsmt.das.web;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.model.*;
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

@WebServlet("/deleteBid")
public class DeleteBidServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private AuctionManager auctionManager;
    public DeleteBidServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher;
        if (session != null) {
            User sessionUser = (User)session.getAttribute("user");
            request.setAttribute("username", sessionUser.getUsername());
            request.setAttribute("ID", sessionUser.getId());

            long auctionID = Long.parseLong(request.getParameter("auctionID"));
            long bidID = Long.parseLong(request.getParameter("bidID"));

            BidStatus status = auctionManager.deleteBid(auctionID, bidID);
            response.getWriter().println(status.toString());
            request.setAttribute("auctionID", auctionID);
            // dispatcher = request.getServletContext().getRequestDispatcher("/detailedCustomer");
        }
        else {
            dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);
        }
    }

}