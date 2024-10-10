/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import dao.mydao.MyDAO;
import java.sql.Date;
import java.util.ArrayList;
import model.Manga;

/**
 *
 * @author macbook
 */
public class MangaDAO extends MyDAO{
    public List<Manga> getAllManga() {
        List<Manga> mangas = new ArrayList<>();
        xSql = "select * from stories";
        
        try {
            ps = con.prepareStatement(xSql);
            rs = ps.executeQuery();
            
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
        } catch(Exception e) {
            System.out.println(e);
        }
        
        return mangas;
    }
}
