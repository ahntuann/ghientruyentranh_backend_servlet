/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.mydao.MyDAO;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import model.Comment;
import java.util.List;

/**
 *
 * @author macbook
 */
public class CommentDAO extends MyDAO {

    private List<Comment> createCommentList(ResultSet rs) {
        List<Comment> comments = new ArrayList<>();

        try {
            while (rs.next()) {
                int id = rs.getInt("comment_id");
                int userId = rs.getInt("user_id");
                int storyId = rs.getInt("story_id");
                int chapterId = rs.getInt("chapter_id");
                String content = rs.getString("content");
                Date createdAt = rs.getDate("created_at");

                Comment comment = new Comment(id, userId, storyId, chapterId, content, createdAt);

                comments.add(comment);
            }
        } catch (Exception e) {
            System.out.println("Error when create comment list");
        }

        return comments;
    }

    public List<Comment> getAllCommentsByChapterID(int chapterID) {
        List<Comment> comments = new ArrayList<>();
        xSql = "select * from comments where chapter_id = ?";

        try {
            ps = con.prepareStatement(xSql);
            ps.setInt(1, chapterID);
            rs = ps.executeQuery();

            comments = createCommentList(rs);
        } catch (Exception e) {
            System.out.println("Error when get comments by chapterID");
        }

        return comments;
    }

    public Comment insertComment(int userID, int chapterID, int storyID, String content) {
        String xSql = "INSERT INTO Comments (user_id, chapter_id, story_id, content, created_at) "
                + "OUTPUT INSERTED.comment_id, INSERTED.user_id, INSERTED.chapter_id, INSERTED.story_id, INSERTED.content, INSERTED.created_at "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            Date currentDate = new Date(System.currentTimeMillis());

            ps = con.prepareStatement(xSql);
            ps.setInt(1, userID);
            ps.setInt(2, chapterID);
            ps.setInt(3, storyID);
            ps.setString(4, content);
            ps.setDate(5, currentDate);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Comment newComment = new Comment();
                newComment.setId(rs.getInt("comment_id")); 
                newComment.setUserId(rs.getInt("user_id"));
                newComment.setChapterId(rs.getInt("chapter_id"));
                newComment.setStoryId(rs.getInt("story_id"));
                newComment.setContent(rs.getString("content"));
                newComment.setCreatedAt(rs.getDate("created_at"));

                return newComment; 
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null; 
    }

}
