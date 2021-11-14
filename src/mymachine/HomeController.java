/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mymachine;

import Pojo.Order;
import Pojo.OrderDetail;
import Pojo.User;
import Service.BeverageService;
import Service.OrderService;
import Service.UserService;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author trang
 */
public class HomeController implements Initializable {

    @FXML
    private ComboBox cbMoneyIn;
    @FXML
    private Button btnOK;
    @FXML
    private Spinner cocaCount;
    @FXML
    private Spinner pepsiCount;
    @FXML
    private Spinner sodaCount;
    @FXML
    private Text txtTotal;
    @FXML
    private Text txtWallet;
    @FXML
    private Text txtName;
    @FXML
    private Text txtMoneyIn;
    @FXML
    private Button btnNhap;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        BeverageService.addSpinnerValue(sodaCount);
        BeverageService.addSpinnerValue(cocaCount);
        BeverageService.addSpinnerValue(pepsiCount);
        List<String> moneyIn = new ArrayList<String>() {
        };
        moneyIn.add("10000");
        moneyIn.add("20000");
        moneyIn.add("50000");
        moneyIn.add("100000");
        moneyIn.add("200000");
        this.cbMoneyIn.getItems().addAll(moneyIn);
        this.txtMoneyIn.setText("0");
        txtWallet.setText(String.valueOf(UserService.currentUser.getMoney()));
    }

    public void addMoney(ActionEvent event) {
        if (this.cbMoneyIn.getValue().toString() != null) {
            if (Double.parseDouble(this.cbMoneyIn.getValue().toString())
                    <= Double.parseDouble(this.txtWallet.getText())) {
                
                Double moneyIn = Double.parseDouble(this.txtMoneyIn.getText())
                        +  Double.parseDouble(this.cbMoneyIn.getValue().toString());
                
                this.txtMoneyIn.setText(moneyIn.toString());
                
                Double moneyLeft = Double.parseDouble(this.txtWallet.getText())
                        - Double.parseDouble(this.cbMoneyIn.getValue().toString());
                
                txtWallet.setText(String.valueOf(moneyLeft));
                
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setContentText("Not enough money!");
                alert1.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please choose the denomination!");
            alert.showAndWait();
        }
    }

    public void addOrder(ActionEvent event) {
        int total;
        int coca = (int) cocaCount.getValue() * 10000;
        int pepsi = (int) pepsiCount.getValue() * 10000;
        int soda = (int) sodaCount.getValue() * 20000;

        total = coca + pepsi + soda;

        txtTotal.setText(Integer.toString(total));
    }

    public void newOrder(ActionEvent event) throws ParseException, SQLException, Exception {
        if (Double.parseDouble(txtTotal.getText()) <= Double.parseDouble(txtMoneyIn.getText())) {

            Order d = new Order(Double.parseDouble(this.txtTotal.getText()));

            OrderDetail cocaOd = new OrderDetail(1, (int) cocaCount.getValue());
            OrderDetail pepsiOd = new OrderDetail(2, (int) pepsiCount.getValue());
            OrderDetail sodaOd = new OrderDetail(3, (int) sodaCount.getValue());

            if (OrderService.addOrder(d, cocaOd, pepsiOd, sodaOd, UserService.currentUser.getIdUser()) == true) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                
                if (OrderService.winOrNot == 1) {
                    alert.setContentText("Order successfully!\n"
                            + " You had paid " + txtTotal.getText() + " for "
                            + cocaCount.getValue() + " coca,"
                            + pepsiCount.getValue() + " pepsi and "
                            + sodaCount.getValue() + " soda\n"
                            + " You received 1 can for free ^^");
                } else {
                    alert.setContentText("Order successfully!\n"
                            + " You had paid " + txtTotal.getText() + " for "
                            + cocaCount.getValue() + " coca,"
                            + pepsiCount.getValue() + " pepsi and "
                            + sodaCount.getValue() + " soda\n");
                }
                alert.showAndWait();
                
                UserService.checkLogin(UserService.currentUser.getUserName(), UserService.currentUser.getPassword());
                UserService.updateWallet(UserService.currentUser);
                
                cocaCount.getValueFactory().setValue(0);
                pepsiCount.getValueFactory().setValue(0);
                sodaCount.getValueFactory().setValue(0);
                double moneyLeft = Double.parseDouble(txtMoneyIn.getText())
                        - Double.parseDouble(txtTotal.getText());
                txtMoneyIn.setText(String.valueOf(moneyLeft));
                txtTotal.setText("");
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Not enough money!");
            alert.showAndWait();
        }

    }

    public void cancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Choose to choice");
        alert.setContentText("Cancel the transaction ?");

        ButtonType btnYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(btnNo, btnYes);
        Optional<ButtonType> result = alert.showAndWait();
        alert.show();

        if (result.get() == btnYes) {
            alert.close();

            Double moneyLeft = Double.parseDouble(this.txtWallet.getText())
                    + Double.parseDouble(this.txtMoneyIn.getText());
            txtWallet.setText(String.valueOf(moneyLeft));
            cocaCount.getValueFactory().setValue(0);
            pepsiCount.getValueFactory().setValue(0);
            sodaCount.getValueFactory().setValue(0);
            cbMoneyIn.setValue(null);
            txtTotal.setText("");
            this.txtMoneyIn.setText("0");
            
        } else {
            alert.close();
        }
    }

}
