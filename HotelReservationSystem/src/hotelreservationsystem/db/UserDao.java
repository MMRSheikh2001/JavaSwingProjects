/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystem.db;

import hotelreservationsystem.models.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class UserDao {

    DataBase db = new DataBase();

    PreparedStatement ps;
    ResultSet rs;
    String sql;

    private static final String CREATE_TABLE
            = "create table if not exists users ("
            + " id integer primary key autoincrement,"
            + " userName text not null unique,"
            + " password text not null,"
            + " fullName text not null default '',"
            + " isAdmin integer not null default 0)";

    public void createTable() {

        try {
            ps = db.getCon().prepareStatement(CREATE_TABLE);
            ps.execute();

            ps.getConnection().close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int countAll() {

        sql = "select count(*) from users";
        int count = 0;

        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public User findByUserName(String userName) {

        sql = "select * from users where userName = ?";
        User user = null;

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, userName);
            rs = ps.executeQuery();

            while (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("fullName"));
                user.setIsAdmin(rs.getInt("isAdmin") == 1);
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public void save(User e) {

        sql = "insert into users (userName, password, fullName, isAdmin) values (?, ?, ?, ?)";

        try {
            ps = db.getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, e.getUserName());
            ps.setString(2, e.getPassword());
            ps.setString(3, e.getFullName() == null ? "" : e.getFullName());
            ps.setInt(4, Boolean.TRUE.equals(e.getIsAdmin()) ? 1 : 0);
            ps.executeUpdate();

            // put the new id on the object, it is needed for the next step (for example the payment)
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                e.setId(rs.getLong(1));
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User findById(Long id) {

        sql = "select * from users where id = ?";
        User user = null;

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("fullName"));
                user.setIsAdmin(rs.getInt("isAdmin") == 1);
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public List<User> findAll() {

        List<User> users = new ArrayList<>();
        sql = "select * from users order by id";

        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("fullName"));
                user.setIsAdmin(rs.getInt("isAdmin") == 1);
                users.add(user);
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

    public void update(User e) {

        sql = "update users set userName = ?, password = ?, fullName = ?, isAdmin = ? where id = ?";

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, e.getUserName());
            ps.setString(2, e.getPassword());
            ps.setString(3, e.getFullName() == null ? "" : e.getFullName());
            ps.setInt(4, Boolean.TRUE.equals(e.getIsAdmin()) ? 1 : 0);
            ps.setLong(5, e.getId());
            ps.executeUpdate();

            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
