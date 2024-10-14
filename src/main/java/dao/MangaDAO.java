/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import dao.mydao.MyDAO;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Manga;
import util.StringUtil;

/**
 *
 * @author macbook
 */
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
}
