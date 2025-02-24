/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Ihara
 */
public class InvoicesProductsreportBean {
    private String id;
    private String product_name;
    private String qty;
    private String selling_price;
    private String total;

    public InvoicesProductsreportBean(String id, String product_name, String qty, String selling_price, String total) {
        this.id = id;
        this.product_name = product_name;
        this.qty = qty;
        this.selling_price = selling_price;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    
    
}
