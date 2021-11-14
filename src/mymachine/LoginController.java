/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mymachine;

import Pojo.User;
import Service.UserService;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author trang
 */
public class LoginController implements Initializable {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
      public void Login(ActionEvent event) throws Exception {
        if (!this.txtUsername.getText().equals("") && !this.txtPassword.getText().equals("")
                && verifyID(this.txtUsername)) {
            if (UserService.checkLogin(this.txtUsername.getText(), this.txtPassword.getText())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Log in successfully!");
                alert.showAndWait();
                    Main.setRoot("Home");
                
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong password or username! Please try again");
                alert.showAndWait();  
                this.txtPassword.clear();
                this.txtUsername.clear();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please complete your form before submit!");
            alert.showAndWait();
        }
    }
      
      public boolean verifyID(TextField txt) {
        Pattern p = Pattern.compile("[a-z-|_|.]+");
        Matcher m = p.matcher(txt.getText());

        if (m.find() && m.group().equals(txt.getText())) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter valid character!");
            alert.showAndWait();
            txt.clear();
            return false;
        }
    }
    
}
