package Controllers;

import Features.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author 33649
 */
public class signInController implements Initializable {

    /////////////////////////// Définition des éléments //////////////////////
    
    @FXML
    private Label label;
    
    @FXML
    private Button buttonLogin;
    
    @FXML
    private TextField mailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button buttonForgotPass;
    
    @FXML
    private Button buttonCreateAccount;
    
    @FXML
    private Text badCredentialsError;
    
    @FXML
    private Text invalidMailError;
    
    @FXML
    private CheckBox remindMe;
    
    User user; // Creation de l'utilisateur
    
    ///////////////////////////////Définition des méthodes HandleAction ///////////////////////////////
    public void setUser(User user){
        this.user = user;
        if(user.checkRemindMe()){//fichier avec identifiants stockés existants
            try {
                user.readRemindMe(); //lecture de ce fichier
            } catch (IOException ex) {
                Logger.getLogger(signInController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //écriture des informations sur l'interfaces
            mailField.setText(user.getMail());
            passwordField.setText(user.getPswd());
            remindMe.setSelected(true); //on coche par défaut la case "se souvenir de moi"
        }
    }
    
    /* -- Lancer la scene "Nouveau Compte" -- */
    @FXML
    private void onbuttonCreateAccountPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/newAccount.fxml"));
        Parent root = (Parent) loader.load();
        newAccountController ctrl;
        ctrl = loader.getController();
        ctrl.setUser(user);
        Scene forgotPass = new Scene(root);
        
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
        app_stage.setTitle("Créer un nouveau compte");
        app_stage.hide(); //optional
        app_stage.setScene(forgotPass);
        app_stage.setResizable(false);
        app_stage.show();  
    }
    
    /* -- Lance la scene MDP oublié -- */
    @FXML
    private void onButtonForgotPassPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/forgotPass.fxml"));
        Parent root = (Parent) loader.load();
        forgotPasswordController ctrl;
        ctrl = loader.getController();
        ctrl.setUser(user);
        Scene forgotPass = new Scene(root);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setTitle("Mot de passe oublié");
        app_stage.hide(); //optional
        app_stage.setScene(forgotPass);
        app_stage.setResizable(false);
        app_stage.show();
    }
    
    /* -- Boutton "Login" -- */
    @FXML
    private void onButtonLoginPressed(ActionEvent event) throws IOException, Exception {
        if(remindMe.isSelected() && !user.checkRemindMe())user.createRemindMe(mailField.getText() , passwordField.getText());
        else if(!remindMe.isSelected() && user.checkRemindMe()) user.removeRemindMe();
        else if(remindMe.isSelected() && user.checkRemindMe()){
            user.removeRemindMe();
            user.createRemindMe(mailField.getText() , passwordField.getText());
        }
        if(user.signIn(mailField.getText(), passwordField.getText())){
            if (user.isAdmin() == true){
                user.getData().loadConstraints();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/homeAdmin.fxml"));
                Parent root = (Parent) loader.load();
                homeAdminController ctrl;
                ctrl = loader.getController();
                ctrl.setUser(user);
                Scene homeAdmin = new Scene (root);

                Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                app_stage.setTitle("DPU - Administrateur");
                app_stage.hide(); //optional
                app_stage.setScene(homeAdmin);
                app_stage.setResizable(false);
                app_stage.show();
            }else{
                user.getData().loadConstraints();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/home.fxml"));
                Parent root = (Parent) loader.load();
                homeController ctrl;
                ctrl = loader.getController();
                ctrl.setUser(user);
                Scene home = new Scene (root);

                Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                app_stage.setTitle("DPU - Administrateur");
                app_stage.hide(); //optional
                app_stage.setScene(home);
                app_stage.setResizable(false);
                app_stage.show();
            }
            
        }else badCredentialsError.setOpacity(1);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
