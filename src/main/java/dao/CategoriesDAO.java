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

}
