/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poshomepractice.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import poshomepractice.model.User;
import poshomepractice.util.DbUtil;

/**
 *
 * @author Administrator
 */
public class UserDao {

    DbUtil util = new DbUtil();
    PreparedStatement ps;
    ResultSet rs;
    String sql = null;

    public void saveUser(User user) {
        sql = "insert into user(userName,password,role) values(?,?,?)";
        try {
            ps = util.getCon().prepareStatement(sql);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());

            ps.executeUpdate();
            ps.close();
            util.getCon().close();
            System.out.println("Data Save (Home)");

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
