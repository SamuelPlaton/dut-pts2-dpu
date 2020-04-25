/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.sql.*;

/**
 *
 * @author alfred
 */
public class MySqlConn {
    private boolean validConn = true;
    private Statement sqlStatement = null;
    private Connection conn;
    
    public MySqlConn() throws Exception{
        try {

            // create a connection to the database
            conn = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7296152", "sql7296152", "YUH4TZqAZI"); //url , user , passwd
            sqlStatement = conn.createStatement();
            
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            validConn = false;
        }
    }
    
    public Statement getStatement(){
        return sqlStatement;
    }
    
    public void closeConn() throws SQLException{
        conn.close();
    }
    
    public boolean isValid(){
        return validConn;
    }
            
}