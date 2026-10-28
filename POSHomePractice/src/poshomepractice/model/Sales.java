/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poshomepractice.model;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class Sales {

    private int id;
    private String productName;
    private double unitPrice;
    private double quantity;
    private double totalPrice;
    private double discount;
    private double actualPrice;
    private Date date;

    public Sales() {
    }

    public Sales(int id, String productName, double unitPrice, double quantity, double totalPrice, double discount, double actualPrice, Date date) {
        this.id = id;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.actualPrice = actualPrice;
        this.date = date;
    }

    public Sales(int id, String productName, double unitPrice, double quantity, double totalPrice, double discount, double actualPrice) {
        this.id = id;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.actualPrice = actualPrice;
    }

    public Sales(String productName, double unitPrice, double quantity, double totalPrice, double discount, double actualPrice) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.actualPrice = actualPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public java.sql.Date getSqlDate() {
        return new java.sql.Date(new Date().getTime());
    }

}
