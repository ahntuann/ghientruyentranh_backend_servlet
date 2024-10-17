//các thao tác với table categories
package dao;

import dao.mydao.MyDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Categories;

public class CategoriesDAO extends MyDAO {

    private List<Categories> createCategories(ResultSet rs) {
        List<Categories> categories = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("category_id");
                String categoryName = rs.getString("category_name");
                Categories category = new Categories(id, categoryName);
                categories.add(category);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return categories;
    }

    //getAll Categories
    public List<Categories> getAllCategories() throws SQLException {
        String xSql = "SELECT * FROM Categories";
        List<Categories> categories = new ArrayList<>();
        try {
            ps = con.prepareStatement(xSql);
            rs = ps.executeQuery();
            categories = createCategories(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
        return categories;
    }

    //getCategoriesByMangasId
    public List<Categories> getCategoriesByMangasId(int storyId) throws SQLException {
        List<Categories> categories = new ArrayList<>();
        String xSql = "select c.category_id, c.category_name\n"
                + "from Categories c \n"
                + "join story_categories sc on c.category_id = sc.category_id\n"
                + "join Stories s on s.story_id = sc.story_id\n"
                + "where s.story_id = ?";
        try {
            ps = con.prepareStatement(xSql);
            ps.setInt(1, storyId);
            rs = ps.executeQuery();
            categories = createCategories(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
        return categories;
    }

}
