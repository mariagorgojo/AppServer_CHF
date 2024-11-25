/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import ifaces.SurgeryManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pojos.Surgery;

/**
 *
 * @author carmengarciaprieto
 */
public class JDBCSurgeryManager implements SurgeryManager {

    private Connection c;

    public JDBCSurgeryManager(Connection c) {
        this.c = c;

    }

    @Override
    public void insertSurgery(String surgery) {
        try {
            String sql = "INSERT OR IGNORE INTO Surgery (surgery) VALUES (?)";
            try ( PreparedStatement p = c.prepareStatement(sql)) {
                p.setString(1, surgery);
                p.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Database exception.");
            e.printStackTrace();
        }

    }

    @Override
    public void assignSurgeryToEpisode(int surgery_id, int episode_id) {
        try {
            String sql = "INSERT INTO Episode_Surgery (surgery_id, episode_id) VALUES (?,?)";
            PreparedStatement p = c.prepareStatement(sql);
            p.setInt(1, surgery_id);
            p.setInt(2, episode_id);
            p.executeUpdate();
            p.close();
        } catch (SQLException e) {
            System.out.println("Database error.");
            e.printStackTrace();
        }
    }

    @Override

    public ArrayList<Surgery> getSurgeriesByEpisode(int episode_id) {
        ArrayList<Surgery> list = new ArrayList<>();
        String sql = "SELECT Surgery.id, Surgery.surgery FROM Surgery "
                + "JOIN Episode_Surgery ON Surgery.id = Episode_Surgery.surgery_id "
                + "WHERE Episode_Surgery.episode_id = ?";
        try ( PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, episode_id);
            try ( ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Surgery s = new Surgery(rs.getInt("id"), rs.getString("surgery"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            System.out.println("database error");
            e.printStackTrace();
        }
        return list;
    }

    @Override

    public ArrayList<Surgery> getSurgeriesByPatient(String patient_id) {
        ArrayList<Surgery> list = new ArrayList<>();
        String sql = "SELECT Surgery.id, Surgery.surgery FROM Surgery "
                + "JOIN Episode_Surgery ON Surgery.id = Episode_Surgery.surgery_id "
                + "JOIN Episode ON Episode_Surgery.episode_id = Episode.id "
                + "WHERE Episode.patient_id = ?";
        try ( PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, patient_id);
            try ( ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Surgery s = new Surgery(rs.getInt("id"), rs.getString("surgery"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            System.out.println("database error");
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String getSurgeryById(int surgery_id) {
        String sql = "SELECT surgery FROM Surgery WHERE id = ?";
        try ( PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, surgery_id);
            try ( ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("surgery");
                }
            }
        } catch (SQLException e) {
            System.out.println("database error");
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra
    }

    @Override
    public ArrayList<Surgery> getAllSurgeries() {
        ArrayList<Surgery> surgeries = new ArrayList<>();
        String sql = "SELECT id, surgery FROM Surgery";
        try ( Statement stmt = c.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Surgery surgery = new Surgery(rs.getInt("id"), rs.getString("surgery"));
                surgeries.add(surgery);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving surgeries: " + e.getMessage());
            e.printStackTrace();
        }
        return surgeries;
    }

    @Override

    public int getSurgeryId(String surgery) {
        String sql = "SELECT id FROM Surgery WHERE surgery LIKE ?";
        try ( PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, surgery);
            try ( ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while retrieving surgery ID");
            e.printStackTrace();
        }
        return -1; // Retorna -1 si no se encuentra
    }

    // nueva
    @Override

    public boolean isSurgeryAssociatedWithEpisode(int surgeryId, int episodeId) {
        String sql = "SELECT COUNT(*) FROM Episode_Surgery WHERE surgery_id = ? AND episode_id = ?";
        try ( PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, surgeryId);
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
