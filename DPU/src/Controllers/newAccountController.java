/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Features.User;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author 33649
 */
public class newAccountController implements Initializable {
    //////////////////// Definition des elements /////////////////////
    
    @FXML
    private Label label;
    
    @FXML
    private TextField mailField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField passwordFieldConfirm;
    
    @FXML
    private Text errorPasswordNotSame;
    @FXML
    private Text errorEmptyField;
    
    //pour les champs trop long
    @FXML
    private Text errorMailTooLong;
    @FXML
    private Text errorNameTooLong;
    @FXML
    private Text errorPaswdTooLong;
    
    User user;
    
    /////////////////////// Definition des methodes handleAction //////////////////////////
    /* -- recupere les informations -- */
    
    public void setUser(User user){
        this.user = user;
    }
    
    @FXML
    private void onButtonValidatePressed(ActionEvent event) throws Exception {
        boolean validInformations = true;
        
        // Check la longueur des champs pour que ca rentre dans la bd
        if (mailField.getText().length() > 80){
            errorMailTooLong.setOpacity(1);
            validInformations = false;
        }
        if (firstNameField.getText().length() > 40 | surnameField.getText().length() > 40){
            errorNameTooLong.setOpacity(1);
            validInformations = false;
        }
        if (passwordField.getText().length() > 100){
            errorPaswdTooLong.setOpacity(1);
            validInformations = false;
        }
        
        //test si aucun champ n'est nul
        if(firstNameField.getText().equals("")|surnameField.getText().equals("")|mailField.getText().equals("")|passwordField.getText().equals("")){
            errorEmptyField.setOpacity(1);
            validInformations = false;
        }else{ // recup les infos
            errorEmptyField.setOpacity(0);
            if(passwordField.getText().equals(passwordFieldConfirm.getText())){
                errorPasswordNotSame.setOpacity(0);
                user = new User(mailField.getText(), passwordField.getText(), firstNameField.getText(), surnameField.getText());
            }else{
                errorPasswordNotSame.setOpacity(1);
                validInformations = false;
            }
        }
        
        if(validInformations){
            if (!user.signUp(mailField.getText(), passwordField.getText(), firstNameField.getText(), surnameField.getText())){
                Alert alertAccountExist = new Alert(AlertType.WARNING);
                alertAccountExist.setTitle("Erreur : compte déjà existant");
                alertAccountExist.setHeaderText("Erreur : compte déjà existant");
                alertAccountExist.setContentText("Un compte a déjà créé avec l'adresse mail mentionnée, veuillez réessayer");
                alertAccountExist.showAndWait();
            }else{
                Alert accountCreated = new Alert(AlertType.INFORMATION);
                accountCreated.setTitle("Compte créé");
                accountCreated.setHeaderText(null);
                accountCreated.setContentText("Votre compte a bien été créé, vous pouvez à présent vous connecter !");
                accountCreated.showAndWait();
                user.removeRemindMe();
                user.createRemindMe(mailField.getText(), passwordField.getText());
                onButtonSignInReturnPressed(event);
            }
        }
    }
    /* -- retour a la page connexion -- */
    @FXML
    private void onButtonSignInReturnPressed(ActionEvent event) throws IOException {
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
