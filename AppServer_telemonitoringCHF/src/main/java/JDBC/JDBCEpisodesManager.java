/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import ifaces.EpisodesManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import pojos.Episodes;


public class JDBCEpisodesManager implements EpisodesManager {


    private Connection c;

    public JDBCEpisodesManager(Connection c) {
        this.c = c;
    }

    @Override
    public void insertEpisode(Episodes episode) {
        try {
            String sql = "INSERT INTO Episodes (patient_id, date) VALUES (?, ?)";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setInt(1, episode.getPatient_id());
            prep.setString(2, episode.getDate().toString());
            prep.executeUpdate();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error inserting episode.");
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Episodes> getEpisodesByPatient(int patient_id) {
        ArrayList<Episodes> episodesList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Episodes WHERE patient_id = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setInt(1, patient_id);
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                Episodes episode = new Episodes(
                    rs.getInt("id"),
                    LocalDate.parse(rs.getString("date")),
                    rs.getInt("patient_id")
                );
                episodesList.add(episode);
            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving episodes by patient.");
            e.printStackTrace();
        }
        return episodesList;
    }

    @Override
    public ArrayList<Episodes> getEpisodesByDate(int patient_id, LocalDate date) {
        ArrayList<Episodes> episodesList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Episodes WHERE patient_id = ? AND date = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setInt(1, patient_id);
            prep.setString(2, date.toString());
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                Episodes episode = new Episodes(
                    rs.getInt("id"),
                    LocalDate.parse(rs.getString("date")),
                    rs.getInt("patient_id")
                );
                episodesList.add(episode);
            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving episodes by date.");
            e.printStackTrace();
        }
        return episodesList;
    }

    @Override
    public Episodes getEpisodeById(int patient_id, int episode_id) {
        Episodes episode = null;
        try {
            String sql = "SELECT * FROM Episodes WHERE patient_id = ? AND id = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setInt(1, patient_id);
            prep.setInt(2, episode_id);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                episode = new Episodes(
                    rs.getInt("id"),
                    LocalDate.parse(rs.getString("date")),
                    rs.getInt("patient_id")
                );
            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving episode by ID.");
            e.printStackTrace();
        }
        return episode;
    }
}