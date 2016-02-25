package com.abhinav.dylan.umainetrade;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import BCrypt.BCrypt;

/**
 * Created by abhinav on 11/17/15.
 */
public class DBLogin {
    private String emailDB;
    private String passwordDB;

    private Connection connection;

    private boolean verifiedEmail;
    private int validToken;
    private boolean hashPasswordMatch;


    public String getEmailDB() {
        return emailDB;
    }

    public void setEmailDB(String emailDB) {
        this.emailDB = emailDB;
    }

    public String getPasswordDB() {
        return passwordDB;
    }

    public void setPasswordDB(String passwordDB) {
        this.passwordDB = passwordDB;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    public boolean isVerifiedEmail() {
        return verifiedEmail;
    }

    public void setVerifiedEmail(boolean verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }


    public int getValidToken() {
        return validToken;
    }

    public void setValidToken(int validToken) {
        this.validToken = validToken;
    }

    public boolean isHashPasswordMatch() {
        return hashPasswordMatch;
    }

    public void setHashPasswordMatch(boolean hashPasswordMatch) {
        this.hashPasswordMatch = hashPasswordMatch;
    }


    //Constructor to initialize JDBC driver and establish connection
    DBLogin(){

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {


            Toast.makeText(LoginActivity.context, "where is your driver?", Toast.LENGTH_SHORT).show();

            e.printStackTrace();


        }

        setConnection(null);
        try {

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://10.0.3.2:5432/UMaineTrade", "abhinav",
                    "san123");

        } catch (SQLException e) {

            Toast.makeText(LoginActivity.context, "Connection failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();


        }

    }

    /*
    Method to RegexValidator the email and password provided in the LoginActivity page.
     */

    public boolean authenticate(String email, String password) {

        if (connection != null) {
            String hashedPassword = null;
                    ResultSet rs;
                    Statement stmt = null;
                    setVerifiedEmail(false);

                     /*
                    Query database to get count of valid login and if email is validated by using count(email)
                     */
                    try {
                        stmt = connection.createStatement();
                        String authenticateQuery = "select count(email), validated from users where email ='"+email+"' group by validated";


                        rs = stmt.executeQuery(authenticateQuery);
                        while (rs.next()) {

                                //If validated column in database is false, set local variable isverified as false. Use setters accordingly.
                                //If count returned is 0, then invalid user. If count returned is 1, then there exists a user.
                                int getCount = rs.getInt("count");
                                boolean isverified = rs.getBoolean(2);
                                setVerifiedEmail(isverified);
                                setValidToken(getCount);


                        }

                        //Query database to check email and password combination
                        String emailPasswordQuery = "select password from users where email ='"+email+"'";
                        rs = stmt.executeQuery(emailPasswordQuery);

                        //Retrieve hashedPassword and check if it matches with plain text password provided.
                        while (rs.next()) {
                            hashedPassword = rs.getString("password");


                        }
                    try {
                        setHashPasswordMatch(hashPasswordMatch = BCrypt.checkpw(password, hashedPassword) ? true : false);
                    }
                    catch(NullPointerException e){
                        Toast.makeText(LoginActivity.context, "Invalid Username/Password", Toast.LENGTH_SHORT).show();

                    }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(LoginActivity.context, "Failed to make connection", Toast.LENGTH_SHORT).show();
                }


        //Only return true if: username and password matches and email is verified.
        if(getValidToken() == 1 &&isVerifiedEmail() && isHashPasswordMatch()){
            AddListings.ownerId = generateUserId(email);
            return true;


        }
        else{
            return false;
        }


    }
    /*
        Method to check if the user is registered on the app or not, by checking if database contains the email provided.
         */

    public boolean registeredEmail(String email){
        boolean verified = false;
        if (connection != null) {
            ResultSet rs;
            Statement stmt = null;
            try{
                stmt = connection.createStatement();
                rs = stmt.executeQuery("SELECT count(email) from users where email ='"+email+"'");
                while (rs.next()){
                    verified = true;
                }

            }
            catch(SQLException e){
                e.printStackTrace();

            }

        }

        if(verified){
            return true;
        }
        else {
            return false;
        }
    }

    /*
            Method to insert user info into the database.
             */
    public void signUp(String firstname, String lastname, String email, String password){

        if (connection != null) {
            ResultSet rs;
            //Toast.makeText(getApplicationContext(), "You made it", Toast.LENGTH_SHORT).show();
            Statement stmt = null;
            try {
                stmt = connection.createStatement();
                String authenticateQuery = "insert into users (firstname, lastname, email, password, validated) values ('"+firstname+ "','" + lastname+"','" + email+"','" + password+"','" + false+"')";
                rs = stmt.executeQuery(authenticateQuery);
               //connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }




        } else {
            Toast.makeText(LoginActivity.context, "Failed to make connection", Toast.LENGTH_SHORT).show();
        }


    }

    public int generateUserId(String email){
        int userId = 0;
        if (connection != null) {
            ResultSet rs;
            //Toast.makeText(getApplicationContext(), "You made it", Toast.LENGTH_SHORT).show();
            Statement stmt = null;

            try {
                stmt = connection.createStatement();
                String authenticateQuery = "select id from users where email = '"+email+"'";
                rs = stmt.executeQuery(authenticateQuery);
                //connection.close();
                while(rs.next()) {
                    userId = rs.getInt("id");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }




        } else {
            Toast.makeText(LoginActivity.context, "Failed to make connection", Toast.LENGTH_SHORT).show();
        }


        return userId;
    }

    public void addListing(String itemName, int itemPrice, int conditionId, int photoId, int categoryId, int ownerId, String description){
        if (connection != null) {
            ResultSet rs;
            //Toast.makeText(getApplicationContext(), "You made it", Toast.LENGTH_SHORT).show();
            Statement stmt = null;
            try {
                stmt = connection.createStatement();
                //String authenticateQuery = "insert into items (name, price, conditionid, photoid, categoryid, ownerid, description) values ('"+itemName+ "','" + itemPrice+"','" + conditionId+"','"+ photoId+"','" + + categoryId+"','" + description+"')";
                String query = "insert into items (name, price, conditionid, photoid, categoryid, ownerid, description) values ('"+itemName+"', '"+itemPrice+"', '"+conditionId+"', '"+photoId+"', '"+categoryId+"', '"+ownerId+"', '"+description+"')";

                rs = stmt.executeQuery(query);
                //connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }




        } else {
            Toast.makeText(LoginActivity.context, "Failed to make connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void insertImage(File file, FileInputStream fis){
        if (connection != null) {
            ResultSet rs;
            //Toast.makeText(getApplicationContext(), "You made it", Toast.LENGTH_SHORT).show();
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO images VALUES (?, ?)");
                ps.setString(1, file.getName());
                ps.setBinaryStream(2, fis, (int)file.length());
                ps.executeUpdate();
                ps.close();
                fis.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(LoginActivity.context, "Failed to make connection", Toast.LENGTH_SHORT).show();
        }

    }

    public byte[] getImage(String filePath){
        byte[] imgBytes = new byte[1];
        if (connection != null) {
            ResultSet rs;

            //Toast.makeText(getApplicationContext(), "You made it", Toast.LENGTH_SHORT).show();
            try {

                PreparedStatement ps = connection.prepareStatement("SELECT img FROM images WHERE imgname = ?");
                ps.setString(1, filePath);
                ResultSet res = ps.executeQuery();
                while (res.next()) {
                   imgBytes = res.getBytes(1);
                    // use the data in some way here

                }

                res.close();
                ps.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(LoginActivity.context, "Failed to make connection", Toast.LENGTH_SHORT).show();
        }

        return imgBytes;
    }

    public void viewListings(){

        if (connection != null) {
            ResultSet rs;
            //Toast.makeText(getApplicationContext(), "You made it", Toast.LENGTH_SHORT).show();
            Statement stmt = null;
            try {
                stmt = connection.createStatement();
                String query = "select i.name, i.price, con.condition, cat.category, p.photo, u.firstname, u.lastname, i.description from items i \n" +
                        "left join users u on u.id = i.ownerid\n" +
                        "left join condition con on con.id = i.conditionid\n" +
                        "left join category cat on cat.id = i.categoryid\n" +
                        "left join photo p on p.id = i.photoid";
                rs = stmt.executeQuery(query);
                //connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }




        } else {
            Toast.makeText(LoginActivity.context, "Failed to make connection", Toast.LENGTH_SHORT).show();
        }


    }



}
