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
import poshomepractice.model.Product;
import poshomepractice.service.DaoService;
import poshomepractice.util.DbUtil;

/**
 *
 * @author Administrator
 */
public class ProductDao implements DaoService<Product> {

    String sql;
    PreparedStatement ps;
    ResultSet rs;
    DbUtil db = new DbUtil();

    @Override
    public void save(Product e) {
        sql = "insert into product (name,price,quantity,categoryId,supplierId) values(?,?,?,?,?)";
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, e.getName());
            ps.setDouble(2, e.getPrice());
            ps.setDouble(3, e.getQuantity());
            ps.setInt(4, e.getCategoryId());
            ps.setInt(5, e.getSupplierId());
            ps.executeUpdate();
            ps.close();
            db.getCon().close();
            JOptionPane.showMessageDialog(null, "Product Saved");
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Product Not Saved");
        }

    }

    @Override
    public List<Product> findAll() {
        List<Product> pList = new ArrayList<>();
        sql = "select p.id,p.name,p.price,p.quantity,c.name,s.name from product p "
                + "join category c on p.categoryId=c.id "
                + "join supplier s on p.supplierId = s.id";
        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(rs.getInt("p.id"), rs.getString("p.name"),
                        rs.getDouble("p.price"), rs.getDouble("p.quantity"),
                        rs.getString("c.name"), rs.getString("s.name"));
                pList.add(p);

            }
            rs.close();
            ps.close();
            db.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pList;
    }

    @Override
    public void update(Product e) {

        sql = "update product set name=?,price=?,quantity=?,categoryId=?,supplierId=? where id=?";
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, e.getName());
            ps.setDouble(2, e.getPrice());
            ps.setDouble(3, e.getQuantity());
            ps.setInt(4, e.getCategoryId());
            ps.setInt(5, e.getSupplierId());
            ps.setInt(6, e.getId());
            ps.executeUpdate();
            ps.close();
            db.getCon().close();
            JOptionPane.showMessageDialog(null, "Product Updated");
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Product Not Updated");
        }

    }

    @Override
    public Product findById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(int id) {

        sql = "delete from product where id=?";
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            db.getCon().close();
            JOptionPane.showMessageDialog(null, "Product Deleted");

        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Product Not Deleted");
        }

    }

    public List<String> getProductNames() {
        List<String> pList = new ArrayList<>();
        sql = "select name from product";
        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String p = rs.getString("name");
                pList.add(p);

            }
            rs.close();
            ps.close();
            db.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pList;
    }

}
