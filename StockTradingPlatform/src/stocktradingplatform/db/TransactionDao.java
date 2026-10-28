/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package stocktradingplatform.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import stocktradingplatform.models.Transaction;
import stocktradingplatform.models.TransactionType;

/**
 *
 * @author Administrator
 */
public class TransactionDao {

    DataBase db = new DataBase();

    PreparedStatement ps;
    ResultSet rs;
    String sql;

    private static final String CREATE_TABLE
            = "create table if not exists transactions ("
            + " id integer primary key autoincrement,"
            + " userId integer not null,"
            + " stockId integer not null,"
            + " type text not null,"
            + " quantity integer not null,"
            + " unitPrice real not null,"
            + " totalPrice real not null,"
            + " createdAt text not null)";

    public void createTable() {

        try {
            ps = db.getCon().prepareStatement(CREATE_TABLE);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int countUserTransactions(Long id) {
        sql = "select count(*) from transactions where userId=?";

        int count = 0;

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setLong(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count;
    }

    public double getTotalAmount(Long userId, TransactionType type) {

        sql = "select coalesce(sum(totalPrice), 0) from transactions where userId = ? and type = ?";
        double total = 0;

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setLong(1, userId);
            ps.setString(2, type.name());
            rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble(1);
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public int countTransactions() {
        sql = "select count(*) from transactions";

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
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count;
    }

    public void save(Transaction e) {

        sql = "insert into transactions (userId, stockId, type, quantity, unitPrice, totalPrice, createdAt) values (?, ?, ?, ?, ?, ?, ?)";

        // total = quantity x unit price, unless the caller already set it
        Double total = e.getTotalPrice() == null ? e.getQuantity() * e.getUnitPrice() : e.getTotalPrice();

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setLong(1, e.getUserId());
            ps.setLong(2, e.getStockId());
            ps.setString(3, e.getType().name());
            ps.setLong(4, e.getQuantity());
            ps.setDouble(5, e.getUnitPrice());
            ps.setDouble(6, total);
            ps.setString(7, e.getCreatedAt().toString());
            ps.executeUpdate();

            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Transaction findById(Long id) {

        sql = "select * from transactions where id = ?";
        Transaction transaction = null;

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setLong(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                transaction = new Transaction();
                transaction.setId(rs.getLong("id"));
                transaction.setUserId(rs.getLong("userId"));
                transaction.setStockId(rs.getLong("stockId"));
                transaction.setType(TransactionType.valueOf(rs.getString("type")));
                transaction.setQuantity(rs.getLong("quantity"));
                transaction.setUnitPrice(rs.getDouble("unitPrice"));
                transaction.setTotalPrice(rs.getDouble("totalPrice"));
                transaction.setCreatedAt(LocalDateTime.parse(rs.getString("createdAt")));
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return transaction;
    }

    public List<Transaction> findAll() {

        List<Transaction> transactions = new ArrayList<>();
        sql = "select * from transactions order by id desc";

        try {
            ps = db.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getLong("id"));
                transaction.setUserId(rs.getLong("userId"));
                transaction.setStockId(rs.getLong("stockId"));
                transaction.setType(TransactionType.valueOf(rs.getString("type")));
                transaction.setQuantity(rs.getLong("quantity"));
                transaction.setUnitPrice(rs.getDouble("unitPrice"));
                transaction.setTotalPrice(rs.getDouble("totalPrice"));
                transaction.setCreatedAt(LocalDateTime.parse(rs.getString("createdAt")));
                transactions.add(transaction);
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return transactions;
    }

    public List<Transaction> findByUserId(Long userId) {

        List<Transaction> transactions = new ArrayList<>();
        sql = "select * from transactions where userId = ? order by id desc";

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setLong(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getLong("id"));
                transaction.setUserId(rs.getLong("userId"));
                transaction.setStockId(rs.getLong("stockId"));
                transaction.setType(TransactionType.valueOf(rs.getString("type")));
                transaction.setQuantity(rs.getLong("quantity"));
                transaction.setUnitPrice(rs.getDouble("unitPrice"));
                transaction.setTotalPrice(rs.getDouble("totalPrice"));
                transaction.setCreatedAt(LocalDateTime.parse(rs.getString("createdAt")));
                transactions.add(transaction);
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return transactions;
    }

}
