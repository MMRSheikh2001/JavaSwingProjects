/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poshomepractice.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import poshomepractice.model.Sales;
import poshomepractice.service.DaoService;
import poshomepractice.util.DbUtil;

/**
 *
 * @author Admin
 */
public class SalesDao implements DaoService<Sales> {

    String sql;
    PreparedStatement ps;
    ResultSet rs;
    DbUtil db = new DbUtil();

    @Override
    public void save(Sales e) {
        sql = "insert into sales (productName,unitPrice,quantity,totalPrice,discount,actualPrice,date) values(?,?,?,?,?,?,?)";
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, e.getProductName());
            ps.setDouble(2, e.getUnitPrice());
            ps.setDouble(3, e.getQuantity());
            ps.setDouble(4, e.getTotalPrice());
            ps.setDouble(5, e.getDiscount());
            ps.setDouble(6, e.getActualPrice());
            ps.setDate(7, e.getSqlDate());
            ps.executeUpdate();
            ps.close();
            db.getCon().close();
            JOptionPane.showMessageDialog(null, "Sales Saved");
        } catch (SQLException ex) {
            Logger.getLogger(SalesDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Sales Not Saved");
        }

    }

    @Override
    public List<Sales> findAll() {
        List<Sales> sList = new ArrayList<>();
        sql = "select * from sales";
        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                java.sql.Date sqlDate = rs.getDate("date");
                LocalDate localDate = sqlDate.toLocalDate();
                Sales s = new Sales(rs.getInt("id"), rs.getString("productName"), rs.getDouble("unitPrice"),
                        rs.getDouble("quantity"), rs.getDouble("totalPrice"),
                        rs.getDouble("discount"), rs.getDouble("actualPrice"), sqlDate);
                sList.add(s);

            }
            rs.close();
            ps.close();
            db.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(SalesDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sList;
    }

    @Override
    public void update(Sales e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Sales findById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
