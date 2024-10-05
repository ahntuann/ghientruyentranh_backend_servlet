package controller;

import com.google.gson.Gson;
import com.mycompany.testmaven.DatabaseConnection;
import dao.DepartmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.Department;

@WebServlet("/departments")
public class DepartmentController extends HttpServlet {
    private DatabaseConnection databaseConnection;
    private Connection con;

    @Override
    public void init() throws ServletException {
        databaseConnection = new DatabaseConnection();
        con = databaseConnection.connect();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Cấu hình CORS
        response.setHeader("Access-Control-Allow-Origin", "*"); // Thay '*' bằng tên miền cụ thể nếu cần
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        try {
            DepartmentDAO departmentDAO = new DepartmentDAO(con);
            List<Department> departments = departmentDAO.getAllDepartments();
            Gson gson = new Gson();
            String json = gson.toJson(departments);
            response.getWriter().write(json);
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy dữ liệu phòng ban: " + e.getMessage());
        }
    }
}
