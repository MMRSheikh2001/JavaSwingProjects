/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystem.db;

import hotelreservationsystem.models.Payment;
import hotelreservationsystem.models.PaymentMethod;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class PaymentDao {

    DataBase db = new DataBase();

    PreparedStatement ps;
    ResultSet rs;
    String sql;

    private static final String CREATE_TABLE
            = "create table if not exists payments ("
            + " id integer primary key autoincrement,"
            + " reservationId integer not null,"
            + " amount real not null,"
            + " method text not null,"
            + " reference text not null,"
            + " paidAt text not null)";

    public void createTable() {

        try {
            ps = db.getCon().prepareStatement(CREATE_TABLE);
            ps.execute();

            ps.getConnection().close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(PaymentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Double getTotalIncome() {
        sql = "select coalesce(sum(amount), 0) from payments";
        Double total = 0.0;

        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble(1);
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(PaymentDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Math.round(total * 100) / 100.0;

    }
    
   

    public int countAll() {

        sql = "select count(*) from payments";
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
            Logger.getLogger(PaymentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public void save(Payment e) {

        sql = "insert into payments (reservationId, amount, method, reference, paidAt) values (?, ?, ?, ?, ?)";

        try {
            ps = db.getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, e.getReservationId());
            ps.setDouble(2, e.getAmount());
            ps.setString(3, e.getMethod().name());
            ps.setString(4, e.getReference());
            ps.setString(5, (e.getPaidAt() == null ? LocalDateTime.now() : e.getPaidAt()).toString());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                e.setId(rs.getLong(1));
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(PaymentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Payment findById(Long id) {

        sql = "select * from payments where id = ?";
        Payment payment = null;

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                payment = new Payment();
                payment.setId(rs.getLong("id"));
                payment.setReservationId(rs.getLong("reservationId"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setMethod(PaymentMethod.valueOf(rs.getString("method")));
                payment.setReference(rs.getString("reference"));
                payment.setPaidAt(LocalDateTime.parse(rs.getString("paidAt")));
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(PaymentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return payment;
    }

    public List<Payment> findAll() {

        List<Payment> payments = new ArrayList<>();
        sql = "select * from payments order by id";

        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setId(rs.getLong("id"));
                payment.setReservationId(rs.getLong("reservationId"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setMethod(PaymentMethod.valueOf(rs.getString("method")));
                payment.setReference(rs.getString("reference"));
                payment.setPaidAt(LocalDateTime.parse(rs.getString("paidAt")));
                payments.add(payment);
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(PaymentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return payments;
    }

    public Payment findByReservationId(Long reservationId) {

        sql = "select * from payments where reservationId = ?";
        Payment payment = null;

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setLong(1, reservationId);
            rs = ps.executeQuery();

            while (rs.next()) {
                payment = new Payment();
                payment.setId(rs.getLong("id"));
                payment.setReservationId(rs.getLong("reservationId"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setMethod(PaymentMethod.valueOf(rs.getString("method")));
                payment.setReference(rs.getString("reference"));
                payment.setPaidAt(LocalDateTime.parse(rs.getString("paidAt")));
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(PaymentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return payment;
    }

}
