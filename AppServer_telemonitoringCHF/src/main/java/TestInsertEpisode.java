
import Executable.*;
import JDBC.ConnectionManager;
import JDBC.JDBCEpisodeManager;
import pojos.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List; 

public class TestInsertEpisode {
    public static void main(String[] args) {
        ConnectionManager connectionManager = new ConnectionManager();
        Connection connection = null;
        JDBCEpisodeManager episodeManager = null;

        try {
            // Obtener la conexión
            connection = connectionManager.getConnection();
            episodeManager = new JDBCEpisodeManager(connection);

            // Crear un episodio para el paciente con ID 101 y fecha actual
            Episode episode = new Episode(101, LocalDateTime.now()); // Paciente con ID 101

            // Añadir cirugías al episodio
            Surgery appendectomy = new Surgery();
            appendectomy.setId(1); // ID ya existente en la tabla Surgery para "Appendectomy"
            Surgery gallbladder = new Surgery();
            gallbladder.setId(2); // ID ya existente en la tabla Surgery para "Gallbladder Removal"
            episode.getSurgeries().add(appendectomy);
            episode.getSurgeries().add(gallbladder);

            // Añadir síntomas al episodio
            Symptom fever = new Symptom();
            fever.setId(1); // ID ya existente en la tabla Symptom para "Fever"
            Symptom pain = new Symptom();
            pain.setId(2); // ID ya existente en la tabla Symptom para "Pain"
            episode.getSymptoms().add(fever);
            episode.getSymptoms().add(pain);

            // Añadir enfermedades al episodio
            Disease influenza = new Disease();
            influenza.setId(1); // ID ya existente en la tabla Disease para "Influenza"
            Disease gallstones = new Disease();
            gallstones.setId(2); // ID ya existente en la tabla Disease para "Gallstones"
            episode.getDiseases().add(influenza);
            episode.getDiseases().add(gallstones);

            // Añadir grabaciones al episodio
            Recording recording1 = new Recording();
            recording1.setDate(LocalDate.now());
            recording1.setDuration(120); // Duración en segundos
            recording1.setSignal_path("/path/to/recording1.wav"); // Ruta del archivo de la grabación
            recording1.setData(new ArrayList<>(List.of(1023, 1056, 1102))); // Datos de la señal
            episode.getRecordings().add(recording1);

            Recording recording2 = new Recording();
            recording2.setDate(LocalDate.now());
            recording2.setDuration(150); // Duración en segundos
            recording2.setSignal_path("/path/to/recording2.wav"); // Ruta del archivo de la grabación
            recording2.setData(new ArrayList<>(List.of(980, 990, 1000))); // Datos de la señal
            episode.getRecordings().add(recording2);

            // Insertar el episodio en la base de datos
            episodeManager.insertEpisode(episode);

            System.out.println("Episode inserted successfully!");

        }catch (Exception e) {
            // Manejo de excepciones genéricas
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        // Manejo de excepciones específicas de SQL
         finally {
            // Asegurar que la conexión se cierre
            if (connection != null) {
                try {
                    connectionManager.closeConnection();
                } catch (Exception e) {
                    System.err.println("Error closing the connection: " + e.getMessage());
                }
            }
        }
    }
}