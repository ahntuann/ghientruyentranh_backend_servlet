/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import dao.mydao.MyDAO;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Manga;
import util.StringUtil;

public class MangaDAO extends MyDAO {

    private List<Manga> createMangas(ResultSet rs) {
        List<Manga> mangas = new ArrayList<>();

        try {
            while (rs.next()) {
                int id = rs.getInt("story_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String desc = rs.getString("description");
                String coverImageURL = rs.getString("cover_image");
                String status = rs.getString("status");
                Date createdAt = rs.getDate("created_at");
                Date updatedAt = rs.getDate("updated_at");
                int likeCount = rs.getInt("like_count");

                Manga manga = new Manga(id, title, author, desc, coverImageURL, status, createdAt, updatedAt, likeCount);

                mangas.add(manga);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return mangas;
    }

    public List<Manga> getAllManga() {
        List<Manga> mangas = new ArrayList<>();
        xSql = "select * from stories";

        try {
            ps = con.prepareStatement(xSql);
            rs = ps.executeQuery();

            mangas = createMangas(rs);

        } catch (Exception e) {
            System.out.println(e);
        }

        return mangas;
    }

    public List<Manga> getMangaByName(String xName) {
        List<Manga> mangas = new ArrayList<>();
        xSql = "select * from stories where title like ? COLLATE Vietnamese_CI_AI";

        try {
            ps = con.prepareStatement(xSql);
            ps.setString(1, "%" + xName + "%");
            rs = ps.executeQuery();

            System.out.println(StringUtil.removeAccent(xName));

            mangas = createMangas(rs);
        } catch (Exception e) {
            System.out.println(e);
        }

        return mangas;
    }

    public List<Manga> getMangaByAuthor(String xAuthor) {
        List<Manga> mangas = new ArrayList<>();
        xSql = "select * from stories where author like ? COLLATE Vietnamese_CI_AI";

        try {
            ps = con.prepareStatement(xSql);
            ps.setString(1, "%" + xAuthor + "%");
            rs = ps.executeQuery();

            mangas = createMangas(rs);
        } catch (Exception e) {
            System.out.println(e);
        }

        return mangas;
    }

    //lay ra 1 manga dua vao id
    public Manga getMangasById(int id) {
        String xSql = "SELECT * FROM Stories WHERE story_id = ?";
        Manga story_selected = null;
        try {
            ps = con.prepareStatement(xSql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                int story_id = rs.getInt("story_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String description = rs.getString("description");
                String cover_image = rs.getString("cover_image");
                String status = rs.getString("status");
                java.sql.Date created_at = rs.getDate("created_at");
                java.sql.Date updated_at = rs.getDate("updated_at");
                int like_count = rs.getInt("like_count");
                story_selected = new Manga(story_id, title, author, description,
                        cover_image, status, created_at, 
                        updated_at, like_count);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return story_selected;

    }
    
    //Lấy ra 10 truyện được update mới nhất
    public List<Manga> getNewMangas(){
        String xSql = "SELECT TOP 10 * \n"
                + "FROM Stories\n"
                + "ORDER BY updated_at DESC;";
        List<Manga> new_mangas = new ArrayList<>();
        try {
            ps = con.prepareStatement(xSql);
            rs = ps.executeQuery();
            new_mangas = createMangas(rs);

        } catch (Exception e) {
            System.out.println(e);
        }
        return new_mangas;
    }

}
