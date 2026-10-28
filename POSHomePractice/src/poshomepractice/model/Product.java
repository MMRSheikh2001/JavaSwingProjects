/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poshomepractice.model;

/**
 *
 * @author Administrator
 */
public class Product {

    private int id;
    private String name;
    private double price;
    private double quantity;
    private int categoryId;
    private int supplierId;

    private String categoryName;
    private String supplierName;

    public Product() {
    }

    public Product(int id, String name, double price, double quantity, int categoryId, int supplierId, String categoryName, String supplierName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.categoryName = categoryName;
        this.supplierName = supplierName;
    }

    public Product(String name, double price, double quantity, int categoryId, int supplierId, String categoryName, String supplierName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.categoryName = categoryName;
        this.supplierName = supplierName;
    }

    public Product(int id, String name, double price, double quantity, int categoryId, int supplierId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
    }

    public Product(String name, double price, double quantity, int categoryId, int supplierId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
    }

    public Product(int id, String name, double price, double quantity, String categoryName, String supplierName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categoryName = categoryName;
        this.supplierName = supplierName;
    }

    public Product(String name, double price, double quantity, String categoryName, String supplierName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categoryName = categoryName;
        this.supplierName = supplierName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", price=" + price + ", quantity=" + quantity + ", categoryId=" + categoryId + ", supplierId=" + supplierId + ", categoryName=" + categoryName + ", supplierName=" + supplierName + '}';
    }

}
