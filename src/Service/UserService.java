/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Pojo.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 *
 * @author trang
 */
public class UserService {
    public static User currentUser= new User();
    public static boolean checkLogin(String name, String pass) throws Exception {
        String sql = "Select * From user where username = ? and password  = ?";

        Connection connect = JDBCconn.getConnection();
        PreparedStatement stm = connect.prepareStatement(sql);
        stm.setString(1, name);
        stm.setString(2, pass);
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            currentUser.setIdUser(rs.getInt("id"));
            currentUser.setUserName(name);
            currentUser.setPassword(pass);
            currentUser.setMoney(rs.getDouble("money"));

            return true;
        }
        else{
            return false;
        } 
    }
    
    public static double wallet(User u) throws Exception {
        double moneyLeft=0;
        String sql = "Select total From machine.order where user_id=? ORDER BY id DESC LIMIT 1";

        Connection connect = JDBCconn.getConnection();
        PreparedStatement stm = connect.prepareStatement(sql);
        stm.setInt(1, u.getIdUser());
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            moneyLeft=u.getMoney()-rs.getDouble("total");    
        }
        else{
            moneyLeft=u.getMoney();
        }
        return moneyLeft;
    }

    public static boolean updateWallet(User u) throws SQLException, Exception {
        String sql = "UPDATE machine.user SET money=? WHERE id=?";

        Connection conn = JDBCconn.getConnection();
        PreparedStatement stm = conn.prepareStatement(sql);
        
        stm.setDouble(1, wallet(u));
        stm.setInt(2, u.getIdUser());
        stm.executeUpdate();

        conn.commit();
        return true;
    }

}
