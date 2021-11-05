/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import context.DBContext;
import dao.DAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Post;

/**
 *
 * @author HP
 */
public class AddPostServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddPostServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddPostServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("home");
        }
        String titleSearch = request.getParameter("titleSearch") == null
                ? ""
                : request.getParameter("titleSearch");
        String title = request.getParameter("title");
        String image = request.getParameter("image");
        String content = request.getParameter("content");
        Date date = new Date();

        DBContext db = new DBContext();
        DAO dao = new DAO(db);

        Post post = new Post(title, image, content, date);

        dao.addPost(post);

        int rowCount = dao.countPost(titleSearch);

        String page_raw = request.getParameter("txtPage");
        page_raw = (page_raw == null) ? "1" : page_raw;

        int pageIndex = Integer.parseInt(page_raw);
        int maxPage = rowCount / 6 + (rowCount % 6 > 0 ? 1 : 0);

        List<Post> list = dao.getPosts(pageIndex, titleSearch);

        request.setAttribute("list", list);
        request.setAttribute("titleSearch", titleSearch);
        request.setAttribute("maxPage", maxPage);
        request.setAttribute("pageIndex", pageIndex);
        request.setAttribute("addSuccess", "1");
        request.getRequestDispatcher("ListPost.jsp").forward(request, response);
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String admin = (String) session.getAttribute("admin");
        return admin != null;
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
