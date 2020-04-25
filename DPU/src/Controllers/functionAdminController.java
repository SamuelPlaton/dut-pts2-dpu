/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import CSV.FilesCSV;
import Features.User;
import Tools.MySqlConn;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 *
 * @author tomte
 */
public class functionAdminController implements Initializable {
    @FXML
    private AnchorPane APBuild;
    @FXML
    private AnchorPane APDeleteUsers;
    @FXML
    private AnchorPane APDefineWeeks;
    
    
    @FXML
    private Button buttonViewConstruct;
    @FXML
    private Button buttonViewDefineWeeks;
    @FXML 
    private Button buttonViewDeleteUser;
    
    
    
    @FXML 
    private ComboBox CBUsers;
    @FXML 
    private Text textDeleteUser;
    @FXML
    private Label LabelErrorWeeks;
    
    
    
    @FXML
    private ComboBox CBWeeks;
    @FXML
    private Label LabelErrorDelete;
    @FXML
    private Label LabelConstructWeeks;
    
    
    
    @FXML
    private ComboBox CBHolidays;
    @FXML
    private Label LabelWeeksHolidays;
    @FXML
    private Label LabelErrorHolidays;
    
    @FXML
    private Label nameAdmin;
    @FXML
    private Label firstnameAdmin;
    @FXML
    private Label mailAdmin;
    
    private Statement st;
    private int key;
    
    private ObservableList<String> userList = FXCollections.observableArrayList(); 
    private HashMap<String, Integer> Users = new HashMap<String, Integer>();
    
    private ObservableList<String> weekList = FXCollections.observableArrayList();
    private HashMap<String, Integer> Weeks = new HashMap<String, Integer>();
    
    private ObservableList<String> HolidayList = FXCollections.observableArrayList();
    private HashMap<String, Integer> Holidays = new HashMap<String, Integer>();
        
    
   //private final Color Blue = new Color(25, 113, 186);
    
    
    @FXML
    private void handleButtonDefineWeeksClicked() throws SQLException{ 
        APBuild.setVisible(false);
        APDeleteUsers.setVisible(false);
        APDefineWeeks.setVisible(true);
        
        LabelErrorWeeks.setVisible(false);
        LabelErrorDelete.setVisible(false);
        LabelConstructWeeks.setVisible(false);
        
        LabelWeeksHolidays.setVisible(false);
        LabelErrorHolidays.setVisible(false);
        
        
        buttonViewDefineWeeks.setStyle("-fx-border-radius: 100px; -fx-text-fill: white; -fx-background-radius: 100px; -fx-border-color: black; -fx-background-color: #1971ba; -fx-font-weight: bold;");
        buttonViewConstruct.setStyle("-fx-border-radius: 100px; -fx-text-fill: black; -fx-background-radius: 100px; -fx-border-color: black; -fx-background-color: none; -fx-font-weight: normal;");
        buttonViewDeleteUser.setStyle("-fx-border-radius: 100px; -fx-text-fill: black; -fx-background-radius: 100px; -fx-border-color: black; -fx-background-color: none; -fx-font-weight: normal;");
        textDeleteUser.setVisible(false);
        
        ResultSet result = null;
        try {
            result = st.executeQuery("SELECT W_NUM, BEGIN_DATE, END_DATE FROM WEEKS WHERE CONSTRUCT = 's' ORDER BY BEGIN_DATE ASC");
        } catch (SQLException ex) {
            Logger.getLogger(functionAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            CBHolidays.getItems().clear();
            while(result.next())
            {
                CBHolidays.getItems().add(result.getString("BEGIN_DATE") + " au " + result.getString("END_DATE"));
                Holidays.put(result.getString("BEGIN_DATE") + " au " + result.getString("END_DATE"), result.getInt("W_NUM"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(functionAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (String i : Holidays.keySet()) {
            System.out.println("Name: " + i + " ID : " + Holidays.get(i));
        }
        
        
    }
    
    public void handleButtonDefineWeeks() throws SQLException{
        
        if(CBHolidays.getValue() == null){
            System.err.println("Impossible, Aucune Semaine selectionné");
            LabelErrorHolidays.setVisible(true);
            LabelWeeksHolidays.setVisible(false);
        }
        else{
            System.out.println(CBHolidays.getValue());
            st.executeUpdate("UPDATE WEEKS SET CONSTRUCT = 'H' WHERE W_NUM=" + Holidays.get(CBHolidays.getValue()));
            System.out.println("update ");
            LabelWeeksHolidays.setVisible(true);
            LabelErrorHolidays.setVisible(false);

            Holidays.clear();
            HolidayList.remove(CBHolidays.getValue());

            CBHolidays.setValue(null);
            CBHolidays.getItems().clear();
            
            ResultSet result = null;
        try {
            result = st.executeQuery("SELECT W_NUM, BEGIN_DATE, END_DATE FROM WEEKS WHERE CONSTRUCT <> 'H' ORDER BY BEGIN_DATE ASC");
        } catch (SQLException ex) {
            Logger.getLogger(functionAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            CBHolidays.getItems().clear();
            while(result.next())
            {
                CBHolidays.getItems().add(result.getString("BEGIN_DATE") + " au " + result.getString("END_DATE"));
                Holidays.put(result.getString("BEGIN_DATE") + " au " + result.getString("END_DATE"), result.getInt("W_NUM"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(functionAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (String i : Holidays.keySet()) {
            System.out.println("Name: " + i + " ID : " + Holidays.get(i));
        }
        }
        
    }
    
    
    @FXML
    private void handleButtonDeleteUserClicked() throws SQLException{
        APBuild.setVisible(false);
        APDeleteUsers.setVisible(true);
        APDefineWeeks.setVisible(false);
        

        LabelErrorDelete.setVisible(false);
        textDeleteUser.setVisible(false);
        
        LabelErrorWeeks.setVisible(false);
        LabelConstructWeeks.setVisible(false);
        
        LabelWeeksHolidays.setVisible(false);
        LabelErrorHolidays.setVisible(false);
        
        buttonViewDeleteUser.setStyle("-fx-border-radius: 100px; -fx-text-fill: white; -fx-background-radius: 100px; -fx-border-color: black; -fx-background-color: #1971ba; -fx-font-weight: bold;");
        buttonViewDefineWeeks.setStyle("-fx-border-radius: 100px; -fx-text-fill: black; -fx-background-radius: 100px; -fx-border-color: black; -fx-background-color: none; -fx-font-weight: normal;");
        buttonViewConstruct.setStyle("-fx-border-radius: 100px; -fx-text-fill: black; -fx-background-radius: 100px; -fx-border-color: black; -fx-background-color: none; -fx-font-weight: normal;");
        
        
        CBUsers.getItems().clear();
        
        ResultSet result = st.executeQuery("SELECT ID_USER, MAIL_USER FROM USERS ORDER BY FIRSTNAME, SURNAME, MAIL_USER ASC");
        
        while(result.next())
        {
            CBUsers.getItems().add(result.getString("MAIL_USER"));
            Users.put(result.getString("MAIL_USER"), result.getInt("ID_USER"));
        }
        
        for (String i : Users.keySet()) {
            System.out.println("Name: " + i + " ID : " + Users.get(i));
        }
        
    }
    @FXML
    public void handleButtonDeleteUser() throws SQLException, Exception{
        
        if(CBUsers.getValue() == null){
            System.err.println("Impossible de suppriner, aucun utilisateur selectionné");
            LabelErrorDelete.setVisible(true);
            textDeleteUser.setVisible(false);
        }
        else{
            LabelErrorDelete.setVisible(false);
            System.out.println(CBUsers.getValue().toString());       
            System.out.println(Users.get(CBUsers.getValue()));
            st.executeUpdate("DELETE FROM USERS WHERE ID_USER=" + Users.get(CBUsers.getValue()));
            System.out.println("deleted User: " + CBUsers.getValue());

            textDeleteUser.setVisible(true);

            Users.clear();
            userList.remove(CBUsers.getValue());

            CBUsers.setValue(null);
            CBUsers.getItems().clear();

            ResultSet result = st.executeQuery("SELECT ID_USER, MAIL_USER FROM USERS ORDER BY FIRSTNAME, SURNAME, MAIL_USER ASC");

            while(result.next())
            {
                CBUsers.getItems().add(result.getString("MAIL_USER"));
                Users.put(result.getString("MAIL_USER"), result.getInt("ID_USER"));
            }

            for (String i : Users.keySet()) {
                System.out.println("Name: " + i + " ID : " + Users.get(i));
            }       
        }
    }
    
    @FXML
    private void handleButtonConstructClicked(){
        APBuild.setVisible(true);
        APDeleteUsers.setVisible(false);
        APDefineWeeks.setVisible(false);
        
        LabelErrorWeeks.setVisible(false);
        LabelConstructWeeks.setVisible(false);
        
        LabelWeeksHolidays.setVisible(false);
        LabelErrorHolidays.setVisible(false);
        
        textDeleteUser.setVisible(false);
        LabelErrorDelete.setVisible(false);

        buttonViewConstruct.setStyle("-fx-border-radius: 100px; -fx-text-fill: white; -fx-background-radius: 100px; -fx-border-color: black; -fx-background-color: #1971ba; -fx-font-weight: bold;");
        buttonViewDefineWeeks.setStyle("-fx-border-radius: 100px; -fx-text-fill: black; -fx-background-radius: 100px; -fx-border-color: black; -fx-background-color: none; -fx-font-weight: normal;");
        buttonViewDeleteUser.setStyle("-fx-border-radius: 100px; -fx-text-fill: black; -fx-background-radius: 100px; -fx-border-color: black; -fx-background-color: none; -fx-font-weight: normal;");
        
        
        
        ResultSet result = null;
        try {
            result = st.executeQuery("SELECT W_NUM, BEGIN_DATE, END_DATE FROM WEEKS WHERE CONSTRUCT = 's' ORDER BY BEGIN_DATE ASC");
        } catch (SQLException ex) {
            Logger.getLogger(functionAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            CBWeeks.getItems().clear();
            while(result.next())
            {
                CBWeeks.getItems().add(result.getString("BEGIN_DATE") + " au " + result.getString("END_DATE"));
                Weeks.put(result.getString("BEGIN_DATE") + " au " + result.getString("END_DATE"), result.getInt("W_NUM"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(functionAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    @FXML
    private void handleButtonDownloadClicked() throws SQLException, Exception{
        //FilesCSV file = new FilesCSV();
        
        ResultSet result = st.executeQuery("SELECT SURNAME, FIRSTNAME, PASSWORD, MAIL_USER FROM USERS");
        
        while(result.next()){
            String surname = result.getString("SURNAME");
            String firstname = result.getString("FIRSTNAME");
            String pswd = result.getString("PASSWORD");
            String mail = result.getString("MAIL_USER");
            
            User user = new User(mail, pswd, firstname, surname);
            
            FilesCSV DownloadFile = new FilesCSV(user);
            
            user = null;
        }
        
    }
    
    @FXML
    private void handleButtonConstruct() throws SQLException{
        
        if(CBWeeks.getValue() == null){
            System.err.println("Impossible de construire, Aucune Semaine selectionné");
            LabelErrorWeeks.setVisible(true);
            LabelConstructWeeks.setVisible(false);
        }
        else{
            st.executeUpdate("UPDATE WEEKS SET CONSTRUCT = 'S' WHERE W_NUM=" + Weeks.get(CBWeeks.getValue()));
            LabelConstructWeeks.setVisible(true);
            LabelErrorWeeks.setVisible(false);

            Weeks.clear();
            weekList.remove(CBWeeks.getValue());

            CBWeeks.setValue(null);
            CBWeeks.getItems().clear();
            
            ResultSet result = null;
        try {
            result = st.executeQuery("SELECT W_NUM, BEGIN_DATE, END_DATE FROM WEEKS WHERE CONSTRUCT = 's' ORDER BY BEGIN_DATE ASC");
        } catch (SQLException ex) {
            Logger.getLogger(functionAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            CBWeeks.getItems().clear();
            while(result.next())
            {
                CBWeeks.getItems().add(result.getString("BEGIN_DATE") + " au " + result.getString("END_DATE"));
                Weeks.put(result.getString("BEGIN_DATE") + " au " + result.getString("END_DATE"), result.getInt("W_NUM"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(functionAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        }
    }
    
    @FXML
    private void handleButtonSeeWeeksClicked() throws SQLException{
        
        String test = "";
        ResultSet result = st.executeQuery("SELECT W_NUM, BEGIN_DATE, END_DATE FROM WEEKS WHERE CONSTRUCT = 'S' ORDER BY BEGIN_DATE DESC");
        while(result.next()){
            test += "Semaine " + result.getInt("W_NUM") + " : ";
            test += "Du " + result.getString("BEGIN_DATE") + " au " + result.getString("END_DATE");
            test += "\n\n";
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFORMATION");
        alert.setHeaderText("Semaines construites :");

        TextArea area = new TextArea(test);
        area.setWrapText(true);
        area.setEditable(false);

        alert.getDialogPane().setContent(area);
        alert.setResizable(false);

        alert.showAndWait();
        
    }
    
    User user;   
    
    void setUser(User user) {
        this.user = user;
    }
    
    @FXML
    private ImageView home;
    
    @FXML
    private ImageView deconnection;
    
    @FXML
    private void onHomePressed(MouseEvent event) throws SQLException, IOException{
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
    
    @FXML
    private void onOutPressed(MouseEvent event) throws SQLException, IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/connection.fxml"));
        Parent root = (Parent) loader.load();
        signInController ctrl;
        ctrl = loader.getController();
        ctrl.setUser(user);
        Scene signIn = new Scene(root);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setTitle("Connexion");
        app_stage.hide(); //optional
        app_stage.setScene(signIn);
        app_stage.setResizable(false);
        app_stage.show();
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            MySqlConn conn = new MySqlConn();
            st = conn.getStatement();
        } catch (Exception ex) {
            Logger.getLogger(functionAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        ResultSet result = null;
        try {
            result = st.executeQuery("SELECT W_NUM, BEGIN_DATE, END_DATE FROM WEEKS WHERE CONSTRUCT = 's' ORDER BY BEGIN_DATE ASC");
        } catch (SQLException ex) {
            Logger.getLogger(functionAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            CBWeeks.getItems().clear();
            while(result.next())
            {
                CBWeeks.getItems().add(result.getString("BEGIN_DATE") + " au " + result.getString("END_DATE"));
                Weeks.put(result.getString("BEGIN_DATE") + " au " + result.getString("END_DATE"), result.getInt("W_NUM"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(functionAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            result = st.executeQuery("SELECT SURNAME, FIRSTNAME, MAIL_USER FROM USERS WHERE RANK_USER = 'ADMIN'");
        } catch (SQLException ex) {
            Logger.getLogger(functionAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            while(result.next())
            {
                nameAdmin.setText(result.getString("SURNAME"));
                firstnameAdmin.setText(result.getString("FIRSTNAME"));
                mailAdmin.setText(result.getString("MAIL_USER"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(functionAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }    
}
