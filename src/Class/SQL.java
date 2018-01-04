package Class;

import com.google.common.io.Files;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

public class SQL {
    static Connection conn;
    static boolean firstRun;
    static MainController mc = new MainController();
    
    public SQL(){}
    
    public SQL(String dbName) throws SQLException{
        try {
            String url = null;
            String user = null;
            String pass = null;
            List <String> list = Files.readLines(new File("./Settings.txt"), StandardCharsets.UTF_8);
            Iterator<String> iterator = list.iterator();
            int i = 0;
            while (iterator.hasNext()){
                i++;
                String ele = iterator.next();
                switch (i) {
                    case 8:
                        firstRun = ele.replaceFirst("(.*)=", "").equals("Y");
                        break;
                    case 10:
                        url = ele.replaceFirst("(.*)=", "");
                        break;
                    case 11:
                        user = ele.replaceFirst("(.*)=", "");
                        break;
                    case 12:
                        pass = ele.replaceFirst("(.*)=", "");
                        break;
                }
            }
            if (!firstRun){
                conn = DriverManager.getConnection("jdbc:mysql://" + url + dbName, user, pass);
            } else {
                Desktop.getDesktop().open(new File("./Settings.txt"));
                System.exit(0);
            }
        } catch (IOException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String login(String user, String pass){
        String success = "false";
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT name, pass_hash, permissions FROM individual WHERE permissions != 0;");
            while (rs.next()) {
                if (rs.getString(1).equals(user)){
                    if (rs.getString(2).equals(pass)){   
                        success = "match"+rs.getString(3);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
                System.out.println("\n\n SQL DISCONNECTED \n\n");
            }
        }
        return success;
    }
    
    public static void close(){
        try {
            conn.close();
            Platform.exit(); //Exit application
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            Platform.exit();
        }
    }
    
    public static String[][] houseRank(){
        String[][] rank = new String[4][3];
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT name, points, color FROM house ORDER BY points DESC;");
            int c = 0;
            while (rs.next()) {
                rank[c][0] = rs.getString(1);
                rank[c][1] = rs.getString(2);
                rank[c][2] = rs.getString(3);
                c++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
        return rank;
    }
    
    public static ResultSet eventList() {
        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT id, name, grades, datetime FROM events ORDER BY datetime DESC;");
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
        return rs;
    }
    
    public static ResultSet personList(String house) {
        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT id, name, grade, section, points FROM individual WHERE house = '"+ house +"' ORDER BY is_student ASC, points DESC;");
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
        return rs;
    }
    
    public static ResultSet participantList(){
        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT id, name, house FROM individual WHERE is_student = 1 ORDER BY name;");
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
        return rs;
    }
    
    public static ResultSet participantSelection(int id){
        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT participant FROM participant WHERE event = "+ id +";");
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
        return rs;
    }
    
    public static void addParticipants(int event, int participant[]){
        try {
            conn.createStatement().executeUpdate("DELETE FROM participant WHERE event = "+event+";");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO participant (event, participant) VALUES ("+event+", ?);");
            for (int element : participant){
                stmt.setInt(1, element);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
    }
    
//    EVENTS    
    private static int houseInt(String house){
        int hInt = 0;
        switch (house){
            case "Air":
                hInt = 1;
                break;
            case "Earth":
                hInt = 2;
                break;
            case "Fire":
                hInt = 3;
                break;
            case "Water":
                hInt = 4;
                break;
        }
        return hInt;
    }
    
    public static void addEvent(String name, String datetime, String venue, int grades, int points1, int points2, int points3, int points4, String win1, String win2, String win3, String win4, String description){
        int winner1 = 0, winner2 = 0, winner3 = 0, winner4 = 0;
        if (win1!=null)
            winner1 = houseInt(win1);
        if (win2!=null)
            winner2 = houseInt(win2);
        if (win3!=null)
            winner3 = houseInt(win3);
        if (win4!=null)
            winner4 = houseInt(win4);
        try {
            conn.createStatement().executeUpdate("INSERT INTO events (name, datetime, venue, grades, points1, points2, points3, points4, win1, win2, win3, win4, description) VALUES ('"+ name +"','"+ datetime +"','"+ venue +"',"+ grades +","+ points1 +","+ points2 +","+ points3 +","+ points4 +","+ winner1 +","+ winner2 +","+ winner3 +","+ winner4 +",'"+ description +"');");
            if (!(winner1==0||winner2==0||winner3==0||winner4==0)){
                ResultSet rs = conn.createStatement().executeQuery("SELECT id FROM events WHERE name = '"+ name +"' AND datetime = '"+ datetime +"';");
                rs.first();
                updatePoints(rs.getInt(1), winner1, points1, winner2, points2, winner3, points3, winner4, points4);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
    }
    
    public static void updateEvent(int id, String name, String datetime, String venue, int grades, int points1, int points2, int points3, int points4, String win1, String win2, String win3, String win4, String description){
        int winner1 = 0, winner2 = 0, winner3 = 0, winner4 = 0;
        if (win1!=null)
            winner1 = houseInt(win1);
        if (win2!=null)
            winner2 = houseInt(win2);
        if (win3!=null)
            winner3 = houseInt(win3);
        if (win4!=null)
            winner4 = houseInt(win4);
        try {
            if (!(winner1==0||winner2==0||winner3==0||winner4==0))
                updatePoints(id, winner1, points1, winner2, points2, winner3, points3, winner4, points4);
            conn.createStatement().executeUpdate("UPDATE events SET name = '"+ name +"',datetime = '"+ datetime +"',venue = '"+ venue +"',grades = "+ grades +",points1 = "+ points1 +",points2 = "+ points2 +",points3 = "+ points3 +",points4 = "+ points4 +",win1 = '"+ winner1 +"',win2 = '"+ winner2 +"',win3 = '"+ winner3 +"',win4 = '"+ winner4 +"',description = '"+ description +"' WHERE id = "+ id +";");
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
    }
    
    public static void deleteEvent(int id){
        try {
            conn.createStatement().executeUpdate("DELETE FROM events WHERE id = "+ id +";");
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
    }
    
    public static ResultSet loadEvent(int id){
        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT name, datetime, venue, grades, points1, points2, points3, points4, win1, win2, win3, description FROM events WHERE id = "+ id +";");
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
        return rs;
    }
    
    private static void updatePoints(int EvID, int win1, int point1, int win2, int point2, int win3, int point3, int win4, int point4){
        try {
            //House
            ResultSet house = conn.createStatement().executeQuery("SELECT id, points FROM house;");
            while (house.next()){
                if (house.getInt(1) == win1){
                    int val = house.getInt(1)+point1;
                    conn.createStatement().executeUpdate("UPDATE house SET points = "+ val +" WHERE id = "+ win1 +";");
                }
                if (house.getInt(1) == win2){
                    int val = house.getInt(1)+point2;
                    conn.createStatement().executeUpdate("UPDATE house SET points = "+ val +" WHERE id = "+ win2 +";");
                }
                if (house.getInt(1) == win3){
                    int val = house.getInt(1)+point3;
                    conn.createStatement().executeUpdate("UPDATE house SET points = "+ val +" WHERE id = "+ win3 +";");
                }
                if (house.getInt(1) == win4){
                    int val = house.getInt(1)+point4;
                    conn.createStatement().executeUpdate("UPDATE house SET points = "+ val +" WHERE id = "+ win4 +";");
                }
            }
            
            //Participants
            ResultSet participants = participantSelection(EvID);
            while (participants.next()){
                ResultSet person = conn.createStatement().executeQuery("SELECT points, house FROM individual WHERE id = "+ participants.getInt(1) +";");
                person.next();
                int houseNum = houseInt(person.getString(2));
                int val = 0;
                if (houseNum == win1){
                    val = person.getInt(1)+point1;
                }
                if (houseNum == win2){
                    val = person.getInt(1)+point2;
                }
                if (houseNum == win3){
                    val = person.getInt(1)+point3;
                }
                if (houseNum == win4){
                    val = person.getInt(1)+point4;
                }
                conn.createStatement().executeUpdate("UPDATE individual SET points = "+ val +" WHERE id = "+ participants.getInt(1) +";");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
    }
    
    public static ResultSet loadParticipants(int id){
        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT * FROM participant WHERE event = "+ id +";");
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
        return rs;
    }
    
//    PERSON
    
    public static void addPerson(String name, String designation, String house, boolean isFaculty, int grade, char section, int program, int permissions, String pass){
        try {
            conn.createStatement().executeUpdate("INSERT INTO individual (name, designation, house, is_student, grade, section, program, permissions, pass_hash) VALUES ('"+ name +"','"+ designation +"','"+ house +"',"+ (isFaculty ? 0 : 1) +","+ grade +",'"+ section +"',"+ program +","+ permissions +",'"+ pass +"');");
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
    }
    
    public static void updatePerson(int id, String name, String designation, String house, boolean isFaculty, int grade, char section, int program, int permissions, String pass){
        try {
            conn.createStatement().executeUpdate("UPDATE individual SET name = '"+ name +"',designation = '"+ designation +"',house = '"+ house +"',is_student = "+ (isFaculty ? 0 : 1) +",grade = "+ grade +",section = '"+ section +"',program = "+ program +",permissions = "+ permissions +",pass_hash = '"+ pass +"' WHERE id = "+ id +";");
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
    }
    
    public static void deletePerson(int id){
        try {
            conn.createStatement().executeUpdate("DELETE FROM individual WHERE id = "+ id +";");
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
    }
    
    public static ResultSet loadPerson(int id){
        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT name, designation, house, is_student, grade, section, program, permissions, points FROM individual WHERE id = "+ id +";");
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("Communications link failure")){
                mc.sqlDisconnect();
            }
        }
        return rs;
    }
    
}