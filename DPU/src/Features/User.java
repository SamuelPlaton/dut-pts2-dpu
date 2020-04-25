/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Features;

import Tools.MySqlConn;
import Tools.SendMail;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.firstInMonth;
import javax.mail.MessagingException;

/**
 *
 * @author alfred
 */
public class User {
    private String mail;
    private String pswd;
    private String firstname;
    private String surname;
    private MySqlConn userConn;
    private Statement st;
    private Data data;
    private int userId;
    private boolean isAdmin;
    private final File credentials = new File("Data\\credentials");

    public User(String mail, String pswd, String firstname, String surname) throws Exception {
        this.mail = mail;
        this.pswd = pswd;
        this.firstname = firstname;
        this.surname = surname;
        userConn = new MySqlConn();
        st = userConn.getStatement();
    }
    
    public User() throws Exception{
        userConn = new MySqlConn();
        st = userConn.getStatement();
    }
    
    public void initUser(String mail, String pswd) throws Exception {
        this.mail = mail;
        this.pswd = pswd;
        userConn = new MySqlConn();
        st = userConn.getStatement();
    }
    
    public MySqlConn getUserConn(String mail){
        return userConn; 
    }
    
    public String getFirstname(){
        return firstname;
    }
    
    public String getSurname(){
        return surname;
    }
    
    public String getMail() {
        return mail;
    }

    public String getPswd() {
        return pswd;
    }
    
    public Data getData(){
        return data;
    }
    
    public boolean isAdmin(){
        return isAdmin;
    }
    
    public Statement getStatement(){
        return st;
    }
    
    public boolean signUp(String mail, String pswd, String firstname, String surname) throws Exception{ //false si un compte avec le mm mail existe déjà
        ResultSet rs = st.executeQuery("SELECT COUNT(*) AS EXIST FROM USERS WHERE MAIL_USER = \"" + mail +"\"");
        rs.next();
        if (rs.getInt("EXIST") == 1) return false;
        st.executeUpdate("INSERT INTO USERS (SURNAME, FIRSTNAME, MAIL_USER, PASSWORD) VALUES (\"" +surname + "\", \"" +firstname + "\", \"" +mail + "\", \"" +pswd + "\")"); //pas de points virgule à la fin de la requête
        return true; //false si mail déjà existant
    }
    
    public void setAllWeeks() throws SQLException{
        System.out.println("setAllWeeks");
        Integer i = new Integer(1);
        ResultSet rs = st.executeQuery("SELECT END_DATE FROM WEEKS WHERE W_NUM=51");
        rs.next();
        Integer years = Integer.valueOf(rs.getString("END_DATE").substring(0, 4));
        Integer month = Integer.valueOf(rs.getString("END_DATE").substring(5, 7));
        Integer days = Integer.valueOf(rs.getString("END_DATE").substring(8, 10));
        if(month.equals(0)) month = 1;
        if(days.equals(0)) days = 1;
        LocalDate lastWeek = LocalDate.of(years, month, days);
        System.out.println(lastWeek.toString());
        if(LocalDate.now().isAfter(lastWeek)){
            LocalDate dateDeb = LocalDate.now().withMonth(9).with(firstInMonth(DayOfWeek.MONDAY));
            LocalDate dateFin = dateDeb.plusDays(7);
            for(i = 36 ; i !=52 ; i++){
                st.executeUpdate("UPDATE WEEKS SET BEGIN_DATE = '" + dateDeb.toString() + "' WHERE W_NUM = " + i.toString());
                st.executeUpdate("UPDATE WEEKS SET END_DATE = '" + dateFin.toString() + "' WHERE W_NUM = " + i.toString());
                dateDeb = dateDeb.plusDays(7);
                dateFin = dateFin.plusDays(7);
            }
            for(i = 1 ; i !=36 ; i++){
                st.executeUpdate("UPDATE WEEKS SET BEGIN_DATE = '" + dateDeb.toString() + "' WHERE W_NUM = " + i.toString());
                st.executeUpdate("UPDATE WEEKS SET END_DATE = '" + dateFin.toString() + "' WHERE W_NUM = " + i.toString());
                dateDeb = dateDeb.plusDays(7);
                dateFin = dateFin.plusDays(7);
            }
            st.executeUpdate("DELETE FROM WEEKS WHERE W_NUM = 1 OR W_NUM = 9 OR W_NUM = 10 OR W_NUM = 17 OR W_NUM = 18 OR W_NUM = 27 OR W_NUM = 28 OR W_NUM = 29 OR W_NUM = 30 OR W_NUM = 31 OR W_NUM = 32 OR W_NUM = 33 OR W_NUM = 34 OR W_NUM = 35 OR W_NUM = 43 OR W_NUM = 44;");
        }
    }
    
    public String getUserNameAndSurnameFromDatabase() throws SQLException{
        ResultSet rs = st.executeQuery("SELECT SURNAME , FIRSTNAME FROM USERS WHERE MAIL_USER = \"" + mail + "\"");
        rs.next();
        firstname = rs.getString("FIRSTNAME");
        surname = rs.getString("SURNAME");
        return (firstname + " " + surname);
    }
    
    public void readRemindMe() throws IOException{
        InputStream flux = new FileInputStream("Data\\credentials"); 
        InputStreamReader lecture= new InputStreamReader(flux);
        BufferedReader buff = new BufferedReader(lecture);
        mail = buff.readLine();
        pswd = buff.readLine();
        buff.close();
        System.out.println("Detected credentials : mail = " + mail + " | pswd = " + pswd);
    }
    
    public void removeRemindMe(){
        credentials.delete();
    }
    
    public boolean signIn(String mail , String pswd) throws Exception{
        ResultSet rs = st.executeQuery("SELECT COUNT(*) AS EXIST FROM USERS WHERE MAIL_USER = \"" + mail + "\" AND PASSWORD = \"" + pswd +"\"");
        rs.next();
        if (rs.getInt("EXIST") == 1){
            System.out.println("Bons identifiants");
            rs = st.executeQuery("SELECT * FROM USERS WHERE MAIL_USER = \"" + mail + "\"");
            rs.next();
            System.out.println(rs.getString("SURNAME"));
            userId = rs.getInt("ID_USER");
            surname = rs.getString("SURNAME");
            firstname = rs.getString("FIRSTNAME");
            this.mail = mail;
            if(rs.getString("RANK_USER").equals("TEACHER")) isAdmin = false;
            else isAdmin = true;
            data = new Data(st , userId);
            return true;
        }else{
            System.out.println("Mauvais identifiants");
            return false;
        }
    }
    
    public void forgotPass() throws SQLException, MessagingException{
        String  allCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789&@/*-+.)(_-"; //la chaine fait 73 caracteres
        String newPswd = "";
        SendMail lostPasswdMail;
        for(int i = 0; i < 50; i++) newPswd += allCharacters.charAt((int)Math.floor(Math.random() * 73)); //le mot de passe en fera 50
        lostPasswdMail = new SendMail(mail);
        lostPasswdMail.sendMail(newPswd);
        st.executeUpdate("UPDATE USERS SET PASSWORD = \"" + newPswd + "\" WHERE MAIL_USER = \"" + mail + "\"");
    }
    
    public boolean checkRemindMe(){ //true si la personne avait coché "se souvenir de moi"
        return credentials.exists();
    }
    
    public boolean createRemindMe(String mail , String pswd) throws FileNotFoundException, IOException{
        if(checkRemindMe()) return false;
        else{
            credentials.createNewFile();
            try (PrintWriter writer = new PrintWriter(credentials)) {
                writer.println(mail);
                writer.println(pswd);
            } catch(FileNotFoundException e) {
                System.out.println(e.getMessage());
                return false;
            }
            return true;
        }
    }
    
    public void setAccountPassword(String pswd) throws SQLException{
        this.pswd = pswd;
        st.executeUpdate("UPDATE USERS SET PASSWORD = \"" + pswd + "\" WHERE MAIL_USER = \"" + mail+"\"");
    }
    
    public void setMail(String mail) throws SQLException{
        this.mail = mail;
    }
    
}
