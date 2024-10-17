package controller;

import com.google.gson.Gson;
import dao.CategoriesDAO;
import dao.ChaptersDAO;
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
import model.Categories;
import model.Chapters;

@WebServlet(name = "GetMangaServlet", urlPatterns = {"/mangas"})
public class GetMangaServlet extends HttpServlet {

    // Phương thức lấy ra tất cả các truyện
    private void getAllMangas(HttpServletResponse response) throws IOException {
        MangaDAO mangaDao = new MangaDAO();
        List<Manga> allMangas = mangaDao.getAllManga(); // Giả sử phương thức này đã được định nghĩa trong MangaDAO
        Gson gson = new Gson();
        String json = gson.toJson(allMangas);
        response.getWriter().write(json);
    }

    // Phương thức xử lý lấy ra 10 truyện mới nhất
    private void getNewestMangas(HttpServletResponse response) throws IOException {
        MangaDAO mangaDao = new MangaDAO();
        List<Manga> newMangas = mangaDao.getNewMangas();
        Gson gson = new Gson();
        String json = gson.toJson(newMangas);
        response.getWriter().write(json);
    }

    //Phương thức xử lý lấy truyện theo ID
    private void getMangaById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MangaDAO mangaDao = new MangaDAO();
        String xId = request.getParameter("id");
        if (xId != null && !xId.isEmpty()) {
            int id_real = Integer.parseInt(xId);
            Manga manga = mangaDao.getMangasById(id_real);
            if (manga != null) {
                Gson gson = new Gson();
                String json = gson.toJson(manga);
                response.getWriter().write(json);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Manga not found");
            }
        }
    }
    
    //lấy ra  tất cả các chapter của 1 truyện dựa vào id truyện
    private void getChaptersByMangaID(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String xId = request.getParameter("id");
        String includedChapters = request.getParameter("chapters");
        ChaptersDAO chaptersDao = new ChaptersDAO();
        if (xId != null && !xId.isEmpty() && includedChapters != null && includedChapters.equals("true")) {
            int id_real = Integer.parseInt(xId);
            List<Chapters> chapters = chaptersDao.getChapterByStoryId(id_real);
            Gson gson = new Gson();
            String json = gson.toJson(chapters);
            response.getWriter().write(json);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Manga ID is missing");
        }
    }

    // Phương thức xử lý tìm kiếm truyện theo tên và tác giả
    private void searchManga(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MangaDAO mangaDao = new MangaDAO();
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
    }

    //Phương thức lấy ra những truyện có cùng thể loại 
    private void getMangasSameCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            MangaDAO mangaDao = new MangaDAO();
            String categoryId = request.getParameter("category_id");
            if (categoryId == null || categoryId.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is missing or invalid");
                return;
            }
            int id_real = Integer.parseInt(categoryId);
            List<Manga> mangas = mangaDao.getMangasByCategoryID(id_real);
            if (mangas.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No mangas found for this category");
                return;
            }
            Gson gson = new Gson();
            String json = gson.toJson(mangas);
            response.getWriter().write(json);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    //Phương thức lấy ra category của manga cụ thể khi biết mangaID
    private void getCategoriesByMangasId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            CategoriesDAO categriesDao = new CategoriesDAO();
            String mangaId = request.getParameter("id");
            if (mangaId == null || mangaId.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is missing or invalid");
                return;
            }
            int id_real = Integer.parseInt(mangaId);
            List<Categories> categories = categriesDao.getCategoriesByMangasId(id_real);
            if (categories.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No categories found for this manga");
                return;
            }
            Gson gson = new Gson();
            String json = gson.toJson(categories);
            response.getWriter().write(json);
        } catch (Exception e) {
            System.out.println(e);
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

            // Lấy tất cả các tham số cần thiết
            String xId = request.getParameter("id");
            String isNewest = request.getParameter("newest");
            String xName = request.getParameter("name");
            String xAuthor = request.getParameter("author");
            String xCategoryId = request.getParameter("category_id");
            String includedChapters = request.getParameter("chapters");
            String category = request.getParameter("category");

            //nếu có id và chapters=true thì lấy ra chapters của truyện đó
            if (includedChapters != null && includedChapters.equals("true")) {
                getChaptersByMangaID(request, response);
                return;
            }
            //nếu có id và category=true thì lấy ra thể loại của truyện đó
            if (category != null && category.equals("true")) {
                getCategoriesByMangasId(request, response);
                return;
            }
            // Nếu chỉ có tham số id thì lấy truyện theo id
            if (xId != null) {
                getMangaById(request, response);
                return;
            }
            // Nếu có tham số newest=true thì lấy 10 truyện mới nhất
            if (isNewest != null && isNewest.equals("true")) {
                getNewestMangas(response);
                return;
            }
            // Nếu có tham số name hoặc author thì thực hiện tìm kiếm
            if (xName != null || xAuthor != null) {
                searchManga(request, response);
                return;
            }
            //neu nguoi dung an vao 1 the loai truyen, tra ra truyen cung the loai
            if (xCategoryId != null) {
                getMangasSameCategory(request, response);
                return;
            }
            // Nếu không có tham số nào, thì trả về tất cả các truyện
            getAllMangas(response);

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
