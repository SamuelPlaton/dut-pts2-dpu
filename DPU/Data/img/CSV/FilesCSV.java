package CSV;


import Features.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.sql.SQLException;
import Tools.MySqlConn;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pti-c
 */
public class FilesCSV {
    private final Path fichier;
    private Statement st;
    User user;
    
    
    public FilesCSV(User user) throws IOException, Exception, SQLException{
        this.user = user;
        fichier = Paths.get(user.getSurname() + "-" + user.getFirstname()+".csv");
        Files.write(fichier, String.format("semaine;debut-fin;creneau%n").getBytes());
        //---------------------------------------------------------------------------------------------------------------------
        
        ArrayList<String> weeks = new ArrayList<>();
        ArrayList<Date> timeBadFormat = new ArrayList<>();
        ArrayList<String> horaire = new ArrayList<>();
        ArrayList<Integer> rank = new ArrayList<>();
        ArrayList<String> contraints = new ArrayList<>();
        st = user.getStatement();
        String tranche = "L11 L12 L21 L22 Ma11 Ma12 Ma21 Ma22 Me11 Me12 Me21 Me22 J11 J12 V11 V12 V21 V22";
        ResultSet rs = st.executeQuery("SELECT * FROM WEEKS WHERE CONSTRUCT <> 'H'");
        
        
        while(rs.next()){ 
            timeBadFormat.add(rs.getDate("BEGIN_DATE"));
            String badFormat = rs.getString("BEGIN_DATE") +"-"+ rs.getString("END_DATE");
            String goodFormat="";
            for(int i =0; i < badFormat.length(); i++){
                if(i != 10 && badFormat.charAt(i)=='-'){
                    goodFormat = goodFormat + "/";
                }
                else{
                    goodFormat = goodFormat + badFormat.charAt(i);
                }
            }
            weeks.add(rs.getString("CONSTRUCT") +""+ rs.getString("W_NUM") +";"+ goodFormat);
        }
        
        
        rs = st.executeQuery("SELECT BEGIN_HOUR, END_HOUR, OCCURENCE, TYPE, DATE_CONS FROM CONSTRAINTS WHERE DATE_CONS >= NOW() AND ID_USER IN(SELECT ID_USER FROM USERS WHERE MAIL_USER=\""+user.getMail() + "\"AND ID_USER = ID_USER)");
        
        while(rs.next()){
            String horaireChiffre[] = {"L11 ", "L12 ", "L21 ", "L22 ", "Ma11 ", "Ma12 ", "Ma21 ", "Ma22 ", "Me11 ", "Me12 ", "Me21 ", "Me22 " , "J11 ", "J12 ", "V11 ", "V12 ", "V21 ", "V22 "};//0->17
            int timer = 0;
            Date contrainteBegin = rs.getDate("DATE_CONS");
            for(Date x : timeBadFormat){
                if(contrainteBegin.after(x) || contrainteBegin.equals(x)){timer++;}
            }
            
           LocalDate semaine = timeBadFormat.get(timer).toLocalDate();
           LocalDate contrainte = contrainteBegin.toLocalDate();
          
           
           
           
           String jour = contrainte.getDayOfWeek().toString();
           switch(jour){
               case "MONDAY":
                   if(rs.getString("TYPE") == "PREF"){
                       switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[0] = "L+11 ";
                               break;
                           case "12":
                               horaireChiffre[1] = "L+12 ";
                               break;
                           case "21":
                               horaireChiffre[2] = "L+21 ";
                               break;
                           case "22":
                               horaireChiffre[3] = "L+22 ";
                               break;
                       }
                   }
                   else if(rs.getString("TYPE") == "STRICT"){
                                              switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[0] = "    ";
                               break;
                           case "12":
                               horaireChiffre[1] = "    ";
                               break;
                           case "21":
                               horaireChiffre[2] = "    ";
                               break;
                           case "22":
                               horaireChiffre[3] = "    ";
                               break;
                       }
                       
                   }
                   else{
                       switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[0] = "l11 ";
                               break;
                           case "12":
                               horaireChiffre[1] = "l12 ";
                               break;
                           case "21":
                               horaireChiffre[2] = "l21 ";
                               break;
                           case "22":
                               horaireChiffre[3] = "l22 ";
                               break;
                       }
                   }
                   break;
               case "TUESDAY":
                   if(rs.getString("TYPE") == "PREF"){
                       switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[4] = "Ma+11 ";
                               break;
                           case "12":
                               horaireChiffre[5] = "Ma+12 ";
                               break;
                           case "21":
                               horaireChiffre[6] = "Ma+21 ";
                               break;
                           case "22":
                               horaireChiffre[7] = "Ma+22 ";
                               break;
                       }
                   }
                   else if(rs.getString("TYPE") == "STRICT"){
                                              switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[4] = "    ";
                               break;
                           case "12":
                               horaireChiffre[5] = "    ";
                               break;
                           case "21":
                               horaireChiffre[6] = "    ";
                               break;
                           case "22":
                               horaireChiffre[7] = "    ";
                               break;
                       }
                       
                   }
                   else{
                       switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[4] = "ma11 ";
                               break;
                           case "12":
                               horaireChiffre[5] = "ma12 ";
                               break;
                           case "21":
                               horaireChiffre[6] = "ma21 ";
                               break;
                           case "22":
                               horaireChiffre[7] = "ma22 ";
                               break;
                       }
                   }
                   break;
               case "WEDNESDAY":
                   if(rs.getString("TYPE") == "PREF"){
                       switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[8] = "Me+11 ";
                               break;
                           case "12":
                               horaireChiffre[9] = "Me+12 ";
                               break;
                           case "21":
                               horaireChiffre[10] = "Me+21 ";
                               break;
                           case "22":
                               horaireChiffre[11] = "Me+22 ";
                               break;
                       }
                   }
                   else if(rs.getString("TYPE") == "STRICT"){
                                              switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[8] = "    ";
                               break;
                           case "12":
                               horaireChiffre[9] = "    ";
                               break;
                           case "21":
                               horaireChiffre[10] = "    ";
                               break;
                           case "22":
                               horaireChiffre[11] = "    ";
                               break;
                       }
                       
                   }
                   else{
                       switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[8] = "me11 ";
                               break;
                           case "12":
                               horaireChiffre[9] = "me12 ";
                               break;
                           case "21":
                               horaireChiffre[10] = "me21 ";
                               break;
                           case "22":
                               horaireChiffre[11] = "me22 ";
                               break;
                       }
                   }
                   break;
               case "THURSDAY":
                   if(rs.getString("TYPE") == "PREF"){
                       switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[12] = "J+11 ";
                               break;
                           case "12":
                               horaireChiffre[13] = "J+12 ";
                               break;
                       }
                   }
                   else if(rs.getString("TYPE") == "STRICT"){
                                              switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[12] = "    ";
                               break;
                           case "12":
                               horaireChiffre[13] = "    ";
                               break;
                       }
                       
                   }
                   else{
                       switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[12] = "j11 ";
                               break;
                           case "12":
                               horaireChiffre[13] = "j12 ";
                               break;
                       }
                   }
                   break;
               case "FRIDAY":
                   if(rs.getString("TYPE") == "PREF"){
                       switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[14] = "V+11 ";
                               break;
                           case "12":
                               horaireChiffre[15] = "V+12 ";
                               break;
                           case "21":
                               horaireChiffre[16] = "V+21 ";
                               break;
                           case "22":
                               horaireChiffre[17] = "V+22 ";
                               break;
                       }
                   }
                   else if(rs.getString("TYPE") == "STRICT"){
                                              switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[14] = "    ";
                               break;
                           case "12":
                               horaireChiffre[15] = "    ";
                               break;
                           case "21":
                               horaireChiffre[16] = "    ";
                               break;
                           case "22":
                               horaireChiffre[17] = "    ";
                               break;
                       }
                       
                   }
                   else{
                       switch(rs.getString("BEGIN_HOUR")){
                           case "11":
                               horaireChiffre[14] = "v11 ";
                               break;
                           case "12":
                               horaireChiffre[15] = "v12 ";
                               break;
                           case "21":
                               horaireChiffre[16] = "v21 ";
                               break;
                           case "22":
                               horaireChiffre[17] = "v22 ";
                               break;
                       }
                   }
                   break;
           }
           
           rank.add(timer);
           
           String all = "";
           for(int i = 0; i < 17; i++){all = all + horaireChiffre[i];}
           contraints.add(weeks.get(timer-1) + ";" + all);
           System.out.println(rs.getString("BEGIN_HOUR") + " " + rs.getString("END_HOUR") + " " + rs.getString("OCCURENCE") + " " + rs.getString("TYPE") + rs.getString("DATE_CONS"));
        }
        
        
        
        int rechercheDate = 0;
        String valeur = "";
        for (String temp : weeks) {
            boolean way = false;
            int rechercheRang = 0;
            for(int tempInt : rank){
                if(tempInt == rechercheDate){
                   way = true;
                   valeur = contraints.get(rechercheRang);
                   System.out.println(contraints.get(rechercheRang));
                   rechercheRang++;
                }
                rechercheDate++;
            }
            if(way){          
                way = false;
                 Files.write(fichier, String.format(valeur + "%n").getBytes(), APPEND);
            }
            else{
                 Files.write(fichier, String.format(temp + ";" + tranche +"%n").getBytes(), APPEND);
            }
            rechercheRang++;
        }   
        
    }
    
}
        
    