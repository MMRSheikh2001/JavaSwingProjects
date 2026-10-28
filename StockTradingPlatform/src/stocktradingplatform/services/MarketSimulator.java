/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package stocktradingplatform.services;

import java.util.List;
import java.util.Random;
import javax.swing.Timer;
import stocktradingplatform.db.StockDao;
import stocktradingplatform.models.Stock;

/**
 *
 * @author Administrator
 */
public class MarketSimulator {

    StockDao stockDao = new StockDao();
    Random random = new Random();
    Timer timer;

    private static final int INTERVAL = 3000;       // milliseconds between price changes
    private static final double MAX_CHANGE = 0.02;  // biggest change per tick (2%)
    private static final double MIN_PRICE = 1.00;   // price never goes below this

    public void start() {

        // already running, so do not start a second timer
        if (timer != null && timer.isRunning()) {
            return;
        }

        timer = new Timer(INTERVAL, e -> tick());
        timer.start();
    }

    public void stop() {

        if (timer != null) {
            timer.stop();
        }
    }

    private void tick() {

        List<Stock> stocks = stockDao.findAll();

        for (Stock stock : stocks) {

            // random number between -MAX_CHANGE and +MAX_CHANGE
            double change = (random.nextDouble() * 2 - 1) * MAX_CHANGE;

            double newPrice = stock.getCurrentPrice() * (1 + change);

            if (newPrice < MIN_PRICE) {
                newPrice = MIN_PRICE;
            }

            // keep 2 decimal places
            newPrice = Math.round(newPrice * 100) / 100.0;

            stockDao.updateStockPrice(stock.getId(), newPrice);
        }
    }

}
