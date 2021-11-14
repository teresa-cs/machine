/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pojo;

import java.util.Date;

/**
 *
 * @author trang
 */
public class Promotion {
    private int id;
    private double budget;
    private double win_rate;
    private Date date;
    private double reach;

    public Promotion(int id, double budget, double win_rate, Date date, double reach) {
        this.id = id;
        this.budget = budget;
        this.win_rate = win_rate;
        this.date = date;
        this.reach = reach;
    }
    public Promotion(){
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
     * @return the budget
     */
    public double getBudget() {
        return budget;
    }

    /**
     * @param budget the budget to set
     */
    public void setBudget(double budget) {
        this.budget = budget;
    }

    /**
     * @return the win_rate
     */
    public double getWin_rate() {
        return win_rate;
    }

    /**
     * @param win_rate the win_rate to set
     */
    public void setWin_rate(double win_rate) {
        this.win_rate = win_rate;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the reach
     */
    public double getReach() {
        return reach;
    }

    /**
     * @param reach the reach to set
     */
    public void setReach(double reach) {
        this.reach = reach;
    }
    
}
