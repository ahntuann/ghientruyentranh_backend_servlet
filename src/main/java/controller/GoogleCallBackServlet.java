package controller;
//import
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
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.SQLException;
import model.Users;
import service.FirebaseService;

@WebServlet(name = "GoogleCallBackServlet", urlPatterns = {"/google-callback"})
public class GoogleCallBackServlet extends HttpServlet {

    private void googleCallBack(String idToken, HttpServletRequest request, HttpServletResponse response) throws IOException, FirebaseAuthException, SQLException {
        FirebaseService firebaseService = new FirebaseService();
        FirebaseToken decodedToken = firebaseService.verifyToken(idToken);
        //lấy thông tin người dùng thông qua token
        String email = decodedToken.getEmail();
        String username = decodedToken.getName();
        String avatar = decodedToken.getPicture();
        //khỏi tạo đối tượng userDao
        UsersDAO userDao = new UsersDAO();
        //lấy user dựa vào email, nếu null là chưa tồn tại
        Users existingUser = userDao.getUserByEmail(email);
        Users user = existingUser;
        
        response.setContentType("application/json");
        
        
        if (existingUser == null) {
            //nếu không tồn tại, khởi tạo 1 user mới
            user = new Users();
            user.setUsername(username);
            user.setEmail(email);
            user.setAvatar(avatar);
            user.setRole("user");
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            user.setCreated_at(currentDate);
            user.setUpdated_at(currentDate);
            user.setVip(0);
            userDao.insertUser(user);
        }
        
        // khỏi tạo session để lưu thông tin người dùng
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        
        // nếu đã tồn tại user, thông báo thành công
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\": \"Đăng nhập thành công\", \"user\": " + new Gson().toJson(user) + "}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //lấy token từ fe
        String idToken = request.getParameter("token");
        //check token
        if (idToken == null || idToken.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Thiếu idToken\"}");
            return;
        }
        //gọi hàm googleCallBack và bắt lỗi
        try {
            googleCallBack(idToken, request, response);
        } catch (FirebaseAuthException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Token không hợp lệ\"}   ");
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
    }

}
