/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package stocktradingplatform.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import stocktradingplatform.models.User;

/**
 *
 * @author Administrator
 */
public class UserDao implements DaoService<User> {

    DataBase db = new DataBase();

    PreparedStatement ps;
    ResultSet rs;
    String sql;

    private static final String CREATE_TABLE
            = "create table if not exists users ("
            + " id integer primary key autoincrement,"
            + " userName text not null unique,"
            + " password text not null,"
            + " cashBalance real not null default 0,"
            + " isAdmin integer not null default 0)";

    public void createTable() {

        try {
            ps = db.getCon().prepareStatement(CREATE_TABLE);
            ps.execute();
            ps.close();
            db.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }

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
                user.setCashBalance(rs.getDouble("cashBalance"));
                user.setIsAdmin(rs.getInt("isAdmin") == 1);
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return user;

    }

    public int countUsers() {

        sql = "select count(*) from users";

        int count = 0;

        try {
            ps = db.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            ps.close();
            db.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count;

    }

    @Override
    public void save(User e) {

        sql = "insert into users (userName, password, cashBalance, isAdmin) values (?, ?, ?, ?)";

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, e.getUserName());
            ps.setString(2, e.getPassword());
            ps.setDouble(3, e.getCashBalance() == null ? 0.0 : e.getCashBalance());
            ps.setInt(4, Boolean.TRUE.equals(e.getIsAdmin()) ? 1 : 0);
            ps.executeUpdate();

            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
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
                user.setCashBalance(rs.getDouble("cashBalance"));
                user.setIsAdmin(rs.getInt("isAdmin") == 1);
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return user;

    }

    @Override
    public List<User> findAll() {

        List<User> users = new ArrayList<>();
        sql = "select * from users where isAdmin==0 order by id";

        try {
            ps = db.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setCashBalance(rs.getDouble("cashBalance"));
                user.setIsAdmin(rs.getInt("isAdmin") == 1);
                users.add(user);
            }

            rs.close();
            ps.close();
            db.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return users;

    }

    @Override
    public void update(User e) {

        sql = "update users set userName = ?, password = ?, cashBalance = ?, isAdmin = ? where id = ?";

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setString(1, e.getUserName());
            ps.setString(2, e.getPassword());
            ps.setDouble(3, e.getCashBalance() == null ? 0.0 : e.getCashBalance());
            ps.setInt(4, Boolean.TRUE.equals(e.getIsAdmin()) ? 1 : 0);
            ps.setLong(5, e.getId());
            ps.executeUpdate();

            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
