/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poshomepractice.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import poshomepractice.model.Category;
import poshomepractice.service.DaoService;
import poshomepractice.util.DbUtil;

/**
 *
 * @author Administrator
 */
public class CategoryDao implements DaoService<Category> {

    DbUtil db = new DbUtil();
    PreparedStatement ps;
    ResultSet rs;
    String sql;

    @Override
    public void save(Category e) {
        sql = "insert into category (name) values(?)";
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, e.getName());
            ps.executeUpdate();
            ps.close();
            db.getCon().close();
            JOptionPane.showMessageDialog(null, "Category Saved");
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Category Not Saved");
        }

    }

    @Override
    public List<Category> findAll() {
        List<Category> cList = new ArrayList<>();
        sql = "select * from category order by id";
        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Category c = new Category(rs.getInt("id"), rs.getString("name"));
                cList.add(c);

            }
            ps.close();
            db.getCon().close();
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cList;

    }

    @Override
    public void update(Category e) {
        sql = "update category set name=? where id= ?";
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, e.getName());
            ps.setInt(2, e.getId());
            ps.executeUpdate();
            ps.close();
            db.getCon().close();
            JOptionPane.showMessageDialog(null, "Category Updated");

        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Category Not Updated");
        }

    }

    @Override
    public Category findById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(int id) {
        sql = "delete from category where id=?";
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            db.getCon().close();
            JOptionPane.showMessageDialog(null, "Category Deleted");
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Category Not Deleted");
        }

    }

    public int findIdByName(String name) {
        int id = 0;
        sql = "select id from category where name =?";
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");

            }
            rs.close();
            ps.close();
            db.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return id;
    }

    public List<String> getAllCategoryName() {
        List<String> cList = new ArrayList<>();
        sql = "select name from category";
        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String s = rs.getString("name");
                cList.add(s);

            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cList;
    }

}
