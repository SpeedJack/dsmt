package it.unipi.dsmt.das.web;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.model.*;

import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@MultipartConfig()
@WebServlet(name = "AuctionServlet", urlPatterns = {"/auction"} )
public class AuctionServlet extends HttpServlet {
    @EJB
    AuctionManager auctionManager;
    @EJB
    AuctionStatePublisher publisher;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if(action == null)
            action = "list";
        switch (action) {
            case "sell":
                getCreateAuction(request,response);
                break;
            case "detail":
                getAuctionDetail(request, response);
                break;
            case "list":
            default:
                getAuctionList(request, response);
                break;

        }
    }

    private void getCreateAuction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String destPage;
        if (session != null) {
            destPage = "sell.jsp";
            User sessionUser = (User)session.getAttribute("user");
            request.setAttribute("username", sessionUser.getUsername());
            request.setAttribute("ID", sessionUser.getId());
        }
        else
            destPage = "login.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request, response);
    }

    private void getAuctionList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User sessionUser = (User)session.getAttribute("user");
        String target = request.getParameter("target");
        String message = "";
        if (target == null)
            target = "customer";
        String pageParam = request.getParameter("page");
        int page = 1;
        if(pageParam != null && !pageParam.isEmpty()){
            page = Integer.parseInt(pageParam);
            if (page <= 1)
                page=1;
        }

        AuctionList auctionList;
        if(target.equals("seller"))
            auctionList = auctionManager.auctionAgentList(sessionUser.getId());
        else if (target.equals("offers"))
            auctionList = auctionManager.auctionBidderList(sessionUser.getId());
        else
            auctionList = auctionManager.auctionsList(page);
        List<Auction> list;
        if(auctionList == null){
            list = new ArrayList<>();
            if("offers".equals(target))
                message = "Currently you have not made any offer!";
            else if("seller".equals(target))
                message = "You are not selling anything yet!";
            else
                message = "There is no auction yet, be the first seller!";
        } else
            list = auctionList.getList();
        request.setAttribute("auctionList", list);
        request.setAttribute("message", message);

        RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
        dispatcher.forward(request, response);
    }

    private void getAuctionDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long auctionId = Long.parseLong(request.getParameter("auctionID"));
        boolean update = Boolean.parseBoolean(request.getParameter("update"));
        String destination = update ? "auctionDetail.jsp" : "detailed.jsp";
        HttpSession session = request.getSession(false);
        User sessionUser = (User)session.getAttribute("user");
        AuctionData data = auctionManager.selectAuction(auctionId, sessionUser.getId());
        AuctionState state = publisher.getState(auctionId);
        String target = "customer";
        if(data != null && data.getAuction() != null){
            if(data.getAuction().getAgent() == sessionUser.getId()){
                target = "seller";
            }
            String date = Long.toString(data.getAuction().getEndDate() * 1000);
            BidList list = data.getList();
            List<Bid> bids = null;
            if (list != null) {
                bids = list.getList();
            }
            request.setAttribute("state", state);
            request.setAttribute("auction", data.getAuction());
           //If the auction is over
            if(data.getAuction().getEndDate() <= Instant.now().getEpochSecond() &&
            data.getAuction().getAgent() != sessionUser.getId())
            {
                destination = "auctionResult.jsp";
                Set<Bid> winnings = state == null ? new HashSet<>() : state.getWinning(sessionUser);
                boolean winner = (winnings.size() > 0);
                request.setAttribute("status", winner ? "You win!" : "You Lose!");
                request.setAttribute("bids", winnings);

            } else { //Otherwise continue with auction details
                request.setAttribute("target", target);
                request.setAttribute("bids", bids);
                request.setAttribute("date", date);
            }
        } else {
            destination = "result_page.jsp";
            request.setAttribute("status", "Auction Not Found!");
            request.setAttribute("message","This auction has been deleted and it's no more available");
        }
        request.getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if(action == null)
            action = "";
        switch (action) {
            case "delete":
                doDeleteAuction(request, response);
                break;
            case "create":
                doCreateAuction(request, response);
                break;
            case "detail":
                getAuctionDetail(request, response);
                break;
            default:
                request.setAttribute("state", "We are sorry!");
                request.setAttribute("message", "your request cannot be served");
                request.getRequestDispatcher("error_page").forward(request, response);
                break;
        }
    }

    private void doDeleteAuction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User sessionUser = (User)session.getAttribute("user");
        String destPage = "result_page.jsp";
        long auctionID = Long.parseLong(request.getParameter("auctionID"));
        AuctionData auction = auctionManager.selectAuction(auctionID, sessionUser.getId());
        String status = "";
        String message = "";
        if (auction.getAuction().getAgent() == sessionUser.getId()){
            if(auctionManager.deleteAuction(auctionID).equals("ok")) {
                status = "Done!";
                message = "The auction has been deleted!";
            } else {
                status = "Error!";
                message = "The auction cannot be deleted!";
            }
        } else {
            status = "Permissions Error!";
            message = "You are not the agent for this auction!";
        }

        request.setAttribute("message", message);
        request.setAttribute("status", status);
        request.getRequestDispatcher(destPage).forward(request, response);
    }

    private void doCreateAuction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String,String[]> map = request.getParameterMap();
        User sessionUser = (User)session.getAttribute("user");
        String day = request.getParameter("day");
        String hour = request.getParameter("hour");
        long timestamp = 0;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            timestamp = Math.floorDiv(df.parse(day + " " + hour).getTime(),1000);
        }
        catch (ParseException e) {
            timestamp = 0;
        }
        Auction auction = new Auction( sessionUser.getId(),
                request.getParameter("name"),
                "noImage.jpg",
                request.getParameter("description"),
                timestamp,
                Double.parseDouble(request.getParameter("minimum_bid")),
                Double.parseDouble(request.getParameter("minimum_raise")),
                Long.parseLong(request.getParameter("object")));

        Part filePart = request.getPart("userfile");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        InputStream fileContent = filePart.getInputStream();

        BufferedImage sourceimage = ImageIO.read(fileContent);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ImageIO.write(sourceimage, "png", bytes);
        String binaryImage = Base64.getEncoder().encodeToString(bytes.toByteArray());

        auction.setImage(binaryImage);

        String destPage = "result_page.jsp";
        String message;
        String status;
        String res = auctionManager.createAuction(auction);
        if(res.equals("ok")){
            status = "Done!";
            message = "Object correctly inserted";
        } else {
            status = "Error!";
            message = "An error occurred while inserting a new object";
        }
        request.setAttribute("status", status);
        request.setAttribute("message", message);

        request.getRequestDispatcher(destPage).forward(request, response);
    }
}
