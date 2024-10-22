/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.gson.Gson;
import dao.UsersDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.SQLException;

import model.Users;
import service.FirebaseService;

@WebServlet(name = "GoogleCallBackServlet", urlPatterns = {"/google-callback"})
public class GoogleCallBackServlet extends HttpServlet {

    private void googleCallBack(String idToken, HttpServletResponse response) throws IOException, FirebaseAuthException, SQLException {
        FirebaseService firebaseService = new FirebaseService();
        FirebaseToken decodedToken = firebaseService.verifyToken(idToken);
        //lấy thông tin của user từ token 
        String email = decodedToken.getEmail();
        String username = decodedToken.getName();
        String avatar = decodedToken.getPicture();

        UsersDAO userDao = new UsersDAO();
        // Kiểm tra xem người dùng đã tồn tại chưa
        Users existingUser = userDao.getUserByEmail(email);
        response.setContentType("application/json");
        if (existingUser == null) {
            //chưa tồn tại, tạo mới 1 user
            Users newUser = new Users();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setAvatar(avatar);
            newUser.setRole("user");
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            newUser.setCreated_at(currentDate);
            newUser.setUpdated_at(currentDate);
            newUser.setVip(0);
            // Insert user vào database
            userDao.insertUser(newUser);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Đăng nhập thành công\", \"user\": " + new Gson().toJson(newUser) + "}");
        } else {
            // Người dùng đã tồn tại, đăng nhập trực tiếp
            // Trả về thông tin người dùng đã tồn tại
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Đăng nhập thành công\", \"user\": " + new Gson().toJson(existingUser) + "}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy idToken từ request
        String idToken = request.getParameter("token");

        // Kiểm tra xem idToken có hợp lệ không
        if (idToken == null || idToken.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Thiếu idToken\"}");
            return;
        }

        try {
            // Gọi hàm xử lý đăng nhập Google
            googleCallBack(idToken, response);
        } catch (FirebaseAuthException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Token không hợp lệ\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Lỗi trong cơ sở dữ liệu\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Đã xảy ra lỗi\"}");
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
