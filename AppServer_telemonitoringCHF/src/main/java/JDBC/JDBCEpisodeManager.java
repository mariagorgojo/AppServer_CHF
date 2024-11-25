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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
                        LocalDateTime.parse(rs.getString("date")),
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
                        LocalDateTime.parse(rs.getString("date")),
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
                        LocalDateTime.parse(rs.getString("date")),
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

    // CRAEDA NUEVA -> MARTA G  VOLVER
    @Override
    public Integer getEpisodeId(LocalDateTime date, int patient_id) {
        String sql = "SELECT id FROM Episode WHERE date = ? AND patient_id = ?";

        try ( PreparedStatement p = c.prepareStatement(sql)) {
            // Convertir LocalDateTime al formato esperado por la base de datos
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            p.setString(1, formattedDate); // Usar setString con el formato correcto
            p.setInt(2, patient_id); // Establecer el patient_id como segundo parámetro

            try ( ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id"); // Retornar el id si existe
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar el ID del episodio: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Retornar null si no se encuentra el episodio o hay un error
    }


    /* LA HIZO CARMEN LA MODIFICO
    @Override
    public Integer getEpisodeId(LocalDateTime date) {
        try {
            String sql = "SELECT id FROM Episode WHERE date = ?";
            PreparedStatement p = c.prepareStatement(sql);
            // Convertir LocalDateTime a java.sql.Timestamp antes de pasarlo al PreparedStatement
            p.setTimestamp(1, java.sql.Timestamp.valueOf(date));
            ResultSet rs = p.executeQuery();

            // Verificar si hay resultados
            if (rs.next()) {
                int id = rs.getInt("id");
                rs.close();
                p.close();
                return id;
            } else {
                rs.close();
                p.close();
                return null; // No se encontró el episodio
            }
        } catch (SQLException e) {
            System.out.println("Database error while retrieving episode ID");
            e.printStackTrace();
        }
        return null;
    } */
}
