package dao;

import dao.mydao.MyDAO;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Users;
import util.StringUtil;

public class UsersDAO extends MyDAO {

    private List<Users> createUsers(ResultSet rs) {
        List<Users> users = new ArrayList<>();
        try {
            while (rs.next()) {
                int user_id = rs.getInt("user_id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String avatar = rs.getString("avatar");
                String role = rs.getString("role");
                Date createdAt = rs.getDate("created_at");
                Date updatedAt = rs.getDate("updated_at");
                int vip = rs.getInt("vip");
                Users user = new Users(user_id, username, email, password, avatar, role, createdAt, updatedAt, vip);
                users.add(user);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return users;
    }
    
    private Users createUser(ResultSet rs) {
        Users returnedUser = null;
        try {
            while (rs.next()) {
                int user_id = rs.getInt("user_id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String avatar = rs.getString("avatar");
                String role = rs.getString("role");
                Date createdAt = rs.getDate("created_at");
                Date updatedAt = rs.getDate("updated_at");
                int vip = rs.getInt("vip");
                returnedUser = new Users(user_id, username, email, password, avatar, role, createdAt, updatedAt, vip);
                
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return returnedUser;
    }

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

    public List<Users> getUserByListID(List<Integer> userIDs) {
        List<Users> users = new ArrayList<>();
        xSql = "select * from users where user_id in (";
        StringBuilder sqlBuilder = new StringBuilder(xSql);

        int n = userIDs.size();
        for (int i = 0; i < n - 1; i++) {
            sqlBuilder.append("?, ");
        }
        sqlBuilder.append("?)");

        try {
            ps = con.prepareStatement(sqlBuilder.toString());

            for (int i = 0; i < n; i++) {
                ps.setInt(i + 1, userIDs.get(i));
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                Users user = new Users();
                user.setUser_id(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setAvatar(rs.getString("avatar"));
                user.setRole(rs.getString("role"));
                user.setCreated_at(rs.getDate("created_at"));
                user.setUpdated_at(rs.getDate("updated_at"));
                user.setVip(rs.getInt("vip"));

                users.add(user);
            }
        } catch (Exception e) {
        }

        return users;
    }

    public Users getUsersByName(String xName) {
        Users user = null;
        boolean isNonAccent = StringUtil.isNonAccent(xName);
        if (isNonAccent) {
            // Nếu chuỗi không dấu, tìm kiếm không dấu
            xSql = "SELECT * FROM Users WHERE dbo.removeAccent(username) = ?";
        } else {
            // Nếu chuỗi có dấu, tìm kiếm theo có dấu
            xSql = "SELECT * FROM Users WHERE username = ? COLLATE Vietnamese_CI_AI";
        }
        try {
            ps = con.prepareStatement(xSql);
            ps.setString(1, "%" + xName + "%");
            rs = ps.executeQuery();
            user = createUser(rs);
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

    public boolean insertUser(String name, String email, String pass) throws SQLException {
        Users userMail = getUserByEmail(email);
        Users userName = getUsersByName(name);
        
        xSql = "INSERT INTO Users(username, email, password, avatar, role, created_at, updated_at, vip) VALUES (?,?,?,NULL,'user',GETDATE(),GETDATE(),0)";
        if (userMail == null && userName == null) {
            try {
                ps = con.prepareStatement(xSql);
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, pass);
                rs = ps.executeQuery();
            } catch (SQLException e) {
                System.out.println(e);
            }
            
            return true;
        } else {
            return false;
        }
    }

}
