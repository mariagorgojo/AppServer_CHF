/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author mariagorgojo
 */
public class ConnectionManager {

    private Connection c;

    public ConnectionManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:./db/telemedicine.db");
            c.createStatement().execute("PRAGMA foreign_keys=ON");
            System.out.println("Database connection opened.");
            createTables();

        } catch (Exception e) {
            System.out.println("Database access error");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return c;
    }

    public void closeConnection() {
        try {
            c.close();
        } catch (SQLException e) {
            System.out.println("Database error.");
            e.printStackTrace();
        }
    }

    private void createTables() {
        try {
            Statement s = c.createStatement();

            String table_Doctor = "CREATE TABLE Doctor ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " dni TEXT NOT NULL,"
                    + " name TEXT NOT NULL,"
                    + " surname TEXT NOT NULL,"
                    + " email TEXT NOT NULL,"
                    + " phone INTEGER NUT NULL);";

            s.executeUpdate(table_Doctor);

            String table_Patient = "CREATE TABLE Patient ("
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " dni TEXT NOT NULL,"
                    + " name TEXT NOT NULL,"
                    + " surname TEXT NOT NULL,"
                    + " dob DATE NOT NULL,"
                    + " email TEXT NOT NULL,"
                    + " phone INTEGER NOT NULL,"
                    + " gender TEXT NOT NULL,"
                    + " doctor_id INTEGER,"
                    + "episodes_id INTEGER,"
                    + " FOREIGN KEY (doctor_id) REFERENCES Doctor(id) ON DELETE SET NULL,"
                    + " FOREIGN KEY (episodes_id) REFERENCES Episodes(id) ON DELETE SET NULL);";
            s.executeUpdate(table_Patient);

            String table_Surgery = "CREATE TABLE Surgery ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "surgery TEXT NOT NULL);";
            s.executeUpdate(table_Surgery);

            String table_Symptom = "CREATE TABLE Symptom ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "symptom TEXT NOT NULL);";
            s.executeUpdate(table_Symptom);

            String table_Disease = "CREATE TABLE Disease ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "disease TEXT NOT NULL);";
            s.executeUpdate(table_Disease);

            String table_Episode_Surgery = "CREATE TABLE Episode_Surgery ("
                    + "episode_id INTEGER,"
                    + "surgery_id INTEGER,"
                    + "FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE SET NULL,"
                    + "FOREIGN KEY (surgery_id) REFERENCES Surgery(id) ON DELETE SET NULL,"
                    + "PRIMARY KEY (episode_id, surgery_id));";
            s.executeUpdate(table_Episode_Surgery);

            String table_Episode_Symptom = "CREATE TABLE Episode_Symptom ("
                    + "episode_id INTEGER,"
                    + "symptom_id INTEGER,"
                    + "FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE SET NULL,"
                    + "FOREIGN KEY (symptom_id) REFERENCES Symptom(id) ON DELETE SET NULL),"
                    + "PRIMARY KEY (episode_id, symptom_id));";
            s.executeUpdate(table_Episode_Symptom);

            String table_Episode_Disease = "CREATE TABLE Episode_Disease ("
                    + "episode_id INTEGER,"
                    + "disease_id INTEGER,"
                    + "FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE SET NULL,"
                    + "FOREIGN KEY (disease_id) REFERENCES Disease(id) ON DELETE SET NULL,"
                    + "PRIMARY KEY (episode_id, disease_id));";
            s.executeUpdate(table_Episode_Disease);
            s.close();

        

        String table_Episode = "CREATE TABLE Episode ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "patient_id INTEGER,"
                + "date DATE NOT NULL,"                
                + "FOREIGN KEY (patient_id) REFERENCES Patient(id) ON DELETE SET NULL);";
        s.executeUpdate(table_Episode);
        
        String table_Recording = "CREATE TABLE Recording ("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
        + "episode_id INTEGER,"
        + "date DATE NOT NULL,"
        + "duration INTEGER NOT NULL,"
        + "filepath TEXT NOT NULL"               
        + "FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE CASCADE);";
        s.executeUpdate(table_Recording);
    
        
        } catch (SQLException e) {
            if (e.getMessage().contains("already exist")) {
                return;
            }
            System.out.println("Database error.");
            e.printStackTrace();
        }
    }
    
}
