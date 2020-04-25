package Controllers;

import Features.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class changePasswordController implements Initializable {

    @FXML
    private TextField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField newPasswordConfirmField;
    
    @FXML
    private Text errorEmptyField;
    @FXML
    private Text errorNotSamePassword;
    @FXML
    private Text textCorrectPassword;
    
    User user;
    
    public void setUser(User user){
        this.user = user;
    }
    
    @FXML
    private void onButtonValidatePressed(ActionEvent event) throws SQLException {
        if (oldPasswordField.getText().equals("")|newPasswordField.getText().equals("")|newPasswordConfirmField.getText().equals("")){
            errorEmptyField.setOpacity(1);
        }else{
            errorEmptyField.setOpacity(0);
            if(newPasswordField.getText().equals(newPasswordConfirmField.getText())){
                System.out.println(newPasswordField.getText());
                textCorrectPassword.setOpacity(1);
                errorNotSamePassword.setOpacity(0);
                user.setAccountPassword(newPasswordConfirmField.getText());
            }else{
                errorNotSamePassword.setOpacity(1);
            }
        }
    }
    
    @FXML
     private void onButtonHomeBackPressed(ActionEvent event) throws IOException, SQLException{
        try{
            user.getData().loadConstraints();
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
            
        }catch(IOException e){
            System.err.println(e);
        }  
    }
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
