/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Features.User;
import Tools.SendMail;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author 33649
 */
public class forgotPasswordController implements Initializable {
    
    ///////////////////// Definition des elements /////////////////////////
    
    @FXML
    private TextField mailField;
    @FXML
    private Text errorMailInvalid;
    @FXML
    private Text textCorrectMail;
    
    User user;
    
    ///////////////////// Definition des methodes /////////////////////////
    
    
    public void setUser(User user){
        this.user = user;
    }
    
    /* -- verification et recuperation de l'email valide -- */
    @FXML
    private void onButtonSendPressed(ActionEvent event) throws Exception {
        if (mailField.getText().equals("")){
            errorMailInvalid.setOpacity(1);
            textCorrectMail.setOpacity(0);
        }else{
            errorMailInvalid.setOpacity(0);
            System.out.println(mailField.getText());
            SendMail mail = new SendMail(mailField.getText());
            textCorrectMail.setOpacity(1);
            user.setMail(mailField.getText());
            user.forgotPass();
        }
    }
    
    /* -- retour a la connexion -- */
    @FXML
     private void onButtonBackSignInPressed(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/connection.fxml"));
        Parent root = (Parent) loader.load();
        signInController ctrl;
        ctrl = loader.getController();
        ctrl.setUser(user);
        
        Scene connexion = new Scene(root);
        
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                
        app_stage.setTitle("Connexion");
        app_stage.hide(); //optional
        app_stage.setScene(connexion);
        app_stage.setResizable(false);
        app_stage.show();
     }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
