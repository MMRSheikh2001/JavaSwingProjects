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
import stocktradingplatform.models.Holding;

/**
 *
 * @author Administrator
 */
public class HoldingDao implements DaoService<Holding> {

    DataBase db = new DataBase();

    PreparedStatement ps;
    ResultSet rs;
    String sql;

    // one row per user per stock
    private static final String CREATE_TABLE
            = "create table if not exists holdings ("
            + " id integer primary key autoincrement,"
            + " userId integer not null,"
            + " stockId integer not null,"
            + " quantity integer not null default 0,"
            + " averageBuyPrice real not null default 0,"
            + " unique (userId, stockId))";

    public void createTable() {

        try {
            ps = db.getCon().prepareStatement(CREATE_TABLE);

            ps.execute();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(HoldingDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int countUserHoldings(Long id) {
      sql = "select count(*) from holdings where userId=? and quantity > 0";

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

    public double getHoldingsValue(Long userId) {

        sql = "select coalesce(sum(h.quantity * s.currentPrice), 0) from holdings h join stocks s on s.id = h.stockId where h.userId = ?";
        double value = 0;

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setLong(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                value = rs.getDouble(1);
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(HoldingDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public int countHoldings() {

        sql = "select count(*) from holdings";
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
            Logger.getLogger(HoldingDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public Holding findByUserIdAndStockId(Long userId, Long stockId) {

        sql = "select * from holdings where userId = ? and stockId = ?";
        Holding holding = null;

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setLong(1, userId);
            ps.setLong(2, stockId);
            rs = ps.executeQuery();

            while (rs.next()) {
                holding = new Holding();
                holding.setId(rs.getLong("id"));
                holding.setUserId(rs.getLong("userId"));
                holding.setStockId(rs.getLong("stockId"));
                holding.setQuantity(rs.getLong("quantity"));
                holding.setAverageBuyPrice(rs.getDouble("averageBuyPrice"));
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(HoldingDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return holding;
    }

    public List<Holding> findByUserId(Long userId) {

        List<Holding> holdings = new ArrayList<>();
        sql = "select * from holdings where userId = ? and quantity > 0 order by id";

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setLong(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Holding holding = new Holding();
                holding.setId(rs.getLong("id"));
                holding.setUserId(rs.getLong("userId"));
                holding.setStockId(rs.getLong("stockId"));
                holding.setQuantity(rs.getLong("quantity"));
                holding.setAverageBuyPrice(rs.getDouble("averageBuyPrice"));
                holdings.add(holding);
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(HoldingDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return holdings;
    }

    @Override
    public void save(Holding e) {

        sql = "insert into holdings (userId, stockId, quantity, averageBuyPrice) values (?, ?, ?, ?)";

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setLong(1, e.getUserId());
            ps.setLong(2, e.getStockId());
            ps.setLong(3, e.getQuantity() == null ? 0 : e.getQuantity());
            ps.setDouble(4, e.getAverageBuyPrice() == null ? 0.0 : e.getAverageBuyPrice());
            ps.executeUpdate();

            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(HoldingDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Holding findById(Long id) {

        sql = "select * from holdings where id = ?";
        Holding holding = null;

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setLong(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                holding = new Holding();
                holding.setId(rs.getLong("id"));
                holding.setUserId(rs.getLong("userId"));
                holding.setStockId(rs.getLong("stockId"));
                holding.setQuantity(rs.getLong("quantity"));
                holding.setAverageBuyPrice(rs.getDouble("averageBuyPrice"));
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(HoldingDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return holding;
    }

    @Override
    public List<Holding> findAll() {

        List<Holding> holdings = new ArrayList<>();
        sql = "select * from holdings order by id";

        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Holding holding = new Holding();
                holding.setId(rs.getLong("id"));
                holding.setUserId(rs.getLong("userId"));
                holding.setStockId(rs.getLong("stockId"));
                holding.setQuantity(rs.getLong("quantity"));
                holding.setAverageBuyPrice(rs.getDouble("averageBuyPrice"));
                holdings.add(holding);
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(HoldingDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return holdings;
    }

    @Override
    public void update(Holding e) {

        sql = "update holdings set quantity = ?, averageBuyPrice = ? where id = ?";

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setLong(1, e.getQuantity() == null ? 0 : e.getQuantity());
            ps.setDouble(2, e.getAverageBuyPrice() == null ? 0.0 : e.getAverageBuyPrice());
            ps.setLong(3, e.getId());
            ps.executeUpdate();

            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(HoldingDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
