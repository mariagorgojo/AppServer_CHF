/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import pojos.Episode;
import ifaces.EpisodeManager;


public class JDBCEpisodeManager implements EpisodeManager {


    private Connection c;

    public JDBCEpisodeManager(Connection c) {
        this.c = c;
    }

    @Override
    public void insertEpisode(Episode episode) {
        try {
            String sql = "INSERT INTO Episode (patient_id, date) VALUES (?, ?)";
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
    public ArrayList<Episode> getEpisodesByPatient(Integer patient_id) {
        ArrayList<Episode> episodesList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Episode WHERE patient_id = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setInt(1, patient_id);
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                Episode episode = new Episode(
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
    public ArrayList<Episode> getEpisodesByDate(int patient_id, LocalDate date) {
        ArrayList<Episode> episodesList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Episode WHERE patient_id = ? AND date = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setInt(1, patient_id);
            prep.setString(2, date.toString());
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                Episode episode = new Episode(
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
    public Episode getEpisodeById(int patient_id, int episode_id) {
        Episode episode = null;
        try {
            String sql = "SELECT * FROM Episode WHERE patient_id = ? AND id = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setInt(1, patient_id);
            prep.setInt(2, episode_id);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                episode = new Episode(
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