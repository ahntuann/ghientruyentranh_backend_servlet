package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Department;

public class DepartmentDAO {
    private Connection con;

    public DepartmentDAO(Connection con) {
        this.con = con;
    }

    public List<Department> getAllDepartments() throws SQLException {
        String sql = "SELECT * FROM department";
        List<Department> departments = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                departments.add(new Department(id, name));
            }
        }
        
        return departments;
    }
}
