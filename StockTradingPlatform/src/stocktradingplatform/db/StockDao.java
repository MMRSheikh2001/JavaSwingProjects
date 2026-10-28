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
import stocktradingplatform.models.Stock;

/**
 *
 * @author Administrator
 */
public class StockDao implements DaoService<Stock> {

    DataBase db = new DataBase();

    PreparedStatement ps;
    ResultSet rs;
    String sql;

    private static final String CREATE_TABLE
            = "create table if not exists stocks ("
            + " id integer primary key autoincrement,"
            + " companyName text not null unique,"
            + " quantity integer not null default 0,"
            + " currentPrice real not null default 0)";

    public void createTable() {

        try {
            ps = db.getCon().prepareStatement(CREATE_TABLE);

            ps.execute();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(StockDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Stock findByCompanyName(String companyName) {

        sql = "select * from stocks where companyName = ?";
        Stock stock = null;

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setString(1, companyName);
            rs = ps.executeQuery();

            while (rs.next()) {
                stock = new Stock();
                stock.setId(rs.getLong("id"));
                stock.setCompanyName(rs.getString("companyName"));
                stock.setQuantity(rs.getLong("quantity"));
                stock.setCurrentPrice(rs.getDouble("currentPrice"));
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(StockDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return stock;
    }

    public int countStocks() {

        sql = "select count(*) from stocks";
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
            Logger.getLogger(StockDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count;
    }

    public void updateStockPrice(Long id, Double price) {

        sql = "update stocks set  currentPrice = ? where id = ?";

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setDouble(1, price);
            ps.setLong(2, id);
            ps.executeUpdate();

            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(StockDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deleteStock(Long id) {
        sql = "update stocks set  quantity = 0 where id = ?";

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();

            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(StockDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void save(Stock e) {

        sql = "insert into stocks (companyName, quantity, currentPrice) values (?, ?, ?)";

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setString(1, e.getCompanyName());
            ps.setLong(2, e.getQuantity() == null ? 0 : e.getQuantity());
            ps.setDouble(3, e.getCurrentPrice() == null ? 0.0 : e.getCurrentPrice());
            ps.executeUpdate();

            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(StockDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Stock findById(Long id) {

        sql = "select * from stocks where id = ?";
        Stock stock = null;

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setLong(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                stock = new Stock();
                stock.setId(rs.getLong("id"));
                stock.setCompanyName(rs.getString("companyName"));
                stock.setQuantity(rs.getLong("quantity"));
                stock.setCurrentPrice(rs.getDouble("currentPrice"));
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(StockDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return stock;

    }

    @Override
    public List<Stock> findAll() {

        List<Stock> stocks = new ArrayList<>();
        sql = "select * from stocks order by id";

        try {
            ps = db.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                Stock stock = new Stock();
                stock.setId(rs.getLong("id"));
                stock.setCompanyName(rs.getString("companyName"));
                stock.setQuantity(rs.getLong("quantity"));
                stock.setCurrentPrice(rs.getDouble("currentPrice"));
                stocks.add(stock);
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(StockDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return stocks;
    }

    @Override
    public void update(Stock e) {

        sql = "update stocks set companyName = ?, quantity = ?, currentPrice = ? where id = ?";

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setString(1, e.getCompanyName());
            ps.setLong(2, e.getQuantity() == null ? 0 : e.getQuantity());
            ps.setDouble(3, e.getCurrentPrice() == null ? 0.0 : e.getCurrentPrice());
            ps.setLong(4, e.getId());
            ps.executeUpdate();

            ps.close();
            db.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(StockDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
