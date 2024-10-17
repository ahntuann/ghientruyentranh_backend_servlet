
package controller;

import com.google.gson.Gson;
import dao.ChapterImageDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet(name = "GetChapterImages", urlPatterns = {"/chapterimages"})
public class GetChapterImages extends HttpServlet {

    private void getUrlImageByChapterId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ChapterImageDAO chapterImgDao = new ChapterImageDAO();
            String chapterId_raw = request.getParameter("chapterId");
            String offset_raw = request.getParameter("offset");
            String limit_raw = request.getParameter("limit");

            int chapterId = Integer.parseInt(chapterId_raw);
            int offset = Integer.parseInt(offset_raw);
            int limit = Integer.parseInt(limit_raw);

            List<String> imgUrl = chapterImgDao.getUrlImageByChapterId(chapterId, offset, limit);
            Gson gson = new Gson();
            String json = gson.toJson(imgUrl);
            response.getWriter().write(json);

        } catch (Exception e) {
            System.out.println("Lỗi khi lấy dữ liệu hình ảnh: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Cấu hình CORS
        response.setHeader("Access-Control-Allow-Origin", "*"); // Thay '*' bằng tên miền cụ thể nếu cần
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        try {
            String xChapterId = request.getParameter("chapterId");
            String xOffset = request.getParameter("offset");
            String xLimit = request.getParameter("limit");
            if(xChapterId != null && xOffset != null && xLimit != null){
                getUrlImageByChapterId(request, response);
                return;
            }
        } catch (Exception e) {
            System.out.println(e);
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
