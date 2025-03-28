package JDBC;

import ifaces.RecordingManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import pojos.Recording;
import pojos.Recording.Type;
import java.lang.Integer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCRecordingManager implements RecordingManager {

    private Connection c;

    public JDBCRecordingManager(Connection c) {
        this.c = c;
    }

    @Override
    public void insertRecording(Recording recording) {
        String sql = "INSERT INTO Recording (type, date, filepath, data, episode_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, recording.getType().toString());
            p.setString(2, recording.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS")));
            // Convierte el ArrayList<Integer> `data` en una cadena de texto separada por comas
            p.setString(3, recording.getSignal_path());
            String dataAsString = recording.getData().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            p.setString(4, dataAsString); // Guarda como texto

            p.setInt(5, recording.getEpisode_id());

            p.executeUpdate(); // Ejecuta la inserción
        } catch (SQLException e) {
            System.out.println("Database error");
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Recording> getRecordingsByEpisode(int episode_id, int patient_id) {
        ArrayList<Recording> recordings = new ArrayList<>();
        //nuevo
        try {
            if (c == null || c.isClosed()) {
                System.err.println("Database connection is not available.");
                return recordings;
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCRecordingManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT * FROM Recording JOIN Episode ON Recording.episode_id = Episode.id JOIN Patient ON Episode.patient_id = Patient.id WHERE Recording.episode_id = ? AND Episode.patient_id = ?;";

        try (PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, episode_id);
            p.setInt(2, patient_id);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                // Convierte la cadena `data` de vuelta a un ArrayList<Integer>
                ArrayList<Integer> data = Arrays.stream(rs.getString("data").split(","))
                        .map(Integer::valueOf)
                        .collect(Collectors.toCollection(ArrayList::new));

                Recording recording = new Recording(
                        rs.getInt("id"),
                        Type.valueOf(rs.getString("type")),
                        rs.getTimestamp("date").toLocalDateTime(), // Conversión directa
                        rs.getString("filepath"),
                        data, // Usa el ArrayList<Integer> de `data`
                        rs.getInt("episode_id")
                );
                recordings.add(recording);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving recordings for episode ID: " + episode_id);
            e.printStackTrace();
        }
        return recordings;
    }

    @Override
    public ArrayList<Recording> getRecordingsByType(Recording.Type type, int episode_id) {
        ArrayList<Recording> recordings = new ArrayList<>();
        String sql = "SELECT * FROM Recording WHERE type = ? AND episode_id = ?";

        try (PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, type.name());
            p.setInt(2, episode_id);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                ArrayList<Integer> data = Arrays.stream(rs.getString("data").split(","))
                        .map(Integer::valueOf)
                        .collect(Collectors.toCollection(ArrayList::new));

                Recording recording = new Recording(
                        rs.getInt("id"),
                        Type.valueOf(rs.getString("type")),
                        LocalDateTime.parse(rs.getString("date")),
                        rs.getString("filepath"),
                        data,
                        rs.getInt(episode_id)
                );
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
        String sql = "SELECT * FROM Recording WHERE id = ?";
        try (PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, recording_id);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                ArrayList<Integer> data = Arrays.stream(rs.getString("data").split(","))
                        .map(Integer::valueOf)
                        .collect(Collectors.toCollection(ArrayList::new));

                return new Recording(
                        rs.getInt("id"),
                        Type.valueOf(rs.getString("type")),
                        LocalDateTime.parse(rs.getString("date")),
                        rs.getString("filepath"),
                        data,
                        rs.getInt("episode_id")
                );
            } else {
                throw new SQLException("No recording found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Database error");
            e.printStackTrace();
        }
        return null;
    }
}
    

