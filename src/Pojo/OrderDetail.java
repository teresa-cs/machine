/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pojo;

/**
 *
 * @author trang
 */
public class OrderDetail {
    private int id;
    private int bev_id;
    private int order_id;
    private int quantity;
    private int promo_id;

    
    public OrderDetail(int bev_id, int quantity){
        this.bev_id= bev_id;
        this.quantity= quantity;
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the bev_id
     */
    public int getBev_id() {
        return bev_id;
    }

    /**
     * @param bev_id the bev_id to set
     */
    public void setBev_id(int bev_id) {
        this.bev_id = bev_id;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    /**
     * @return the promo_id
     */
    public int getPromo_id() {
        return promo_id;
    }

    /**
     * @param promo_id the promo_id to set
     */
    public void setPromo_id(int promo_id) {
        this.promo_id = promo_id;
    }
    
}
