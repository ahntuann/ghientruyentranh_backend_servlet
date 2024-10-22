package dao;

import dao.mydao.MyDAO;
import java.sql.SQLException;
import model.Users;

public class UsersDAO extends MyDAO {
    

    //Phương thức tìm kiếm user dựa vào email
    public Users getUserByEmail(String email) throws SQLException {
        Users user = null;
        String xSql = "select * from Users where email = ?";
        try {
            ps = con.prepareStatement(xSql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = new Users();
                user.setUser_id(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setAvatar(rs.getString("avatar"));
                user.setRole(rs.getString("role"));
                user.setCreated_at(rs.getDate("created_at"));
                user.setUpdated_at(rs.getDate("updated_at"));
                user.setVip(rs.getInt("vip"));
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return user;
    }
    
    //phương thức để thêm vào 1 user
    public boolean insertUser(Users user) {
        String xSql = "INSERT INTO Users (username, email, password, avatar, role,"
                + "created_at, updated_at, vip) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = con.prepareStatement(xSql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getAvatar());
            ps.setString(5, user.getRole());
            ps.setDate(6, user.getCreated_at());
            ps.setDate(7, user.getUpdated_at());
            ps.setInt(8, user.getVip());
            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0; 
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

}
