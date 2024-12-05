/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
            // Verificar si el directorio 'db' existe y crearlo si no
            File dbDir = new File("./db");
            if (!dbDir.exists()) {
                dbDir.mkdirs(); // Crea el directorio 'db' si no existe
            }

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

        try ( Statement s = c.createStatement()) {

             // Tabla Administrator (sin dependencias)
            String table_Admin = "CREATE TABLE IF NOT EXISTS Admin ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "dni TEXT NOT NULL,"
                    + "password TEXT NOT NULL);";
            s.executeUpdate(table_Admin);
            System.out.println("Table Admin created.");
            
            // Tabla Doctor (sin dependencias)
            String table_Doctor = "CREATE TABLE IF NOT EXISTS Doctor ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "dni TEXT NOT NULL,"
                    + "password TEXT NOT NULL,"
                    + "name TEXT NOT NULL,"
                    + "surname TEXT NOT NULL,"
                    + "email TEXT NOT NULL,"
                    + "phone INTEGER NOT NULL);";
            s.executeUpdate(table_Doctor);
            System.out.println("Table Doctor created.");

            // Tabla Patient (depende de Doctor)
            String table_Patient = "CREATE TABLE IF NOT EXISTS Patient ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "dni TEXT NOT NULL,"
                    + "password TEXT NOT NULL,"
                    + "name TEXT NOT NULL,"
                    + "surname TEXT NOT NULL,"
                    + "dob DATE NOT NULL,"
                    + "email TEXT NOT NULL,"
                    + "phone INTEGER NOT NULL,"
                    + "gender TEXT NOT NULL,"
                    + "doctor_id INTEGER,"
                    + "FOREIGN KEY (doctor_id) REFERENCES Doctor(id) ON DELETE SET NULL);";
            s.executeUpdate(table_Patient);
            System.out.println("Table Patient created.");

            // Tabla Surgery (sin dependencias)
            String table_Surgery = "CREATE TABLE IF NOT EXISTS Surgery ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "surgery TEXT NOT NULL);";
            s.executeUpdate(table_Surgery);
            System.out.println("Table Surgery created.");

            // Tabla Symptom (sin dependencias)
            String table_Symptom = "CREATE TABLE IF NOT EXISTS Symptom ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "symptom TEXT NOT NULL);";
            s.executeUpdate(table_Symptom);
            System.out.println("Table Symptom created.");

            // Tabla Disease (sin dependencias)
            String table_Disease = "CREATE TABLE IF NOT EXISTS Disease ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "disease TEXT NOT NULL);";
            s.executeUpdate(table_Disease);
            System.out.println("Table Disease created.");

            // Tabla Episode (depende de Patient)
            String table_Episode = "CREATE TABLE IF NOT EXISTS Episode ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "patient_id INTEGER,"
                    + "date DATETIME NOT NULL,"
                    + "FOREIGN KEY (patient_id) REFERENCES Patient(id) ON DELETE SET NULL);";
            s.executeUpdate(table_Episode);
            System.out.println("Table Episode created.");

            // Tabla Episode_Surgery (depende de Episode y Surgery)
            String table_Episode_Surgery = "CREATE TABLE IF NOT EXISTS Episode_Surgery ("
                    + "episode_id INTEGER,"
                    + "surgery_id INTEGER,"
                    + "FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE SET NULL,"
                    + "FOREIGN KEY (surgery_id) REFERENCES Surgery(id) ON DELETE SET NULL,"
                    + "PRIMARY KEY (episode_id, surgery_id));";
            s.executeUpdate(table_Episode_Surgery);
            System.out.println("Table Episode_Surgery created.");

            // Tabla Episode_Symptom (depende de Episode y Symptom)
            String table_Episode_Symptom = "CREATE TABLE IF NOT EXISTS Episode_Symptom ("
                    + "episode_id INTEGER,"
                    + "symptom_id INTEGER,"
                    + "FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE SET NULL,"
                    + "FOREIGN KEY (symptom_id) REFERENCES Symptom(id) ON DELETE SET NULL,"
                    + "PRIMARY KEY (episode_id, symptom_id));";
            s.executeUpdate(table_Episode_Symptom);
            System.out.println("Table Episode_Symptom created.");

            // Tabla Episode_Disease (depende de Episode y Disease)
            String table_Episode_Disease = "CREATE TABLE IF NOT EXISTS Episode_Disease ("
                    + "episode_id INTEGER,"
                    + "disease_id INTEGER,"
                    + "FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE SET NULL,"
                    + "FOREIGN KEY (disease_id) REFERENCES Disease(id) ON DELETE SET NULL,"
                    + "PRIMARY KEY (episode_id, disease_id));";
            s.executeUpdate(table_Episode_Disease);
            System.out.println("Table Episode_Disease created.");

            // Tabla Recording (depende de Episode)
            String table_Recording = "CREATE TABLE IF NOT EXISTS Recording ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "episode_id INTEGER,"
                    + "date DATE NOT NULL,"
                    //+ "duration INTEGER NOT NULL,"
                    + "filepath TEXT NOT NULL, "
                    + "type TEXT, "
                    + "data TEXT ,"
                    //                    + "data TEXT NOT NULL,"
                    + "FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE CASCADE);";
            s.executeUpdate(table_Recording);
            System.out.println("Table Recording created.");
            
            
             insertDefaultData(s);

        } catch (SQLException e) {
            System.out.println("Database error.");
            e.printStackTrace();
        }
    }
    
    
     
    private void insertDefaultData(Statement s) throws SQLException {
    // Insertar enfermedades solo si la tabla está vacía
    String checkDiseases = "SELECT COUNT(*) FROM Disease";
    ResultSet rsDiseases = s.executeQuery(checkDiseases);
    if (rsDiseases.next() && rsDiseases.getInt(1) == 0) {
        String insertDiseases = "INSERT INTO Disease (disease) VALUES " +
                "('Chronic Heart Failure')," +
                "('Hypertension')," +
                "('Ischemic Heart Disease')," +
                "('Dilated Cardiomyopathy');";
        s.executeUpdate(insertDiseases);
        System.out.println("Default diseases inserted.");
    }
    rsDiseases.close();

    // Insertar síntomas solo si la tabla está vacía
    String checkSymptoms = "SELECT COUNT(*) FROM Symptom";
    ResultSet rsSymptoms = s.executeQuery(checkSymptoms);
    if (rsSymptoms.next() && rsSymptoms.getInt(1) == 0) {
        String insertSymptoms = "INSERT INTO Symptom (symptom) VALUES " +
                "('Shortness of breath')," +
                "('Fatigue')," +
                "('Edema')," +
                "('Orthopnea')," +
                "('Nocturnal dyspnea');";
        s.executeUpdate(insertSymptoms);
        System.out.println("Default symptoms inserted.");
    }
    rsSymptoms.close();

    // Insertar cirugías solo si la tabla está vacía
    String checkSurgeries = "SELECT COUNT(*) FROM Surgery";
    ResultSet rsSurgeries = s.executeQuery(checkSurgeries);
    if (rsSurgeries.next() && rsSurgeries.getInt(1) == 0) {
        String insertSurgeries = "INSERT INTO Surgery (surgery) VALUES " +
                "('Heart Transplant')," +
                "('Left Ventricular Assist Device (LVAD)')," +
                "('Coronary Artery Bypass Grafting (CABG)')," +
                "('Implantable Cardioverter Defibrillator (ICD)');";
        s.executeUpdate(insertSurgeries);
        System.out.println("Default surgeries inserted.");
    }
    rsSurgeries.close();
}

}
