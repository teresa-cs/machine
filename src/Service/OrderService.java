/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;
import Pojo.Order;
import Pojo.OrderDetail;
import Pojo.Promotion;
import java.sql.Connection;
import java.text.ParseException;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 *
 * @author trang
 */
public class OrderService {
    
    public static int winOrNot=0;
    
        public static boolean addOrder(Order b,OrderDetail coca, OrderDetail pepsi, OrderDetail soda, int idUser) throws ParseException, SQLException {
        String sql = "INSERT INTO machine.order(id, user_id, total, created_date) VALUES (?,?,?,?)";
        Connection conn = JDBCconn.getConnection();
        
         Date date=java.util.Calendar.getInstance().getTime();
         
//         String stringDate = "2021-11-15";
//		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);///test

        conn.setAutoCommit(false);
        PreparedStatement stm = conn.prepareStatement(sql);

        stm.setInt(1, b.getId());
        stm.setInt(2, idUser);
        stm.setDouble(3, b.getTotal());
        stm.setDate(4, new java.sql.Date(date.getTime()));

        stm.executeUpdate();
        
        conn.commit();
        ////////////
        String query = "SELECT * FROM machine.order ORDER BY id DESC LIMIT 1";

        Connection connect = JDBCconn.getConnection();
        PreparedStatement p = connect.prepareStatement(query);
        ResultSet rs = p.executeQuery();
        while (rs.next()) {
            addOrderDetail(soda, rs.getInt("id"), rs.getDate("created_date"),20000);
            addOrderDetail(coca, rs.getInt("id"), rs.getDate("created_date"),10000);
            addOrderDetail(pepsi, rs.getInt("id"), rs.getDate("created_date"),10000);
        }

        return true;

    }

    public static boolean addOrderDetail(OrderDetail o, int order_id, Date created, double price) throws SQLException {
        if (o.getQuantity() != 0) {
            Promotion idP= new Promotion();

            String sql = "INSERT INTO machine.order_detail(id, bev_id, quantity, order_id, promo_id) VALUES (?,?,?,?,?)";
            Connection conn = JDBCconn.getConnection();

            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement(sql);

            stm.setInt(1, o.getId());
            stm.setInt(2, o.getBev_id());

            if (checkPromo(created) == true && o.getQuantity() >= 3) {
                idP= idPro(created);
                if (checkReach(idP, price) == true) {
                    stm.setInt(3, o.getQuantity() + 1);
                    stm.setInt(5, idP.getId());
                    winOrNot=1;
                } else {
                    stm.setInt(3, o.getQuantity());
                    stm.setNull(5, 0);
                    winOrNot=0;
                }
            } else {
                stm.setInt(3, o.getQuantity());
                stm.setNull(5, 0);
                 winOrNot=0;
            }
            stm.setDouble(4, order_id);
            stm.executeUpdate();

            conn.commit();
            return true;
        }
        return false;
    }
    
   

    public static boolean checkPromo(Date created) throws SQLException {
        String query = "SELECT * FROM machine.promotion WHERE date=?";
        boolean kq=false;

        Connection connect = JDBCconn.getConnection();
        PreparedStatement p = connect.prepareStatement(query);
        p.setDate(1, new java.sql.Date(created.getTime()));
        ResultSet rs = p.executeQuery();
        if (rs.next() == true) {
                double x = (Math.random()*((100-1)+1))+1;///random số trúng
//                    double x =2;//test
                    double win_rate = rs.getDouble("win_rate") * 100;
                    if (0 <= x && x <= win_rate) {// win rate zone
                        kq= true;
                    }
                    else
                        kq= false;
       
        } else {
            addPromo(created);
            checkPromo(created);
            kq= true;
        }
        return kq;
    }

    public static Promotion idPro(Date created) throws SQLException {
        Promotion promo = new Promotion();
        String query = "SELECT * FROM machine.promotion WHERE date=?";

        Connection connect = JDBCconn.getConnection();
        PreparedStatement p = connect.prepareStatement(query);
        p.setDate(1, new java.sql.Date(created.getTime()));
        ResultSet rs = p.executeQuery();
        while (rs.next()) {
            promo.setId(rs.getInt("id"));
            promo.setBudget(rs.getDouble("budget"));
            promo.setWin_rate(rs.getDouble("win_rate"));
            promo.setReach(rs.getDouble("reach"));
            promo.setDate(rs.getDate("date"));

        }
        return promo;
    }
    
     public static boolean updateReach(Promotion promo, double price) throws SQLException{
        
         double update= promo.getReach()- price;
          String sql= "UPDATE machine.promotion SET reach=? WHERE id=?";
        Connection conn = JDBCconn.getConnection();
        
            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement(sql);

            stm.setDouble(1, update);
            stm.setInt(2, promo.getId());     

            stm.executeUpdate();
            conn.commit();
            return true;
        
     }
     public static boolean addPromo(Date newDate) throws SQLException{
         Date date= new Date();
         Calendar c = Calendar.getInstance();
         c.setTime(newDate);
         c.roll(Calendar.DATE, -1);
         date= c.getTime();
         
        
       Promotion pro1 =  idPro(date);
       Promotion pro2 = new Promotion();
       if(pro1.getReach()==0){
           pro2.setWin_rate(pro1.getWin_rate()+ pro1.getWin_rate()* 0.5);
           
       }
       else
           pro2.setWin_rate(pro1.getWin_rate());
       
       
       String sql = "INSERT INTO machine.promotion(id, budget, win_rate, date , reach) VALUES (?,?,?,?,?)";
        Connection conn = JDBCconn.getConnection();
        
        conn.setAutoCommit(false);
        PreparedStatement stm = conn.prepareStatement(sql);

        stm.setNull(1, pro2.getId());
        stm.setDouble(2, 50000);
        stm.setDouble(3, pro2.getWin_rate());
        stm.setDate(4,  new java.sql.Date(newDate.getTime()));
        stm.setDouble(5, 50000);
        
        stm.executeUpdate();
        
        conn.commit();
        return true;
    }

    public static boolean checkReach(Promotion p, double price) throws SQLException {
        double reach= p.getReach();
        if (reach >price || p.getReach()==price) {          
            return updateReach(p, price);
        } else {
            return false;
        }

    }
}
