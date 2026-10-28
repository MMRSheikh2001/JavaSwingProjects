/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystem.db;

import hotelreservationsystem.models.Reservation;
import hotelreservationsystem.models.ReservationStatus;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ReservationDao {

    DataBase db = new DataBase();

    PreparedStatement ps;
    ResultSet rs;
    String sql;

    private static final String CREATE_TABLE
            = "create table if not exists reservations ("
            + " id integer primary key autoincrement,"
            + " userId integer not null,"
            + " roomId integer not null,"
            + " checkIn text not null,"
            + " checkOut text not null,"
            + " totalPrice real not null,"
            + " status text not null,"
            + " createdAt text not null)";

    public void createTable() {

        try {
            ps = db.getCon().prepareStatement(CREATE_TABLE);
            ps.execute();

            ps.getConnection().close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int countAll() {

        sql = "select count(*) from reservations";
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
            Logger.getLogger(ReservationDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public int countByUserId(Long userId) {

        sql = "select count(*) from reservations where userId = ?";
        int count = 0;

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setLong(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(ReservationDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public void save(Reservation e) {

        sql = "insert into reservations (userId, roomId, checkIn, checkOut, totalPrice, status, createdAt) values (?, ?, ?, ?, ?, ?, ?)";

        try {
            ps = db.getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, e.getUserId());
            ps.setLong(2, e.getRoomId());
            ps.setString(3, e.getCheckIn().toString());
            ps.setString(4, e.getCheckOut().toString());
            ps.setDouble(5, e.getTotalPrice());
            ps.setString(6, e.getStatus() == null ? ReservationStatus.BOOKED.name() : e.getStatus().name());
            ps.setString(7, (e.getCreatedAt() == null ? LocalDateTime.now() : e.getCreatedAt()).toString());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                e.setId(rs.getLong(1));
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(ReservationDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Reservation findById(Long id) {

        sql = "select * from reservations where id = ?";
        Reservation reservation = null;

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                reservation = new Reservation();
                reservation.setId(rs.getLong("id"));
                reservation.setUserId(rs.getLong("userId"));
                reservation.setRoomId(rs.getLong("roomId"));
                reservation.setCheckIn(LocalDate.parse(rs.getString("checkIn")));
                reservation.setCheckOut(LocalDate.parse(rs.getString("checkOut")));
                reservation.setTotalPrice(rs.getDouble("totalPrice"));
                reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
                reservation.setCreatedAt(LocalDateTime.parse(rs.getString("createdAt")));
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(ReservationDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reservation;
    }

    public List<Reservation> findAll() {

        List<Reservation> reservations = new ArrayList<>();
        sql = "select * from reservations order by id";

        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getLong("id"));
                reservation.setUserId(rs.getLong("userId"));
                reservation.setRoomId(rs.getLong("roomId"));
                reservation.setCheckIn(LocalDate.parse(rs.getString("checkIn")));
                reservation.setCheckOut(LocalDate.parse(rs.getString("checkOut")));
                reservation.setTotalPrice(rs.getDouble("totalPrice"));
                reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
                reservation.setCreatedAt(LocalDateTime.parse(rs.getString("createdAt")));
                reservations.add(reservation);
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(ReservationDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reservations;
    }

    public List<Reservation> findByUserId(Long userId) {

        List<Reservation> reservations = new ArrayList<>();
        sql = "select * from reservations where userId = ? order by id desc";

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setLong(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getLong("id"));
                reservation.setUserId(rs.getLong("userId"));
                reservation.setRoomId(rs.getLong("roomId"));
                reservation.setCheckIn(LocalDate.parse(rs.getString("checkIn")));
                reservation.setCheckOut(LocalDate.parse(rs.getString("checkOut")));
                reservation.setTotalPrice(rs.getDouble("totalPrice"));
                reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
                reservation.setCreatedAt(LocalDateTime.parse(rs.getString("createdAt")));
                reservations.add(reservation);
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(ReservationDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reservations;
    }

    public void update(Reservation e) {

        sql = "update reservations set roomId = ?, checkIn = ?, checkOut = ?, totalPrice = ?, status = ? where id = ?";

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setLong(1, e.getRoomId());
            ps.setString(2, e.getCheckIn().toString());
            ps.setString(3, e.getCheckOut().toString());
            ps.setDouble(4, e.getTotalPrice());
            ps.setString(5, e.getStatus() == null ? ReservationStatus.BOOKED.name() : e.getStatus().name());
            ps.setLong(6, e.getId());
            ps.executeUpdate();

            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(ReservationDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
