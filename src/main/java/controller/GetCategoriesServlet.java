
package controller;

import com.google.gson.Gson;
import dao.CategoriesDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import model.Categories;

/**
 *
 * @author Admin
 */
@WebServlet(name = "GetCategoriesServlet", urlPatterns = {"/categories"})
public class GetCategoriesServlet extends HttpServlet {

    // Hàm để xử lý lấy danh sách categories
    private void getAllCategories(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Cấu hình CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        CategoriesDAO categoriesDAO = new CategoriesDAO();
        List<Categories> categories = categoriesDAO.getAllCategories();
        Gson gson = new Gson();
        String json = gson.toJson(categories);
        response.getWriter().write(json);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            getAllCategories(request, response);
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy dữ liệu categories: " + e.getMessage());
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
