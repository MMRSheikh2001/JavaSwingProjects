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
import poshomepractice.model.Supplier;
import poshomepractice.service.DaoService;
import poshomepractice.util.DbUtil;

/**
 *
 * @author Administrator
 */
public class SupplierDao implements DaoService<Supplier> {

    String sql;
    PreparedStatement ps;
    ResultSet rs;
    DbUtil db = new DbUtil();

    @Override
    public void save(Supplier e) {

        sql = "insert into supplier (name,cell,contactPersonName,contactPersonCell,address) values(?,?,?,?,?)";

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, e.getName());
            ps.setString(2, e.getCell());
            ps.setString(3, e.getContactPersonName());
            ps.setString(4, e.getContactPersonCell());
            ps.setString(5, e.getAddress());
            ps.executeUpdate();
            ps.close();
            db.getCon().close();
            JOptionPane.showMessageDialog(null, "Supplier Saved");

        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Supplier Not Saved");
        }

    }

    @Override
    public List<Supplier> findAll() {
        List<Supplier> sList = new ArrayList<>();

        sql = "select * from supplier order by id";
        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Supplier s = new Supplier(rs.getInt("id"), rs.getString("name"),
                        rs.getString("cell"), rs.getString("contactPersonName"),
                        rs.getString("contactPersonCell"), rs.getString("address"));
                sList.add(s);

            }
            ps.close();
            rs.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sList;
    }

    @Override
    public void update(Supplier e) {
        sql = "update supplier set name=?,cell=?,contactPersonName=?,contactPersonCell=?,address=? where id=?";
        try {

            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, e.getName());
            ps.setString(2, e.getCell());
            ps.setString(3, e.getContactPersonName());
            ps.setString(4, e.getContactPersonCell());
            ps.setString(5, e.getAddress());
            ps.setInt(6, e.getId());
            ps.executeUpdate();
            ps.close();
            db.getCon().close();
            JOptionPane.showMessageDialog(null, "Supplier Updated");
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Supplier Not Updated");
        }

    }

    @Override
    public Supplier findById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(int id) {
        sql = "delete from supplier where id=?";
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            db.getCon().close();
            JOptionPane.showMessageDialog(null, "Supplier Deleted");

        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Supplier Not Deleted");
        }

    }

    public int findIdByName(String name) {
        int id = 0;
        sql = "select id from supplier where name =?";
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

    public List<String> getAllSupplierName() {
        List<String> sList = new ArrayList<>();
        sql = "select name from supplier";
        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String s = rs.getString("name");
                sList.add(s);

            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sList;
    }

}
