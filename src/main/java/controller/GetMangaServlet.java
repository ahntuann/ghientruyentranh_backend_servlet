/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dao.MangaDAO;
import model.Manga;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "GetMangaServlet", urlPatterns = {"/mangas"})
public class GetMangaServlet extends HttpServlet {

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
            out.println("<title>Servlet GetMangaServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetMangaServlet at " + request.getContextPath() + "</h1>");
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Cấu hình CORS
        response.setHeader("Access-Control-Allow-Origin", "*"); // Thay '*' bằng tên miền cụ thể nếu cần
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        //xử lý lấy ra 10 truyện mới nhất
        
            
        try {
            MangaDAO mangaDao = new MangaDAO();
            String xId = request.getParameter("id");

            // Kiểm tra nếu có id thì trả về manga theo id
            if (xId != null && !xId.isEmpty()) {
                int id_real = Integer.parseInt(xId);
                Manga manga = mangaDao.getMangasById(id_real);
                if (manga != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(manga);
                    response.getWriter().write(json);
                    return; // Dừng lại sau khi trả về manga theo id
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Manga not found");
                    return;
                }
            }

            // Nếu không có id thì thực hiện tìm kiếm theo tên và tác giả
            String xName = request.getParameter("name");
            String xAuthor = request.getParameter("author");

            List<Manga> mangas = new ArrayList<>();
            if (xAuthor != null && xName != null) {
                List<Manga> tmp1 = mangaDao.getMangaByAuthor(xAuthor);
                List<Manga> tmp2 = mangaDao.getMangaByName(xName);

                tmp1.addAll(tmp2);
                mangas = tmp1;
            } else if (xName != null) {
                mangas = mangaDao.getMangaByName(xName);
            } else if (xAuthor != null) {
                mangas = mangaDao.getMangaByAuthor(xAuthor);
            } else {
                mangas = mangaDao.getAllManga();
            }

            Gson gson = new Gson();
            String json = gson.toJson(mangas);
            response.getWriter().write(json);
        } catch (Exception e) {
            System.out.println(e);
        }
        
//        //Loc ra 10 truyen duoc update moi nhat
//        MangaDAO mangaDao = new MangaDAO();
//        List<Manga> new_stories = mangaDao.getNewMangas();
//        Gson gson = new Gson();
//        String json = gson.toJson(new_stories);
//        response.getWriter().write(json);

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
