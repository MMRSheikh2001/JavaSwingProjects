/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poshomepractice.model;

/**
 *
 * @author Administrator
 */
public class Supplier {

    private int id;
    private String name;
    private String cell;
    private String contactPersonName;
    private String contactPersonCell;
    private String address;

    public Supplier() {
    }

    public Supplier(int id, String name, String cell, String contactPersonName, String contactPersonCell, String address) {
        this.id = id;
        this.name = name;
        this.cell = cell;
        this.contactPersonName = contactPersonName;
        this.contactPersonCell = contactPersonCell;
        this.address = address;
    }

    public Supplier(String name, String cell, String contactPersonName, String contactPersonCell, String address) {
        this.name = name;
        this.cell = cell;
        this.contactPersonName = contactPersonName;
        this.contactPersonCell = contactPersonCell;
        this.address = address;
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

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonCell() {
        return contactPersonCell;
    }

    public void setContactPersonCell(String contactPersonCell) {
        this.contactPersonCell = contactPersonCell;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Supplier{" + "id=" + id + ", name=" + name + ", cell=" + cell + ", contactPersonName=" + contactPersonName + ", contactPersonCell=" + contactPersonCell + ", address=" + address + '}';
    }

}
