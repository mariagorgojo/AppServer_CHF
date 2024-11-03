/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import ifaces.RecordingManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import pojos.Recording;
import pojos.Recording.Type;
import pojos.Surgery;

/**
 *
 * @author carmengarciaprieto
 */
public class JDBCRecordingManager implements RecordingManager {

    private Connection c;

    public JDBCRecordingManager(Connection c) {
        this.c = c;
    }

    @Override
    public void insertRecording(Recording recording) { 
        String sql = "INSERT INTO Recording (id, type, duration, data, filepath, episode_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, recording.getId());
            p.setString(2, recording.getType().toString());
            p.setInt(3, recording.getDuration());
            p.setInt(4, recording.getData()); // Asegúrate de que `data` es un int; si es un ArrayList<Integer>, será diferente.
            p.setString(5, recording.getSignal_path());
            p.setInt(6, recording.getEpisode_id());

            p.executeUpdate(); // Ejecuta la inserción
        } catch (SQLException e) {
            System.out.println("Database error");
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Recording> getRecordingsByEpisode(int episode_id) {
            ArrayList<Recording> recordings = new ArrayList<>();
            try{
                String sql = "SELECT * FROM Recording WHERE episode_id = ?";			
                PreparedStatement p = c.prepareStatement(sql);
                p.setInt(1, episode_id);
                ResultSet rs = p.executeQuery();
                while (rs.next()) {				
                        Recording r = new Recording(rs.getInt("id"), Type.valueOf(rs.getString("type")), rs.getInt("duration"), rs.getDate("date").toLocalDate(), rs.getString("signal_path"), rs.getInt("data"), rs.getInt("episode_id"));
                        recordings.add(r);
                }
                rs.close();
                p.close(); 
            } catch (SQLException e) {
                    System.out.println("database error");
                    e.printStackTrace();
            }
            return recordings;
    }

    @Override
    public ArrayList<Recording> getRecordingsByType(Recording.Type type, int episode_id) {

    ArrayList<Recording> recordings = new ArrayList<>();    
    String sql = "SELECT * FROM Recording WHERE type = ? AND episode_id = ?";
    try (PreparedStatement p = c.prepareStatement(sql)) {
        p.setString(1, type.name());  // Convierte el enum `type` a String
        p.setInt(2, episode_id);      // Establece el valor de episode_id en el segundo parámetro
        ResultSet rs = p.executeQuery();
        
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Type recordingType = Type.valueOf(rs.getString("type")); // String to Enum
            Integer duration = rs.getInt("duration");
            LocalDate date = rs.getDate("date").toLocalDate(); // SQL Date to LocalDate
            String signal_path = rs.getString("signal_path");
            Integer data = rs.getInt("data"); 

            Recording recording = new Recording(id, recordingType, duration, date, signal_path, data, episode_id);
            recordings.add(recording);
        }
        
    } catch (SQLException e) {
        System.out.println("Database error");
        e.printStackTrace();
    }
    
    return recordings;
}

    @Override
    public Recording getRecordingById(int recording_id) {
            try {
                String sql = "SELECT * FROM Recording WHERE id LIKE ?";
                PreparedStatement p = c.prepareStatement(sql);
                p.setInt(1, recording_id); 
                ResultSet rs = p.executeQuery();
                if(rs.next()){
                    Integer id = rs.getInt("id");
                    Type type = Type.valueOf(rs.getString("type")); // String to Enum
                    Integer duration = rs.getInt("duration");
                    LocalDate date = rs.getDate("date").toLocalDate(); // SQL Date to LocalDate
                    String signal_path = rs.getString("signal_path");
                    Integer data = rs.getInt("data"); 
                    Integer episode_id = rs.getInt("episode_id");
                    return new Recording(id, type, duration, date, signal_path, data, episode_id);
                } else {
                    throw new SQLException("No recording found with the given ID.");
                }
                rs.close();
                p.close();
            } catch (SQLException e) {
                    System.out.println("database error");
                    e.printStackTrace();
            }
		return null;
        }   
}
