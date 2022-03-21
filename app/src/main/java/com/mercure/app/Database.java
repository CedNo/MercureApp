package com.mercure.app;

import android.util.Log;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private static Database dbIsntance;
    private static Connection con;
    private static Statement stmt = null;
    ResultSet rs = null;


    private Database() {
        // private constructor //
    }

    public static Database getInstance() {
        if (dbIsntance == null) {
            dbIsntance = new Database();
        }
        return dbIsntance;
    }

    public Connection getConnection() {

        if (con == null) {
            try {
                String host = "jdbc:mysql://184.162.255.173:23306/mercure";
                String username = "mercure";
                String password = "P4AarR!c?D";
                con = DriverManager.getConnection(host, username, password);

            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return con;


    }

    public Boolean getUtilisateur(String utilisateur) throws SQLException {
        String query = "select username from users where";
        rs = stmt.executeQuery(query);

        return true;
    }






}