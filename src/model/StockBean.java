/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Ihara
 */
public class StockBean {
    private String id;
    private String name;
    private String category;
    private String brand;
    private String qty;
    private String buying_price;
    private String selling_price;
    private String mfd;
    private String exd;

    public StockBean(String id, String name, String category, String brand, String qty, String buying_price, String selling_price, String mfd, String exd) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.qty = qty;
        this.buying_price = buying_price;
        this.selling_price = selling_price;
        this.mfd = mfd;
        this.exd = exd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getBuying_price() {
        return buying_price;
    }

    public void setBuying_price(String buying_price) {
        this.buying_price = buying_price;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public String getMfd() {
        return mfd;
    }

    public void setMfd(String mfd) {
        this.mfd = mfd;
    }

    public String getExd() {
        return exd;
    }

    public void setExd(String exd) {
        this.exd = exd;
    }
    
    
}
