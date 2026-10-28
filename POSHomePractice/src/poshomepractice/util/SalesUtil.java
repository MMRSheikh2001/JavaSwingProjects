/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poshomepractice.util;

/**
 *
 * @author Admin
 */
public class SalesUtil {

    public double getTotalPrice(double unitPrice, double quantity) {

        return unitPrice * quantity;
    }

    public double getDiscountAmount(double totalPrice, double discountRate) {
        return totalPrice * discountRate / 100;

    }

    public double getActualPrice(double totalPrice, double discountAmount) {

        return totalPrice - discountAmount;
    }
}
