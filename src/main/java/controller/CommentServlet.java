/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Comment;
import dao.CommentDAO;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import model.Users;
import org.json.JSONObject;

/**
 *
 * @author macbook
 */
@WebServlet(name = "CommentServlet", urlPatterns = {"/comments"})
public class CommentServlet extends HttpServlet {

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
            out.println("<title>Servlet CommentServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CommentServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Cấu hình CORS
        setCORHeader(response);
        
        int chapterID = -1;
        try {
            String xChapterID = request.getParameter("chapterID");
            chapterID = Integer.parseInt(xChapterID);
        } catch (Exception e) {
        }

        try {
            if (chapterID > -1) {
                getAllCommentsByChapterID(chapterID, request, response);
            }
        } catch (Exception e) {
        }

    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORHeader(response);
        
        response.setStatus(HttpServletResponse.SC_OK);
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Cấu hình CORS
        setCORHeader(response);

        handlePostComment(request, response);
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
    
    private void setCORHeader(HttpServletResponse response) {
        // Cấu hình CORS
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
    } 

    private void getAllCommentsByChapterID(int chapterID, HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        setCORHeader(response);

        CommentDAO commentDao = new CommentDAO();
        List<Comment> comments = commentDao.getAllCommentsByChapterID(chapterID);
        Gson gson = new Gson();

        String json = gson.toJson(comments);
        response.getWriter().write(json);
    }
    
    private JSONObject parseBodyRequest(HttpServletRequest request) throws IOException{
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        return new JSONObject(sb.toString().trim());
    }
    
    private void handlePostComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        Map<String, Object> res = new HashMap<>();
        Gson gson = new Gson();
        
        System.out.println(session);
        
        if (session == null || session.getAttribute("user") == null) {
            res.put("success", "false");
            res.put("message", "You have to log in to comment!");
            out.print(gson.toJson(res));
        } else {
            JSONObject requestBody = parseBodyRequest(request);
            
            String content = requestBody.getString("content");
            int chapterID = requestBody.getInt("chapterID");
            int mangaID = requestBody.getInt("mangaID");
            int userID = requestBody.getInt("userID");
            
            CommentDAO commentDAO = new CommentDAO();
            
            Comment insertedComment = commentDAO.insertComment(userID, chapterID, mangaID, content);
            System.out.println("inserted: " +insertedComment);
            if (insertedComment != null) {
                res.put("success", "true");
                res.put("comment", insertedComment);
            } else {
                res.put("succes", "false");
            }
            
            res.put("message", "You can now comment!");
            

            out.print(gson.toJson(res));
        }
        out.flush();
        out.close();
    }


}
