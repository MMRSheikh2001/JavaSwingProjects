/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystem.db;

import hotelreservationsystem.models.Room;
import hotelreservationsystem.models.RoomCategory;
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
public class RoomDao {

    DataBase db = new DataBase();

    PreparedStatement ps;
    ResultSet rs;
    String sql;

    private static final String CREATE_TABLE
            = "create table if not exists rooms ("
            + " id integer primary key autoincrement,"
            + " roomNumber text not null unique,"
            + " category text not null,"
            + " pricePerNight real not null,"
            + " active integer not null default 1)";

    public void createTable() {

        try {
            ps = db.getCon().prepareStatement(CREATE_TABLE);
            ps.execute();

            ps.getConnection().close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(RoomDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // how many rows are in the table
    public int countAll() {

        sql = "select count(*) from rooms";
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
            Logger.getLogger(RoomDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public void save(Room e) {

        sql = "insert into rooms (roomNumber, category, pricePerNight, active) values (?, ?, ?, ?)";

        try {
            ps = db.getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, e.getRoomNumber());
            ps.setString(2, e.getCategory().name());
            ps.setDouble(3, e.getPricePerNight());
            ps.setInt(4, e.getActive() == null || e.getActive() ? 1 : 0);
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
            Logger.getLogger(RoomDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Room findById(Long id) {

        sql = "select * from rooms where id = ?";
        Room room = null;

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                room = new Room();
                room.setId(rs.getLong("id"));
                room.setRoomNumber(rs.getString("roomNumber"));
                room.setCategory(RoomCategory.valueOf(rs.getString("category")));
                room.setPricePerNight(rs.getDouble("pricePerNight"));
                room.setActive(rs.getInt("active") == 1);
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(RoomDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return room;
    }

    public List<Room> findAll() {

        List<Room> rooms = new ArrayList<>();
        sql = "select * from rooms order by id";

        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getLong("id"));
                room.setRoomNumber(rs.getString("roomNumber"));
                room.setCategory(RoomCategory.valueOf(rs.getString("category")));
                room.setPricePerNight(rs.getDouble("pricePerNight"));
                room.setActive(rs.getInt("active") == 1);
                rooms.add(room);
            }

            rs.close();
            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(RoomDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rooms;
    }

    public void update(Room e) {

        sql = "update rooms set roomNumber = ?, category = ?, pricePerNight = ?, active = ? where id = ?";

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, e.getRoomNumber());
            ps.setString(2, e.getCategory().name());
            ps.setDouble(3, e.getPricePerNight());
            ps.setInt(4, e.getActive() == null || e.getActive() ? 1 : 0);
            ps.setLong(5, e.getId());
            ps.executeUpdate();

            ps.getConnection().close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(RoomDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
