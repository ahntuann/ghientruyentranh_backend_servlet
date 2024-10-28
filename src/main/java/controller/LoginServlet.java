package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import dao.UsersDAO;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Users;

/**
 *
 * @author macbook
 */
@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

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
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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
        setCorsHeaders(response);

        JSONObject requestBody = parseRequestBody(request);
        String userName = requestBody.getString("name");
        String password = requestBody.getString("password");

        try {
            handleLogin(userName, password, response, request);
        } catch (SQLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    private JSONObject parseRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return new JSONObject(sb.toString().trim());
    }

    private void handleLogin(String userName, String password, HttpServletResponse response, HttpServletRequest request)
            throws SQLException, IOException {
        PrintWriter out = response.getWriter();

        UsersDAO userDAO = new UsersDAO();
        Users userByMail = userDAO.getUserByEmail(userName);
        Users userByUserName = userDAO.getUsersByName(userName);
        
        Map<String, Object> jsonResponse = new HashMap<>();

        if (userByMail != null && BCrypt.checkpw(password, userByMail.getPassword())) {
            HttpSession ss = request.getSession();
            ss.setAttribute("user", userByMail);
            
            jsonResponse.put("success", true);
            jsonResponse.put("role", userByMail.getRole());
            jsonResponse.put("user", userByMail);
        } else if (userByUserName != null && BCrypt.checkpw(password, userByUserName.getPassword())) {
            HttpSession ss = request.getSession();
            ss.setAttribute("user", userByUserName);
            
            jsonResponse.put("success", true);
            jsonResponse.put("role", userByUserName.getRole());
            jsonResponse.put("user", userByUserName);
        } else {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Invalid username or password");
        }
        
        Gson gson = new Gson();
        out.print(gson.toJson(jsonResponse));
        
        out.flush();
    }
}
