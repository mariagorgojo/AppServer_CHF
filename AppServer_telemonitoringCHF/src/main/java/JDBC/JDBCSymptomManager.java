/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import ifaces.SymptomManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Disease;
import pojos.Surgery;
import pojos.Symptom;

/**
 *
 * @author carmengarciaprieto
 */
public class JDBCSymptomManager implements SymptomManager {

    private Connection c;

    public JDBCSymptomManager(Connection c) {
        this.c = c;

    }

    @Override
    public void insertSymptom(String symptom) {

        try {
            String sql = "INSERT OR IGNORE INTO Symptom (symptom) VALUES (?)";
            try ( PreparedStatement p = c.prepareStatement(sql)) {
                p.setString(1, symptom);
                p.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Database exception.");
            e.printStackTrace();
        }

    }

    @Override
    public void assignSymptomToEpisode(int symptom_id, int episode_id) {
        try {
            String sql = "INSERT INTO Episode_Symptom (symptom_id, episode_id) VALUES (?,?)";
            PreparedStatement p = c.prepareStatement(sql);
            p.setInt(1, symptom_id);
            p.setInt(2, episode_id);
            p.executeUpdate();
            p.close();
        } catch (SQLException e) {
            System.out.println("Database error.");
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Symptom> getSymptomsByEpisode(int episode_id, int patient_id) {
        ArrayList<Symptom> list = new ArrayList<>();
        //nuevo
        try {
            if (c == null || c.isClosed()) {
                System.err.println("Database connection is not available.");
                return list;
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCSymptomManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String sql = "SELECT Symptom.id, Symptom.symptom FROM Symptom "
                + "JOIN Episode_Symptom ON Symptom.id = Episode_Symptom.symptom_id "
                + //"WHERE Episode_Symptom.episode_id = ?";
                "JOIN Episode ON Episode_Symptom.episode_id = Episode.id "
                + "WHERE Episode.id = ? AND Episode.patient_id = ?";
        try ( PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, episode_id);
            // nuevo
             p.setInt(2, patient_id);
            try ( ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Symptom s = new Symptom(rs.getInt("id"), rs.getString("symptom"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving symptoms for episode ID: " + episode_id);
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ArrayList<Symptom> getSymptomsByPatient(String patient_id) {
        ArrayList<Symptom> list = new ArrayList<>();
        String sql = "SELECT Symptom.id, Symptom.symptom FROM Symptom "
                + "JOIN Episode_Symptom ON Symptom.id = Episode_Symptom.symptom_id "
                + "JOIN Episode ON Episode_Symptom.episode_id = Episode.id "
                + "WHERE Episode.patient_id = ?";
        try ( PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, patient_id);
            try ( ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Symptom s = new Symptom(rs.getInt("id"), rs.getString("symptom"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error");
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String getSymptomById(int symptom_id) {
        String sql = "SELECT symptom FROM Symptom WHERE id = ?";
        try ( PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, symptom_id);
            try ( ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("symptom");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error");
            e.printStackTrace();
        }
        return null; // Devuelve null si no se encuentra                        
    }

    @Override
    public ArrayList<Symptom> getAllSymptoms() {
        ArrayList<Symptom> symptoms = new ArrayList<>();
        int count=0;
        String sql = "SELECT id, symptom FROM Symptom";
        try ( Statement stmt = c.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()&&count<10) {
                Symptom symptom = new Symptom(rs.getInt("id"), rs.getString("symptom"));
                symptoms.add(symptom);
                count++;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving symptoms: " + e.getMessage());
            e.printStackTrace();
        }
        return symptoms;
    }

    @Override
    public int getSymptomId(String symptom) {
        String sql = "SELECT id FROM Symptom WHERE symptom LIKE ?";
        try ( PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, symptom);
            try ( ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while retrieving symptom ID");
            e.printStackTrace();
        }
        return -1; // Devuelve -1 si no se encuentra

    }

    //nuevo
    @Override
    public boolean isSymptomAssociatedWithEpisode(int symptomId, int episodeId) {

        String sql = "SELECT COUNT(*) FROM Episode_Symptom WHERE symptom_id = ? AND episode_id = ?";
        try ( PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, symptomId);
            p.setInt(2, episodeId);
            try ( ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Devuelve true si ya está asociado
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

}
