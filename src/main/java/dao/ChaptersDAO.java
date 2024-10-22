package dao;

import dao.mydao.MyDAO;
import java.util.ArrayList;
import java.util.List;
import model.Chapters;
import java.sql.ResultSet;
import java.sql.Date;

public class ChaptersDAO extends MyDAO {

    //phuong thuc tao ra Chapters
    private List<Chapters> createChapters(ResultSet rs) {
        List<Chapters> chapters = new ArrayList<>();

        try {
            while (rs.next()) {
                int chapterId = rs.getInt("chapter_id");
                int storyId = rs.getInt("story_id");
                String chapterNumber = rs.getString("chapter_number");
                Chapters chapter = new Chapters(chapterId,storyId,chapterNumber);
                System.out.println(chapter);
                chapters.add(chapter);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return chapters;
    }
    
    private Chapters createChapter(ResultSet rs) {
        Chapters chapter = new Chapters();

        try {
            while (rs.next()) {
                int chapterId = rs.getInt("chapter_id");
                int storyId = rs.getInt("story_id");
                String chapterNumber = rs.getString("chapter_number");
                chapter = new Chapters(chapterId,storyId,chapterNumber);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return chapter;
    }
    
    public Chapters getChapterById(int chapterID) {
        Chapters chapter = new Chapters();
        String xSql = "select chapter_id,story_id,chapter_number from Chapters where chapter_id = ?";
        
        try {
            ps = con.prepareStatement(xSql);
            ps.setInt(1, chapterID);
            rs = ps.executeQuery();
            chapter = createChapter(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return chapter;
    }
    
    //lấy danh sách các chapter của 1 truyện dựa vào storyId

    public List<Chapters> getChapterByStoryId(int storyId) {
        List<Chapters> chapters = new ArrayList<>();
        String xSql = "select chapter_id,story_id,chapter_number from Chapters where story_id = ?";
        try {
            ps = con.prepareStatement(xSql);
            ps.setInt(1, storyId);
            rs = ps.executeQuery();
            chapters = createChapters(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
        return chapters;
    }

}
