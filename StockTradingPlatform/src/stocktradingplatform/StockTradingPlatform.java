/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package stocktradingplatform;

import stocktradingplatform.db.DataBase;
import stocktradingplatform.db.HoldingDao;
import stocktradingplatform.db.StockDao;
import stocktradingplatform.db.TransactionDao;
import stocktradingplatform.db.UserDao;
import stocktradingplatform.models.Stock;
import stocktradingplatform.models.User;
import stocktradingplatform.services.MarketSimulator;
import stocktradingplatform.views.LoginView;

/**
 *
 * @author Administrator
 */
public class StockTradingPlatform {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        UserDao userDao = new UserDao();

//        User user = new User();
//        user.setUserName("admin");
//        user.setPassword("1234");
//
//        user.setIsAdmin(true);
//
//        userDao.createTable();
//        userDao.save(user);
        StockDao stockDao = new StockDao();
//        stockDao.createTable();
//
//        System.out.println("Total stocks : " + stockDao.countStocks());
//        User admin = userDao.findByUserName("admin");
//
//        System.out.println("Name : " + admin.getUserName() + " Balance : " + admin.getCashBalance()
//                + "\n password : " + admin.getPassword());

//        Stock s = new Stock();
//        s.setCompanyName("Pran");
//        s.setCurrentPrice(5000.0);
//        s.setQuantity(50L);
//        stockDao.save(s);
        new MarketSimulator().start();

        LoginView lv = new LoginView();
        lv.setVisible(true);

//        System.out.println("Company : "+ stockDao.findById(1L).getCompanyName());
//        System.out.println("Current Price : "+stockDao.findById(1L).getCurrentPrice());
//        TransactionDao transactionDao = new TransactionDao();
//        transactionDao.createTable();
//        System.out.println("Total Transactions : " + transactionDao.countTransactions());
//        HoldingDao holdingDao = new HoldingDao();
//
//        holdingDao.createTable();
//        System.out.println("Total Holdings : " + holdingDao.countHoldings());

    }

}
