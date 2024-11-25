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
            Statement s = c.createStatement();
            String sql = "INSERT OR IGNORE INTO Surgery (surgery) VALUES ('" + symptom + "')";
            s.executeUpdate(sql);
            s.close();
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
    public ArrayList<Symptom> getSymptomsByEpisode(int episode_id) {
        ArrayList<Symptom> list = new ArrayList<Symptom>();
        try {
            String sql = "SELECT symptom_id FROM Episode_Symptom WHERE episode_id LIKE ?";
            PreparedStatement p = c.prepareStatement(sql);
            p.setInt(1, episode_id);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Symptom s = new Symptom(rs.getInt("id"), rs.getString("symptom"));
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println("database error");
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public ArrayList<Symptom> getSymptomsByPatient(String patient_id) {
        ArrayList<Symptom> list = new ArrayList<Symptom>();
        try {
            String sql = "SELECT Symptom.* FROM Symptom JOIN Episode_Symptom ON Symptom.id = Episode_Symptom.symptom_id JOIN Episode ON Episode_Symptom.episode_id = Episode.id WHERE Episode.patient_id = ?;";
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, patient_id);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Symptom s = new Symptom(rs.getInt("id"), rs.getString("symptom"));
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println("database error");
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public String getSymptomById(int symptom_id) {
        try {
            String sql = "SELECT symptom FROM Symptom WHERE id LIKE ?";
            PreparedStatement p = c.prepareStatement(sql);
            p.setInt(1, symptom_id);
            ResultSet rs = p.executeQuery();
            String name = rs.getString("symptom");
            rs.close();
            p.close();
            return name;
        } catch (SQLException e) {
            System.out.println("database error");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Symptom> getAllSymptoms() {

        ArrayList<Symptom> symptoms = new ArrayList<>();
        int counter = 0;

        String sql = "SELECT symptom FROM Symptom";
        try (Statement stmt = c.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next() && counter < 10) {
                Symptom symptom = new Symptom();
                symptom.setSymptom(rs.getString("symptom")); // Asignar el nombre de la enfermedad
                symptoms.add(symptom);
                counter++;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving diseases: " + e.getMessage());
        }

        return symptoms;
    }

    @Override
    public Integer getSymptomId(String symptom) {
        try {
            String sql = "SELECT id FROM Symptom WHERE symptom LIKE ?";
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, symptom);
            ResultSet rs = p.executeQuery();
            int id = rs.getInt("id");
            rs.close();
            p.close();
            return id;
        } catch (SQLException e) {
            System.out.println("Database error while retrieving symptom ID");
            e.printStackTrace();
        }
        return null;
    }
}
