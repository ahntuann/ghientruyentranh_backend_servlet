package dao;

import dao.mydao.MyDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ChapterImage;

public class ChapterImageDAO extends MyDAO {

    //phương thức lấy ra url_image theo chapterID
    public List<String> getUrlImageByChapterId(int chapterId, int offset, int limit) throws SQLException {
        List<String> imageUrl = new ArrayList<>();
        String xSql = "SELECT ci.image_url "
                + "FROM Chapters_Image ci "
                + "JOIN Chapters c ON c.chapter_id = ci.chapter_id "
                + "WHERE c.chapter_id = ? "
                + "ORDER BY ci.PageNumber ASC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            ps = con.prepareStatement(xSql);
            ps.setInt(1, chapterId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                String imgUrl = rs.getString("image_url");
                imageUrl.add(imgUrl);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy dữ liệu hình ảnh: " + e.getMessage());
            throw e;
        }
        return imageUrl;
    }

}
