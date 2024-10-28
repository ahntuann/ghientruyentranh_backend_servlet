package controller;

import dao.EmailSenderDAO;
import dao.UsersDAO;
import util.RandomString;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import model.VerifyCode;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private final Map<String, VerifyCode> verificationCodes = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void init() throws ServletException {
        super.init();
        scheduler.scheduleAtFixedRate(this::removeExpiredCodes, 5, 5, TimeUnit.MINUTES);
    }

    @Override
    public void destroy() {
        super.destroy();
        scheduler.shutdown();
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK); // Đáp ứng preflight request với mã 200 OK
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    private void removeExpiredCodes() {
        verificationCodes.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    private String generateVerificationCode(String email) {
        String code = RandomString.generateVerificationCode();
        LocalDateTime expiredTime = LocalDateTime.now().plusMinutes(5);
        verificationCodes.put(email, new VerifyCode(code, expiredTime));
        return code;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);

        handleVerificationRequest(request, response);
    }

    private void handleVerificationRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String sendCode = request.getParameter("sendCode");
            String email = request.getParameter("email");

            if ("true".equals(sendCode) && email != null) {
                String code = generateVerificationCode(email);
                EmailSenderDAO.sendVerificationEmail(email, code);
                sendJsonResponse(response, true, "Mã xác thực đã được gửi tới email của bạn.");
            } else {
                sendJsonResponse(response, false, "Thông tin không hợp lệ.");
            }
        } catch (Exception e) {
            sendJsonResponse(response, false, "Lỗi xử lý yêu cầu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);

        try {
            JSONObject requestBody = parseRequestBody(request);
            String username = requestBody.optString("username");
            String password = requestBody.optString("password");
            String email = requestBody.optString("email");
            String authCode = requestBody.optString("code");

            if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !authCode.isEmpty()) {
                handleRegistration(response, username, password, email, authCode);
            } else {
                sendJsonResponse(response, false, "Dữ liệu đầu vào không hợp lệ.");
            }
        } catch (Exception e) {
            sendJsonResponse(response, false, "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleRegistration(HttpServletResponse response, String username, String password, String email, String authCode)
            throws IOException {
        if (verifyCode(email, authCode)) {
            if (registerUser(username, email, password)) {
                sendJsonResponse(response, true, "Đăng ký thành công!");
            } else {
                sendJsonResponse(response, false, "Đăng ký thất bại. Vui lòng thử lại.");
            }
        } else {
            sendJsonResponse(response, false, "Mã xác thực không đúng hoặc đã hết hạn!");
        }
    }

    private boolean verifyCode(String email, String authCode) {
        return Optional.ofNullable(verificationCodes.get(email))
                .map(code -> code.getAuthCode().equals(authCode))
                .orElse(false);
    }

    private boolean registerUser(String username, String email, String password) {
        try {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            UsersDAO usersDAO = new UsersDAO();
            boolean success = usersDAO.insertUser(username, email, hashedPassword);
            verificationCodes.remove(email); // Xóa mã xác thực sau khi đăng ký thành công
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private JSONObject parseRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return new JSONObject(sb.toString().trim());
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", success);
        jsonResponse.put("message", message);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
    }
}
