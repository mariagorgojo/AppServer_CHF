/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
            // verifies if directory 'db' exists, and if not, creates it
            File dbDir = new File("./db");
            if (!dbDir.exists()) {
                dbDir.mkdirs(); // creates 'db' directory
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

            // Table Administrator (without dependencies)
            String table_Administrator = "CREATE TABLE IF NOT EXISTS Administrator ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "dni TEXT NOT NULL,"
                    + "password TEXT NOT NULL);";
            s.executeUpdate(table_Administrator);
            System.out.println("Table Administrator created.");

            // Table Doctor (without dependencies)
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

            // Table Patient (depends on Doctor)
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

            // Table Surgery (without dependencies)
            String table_Surgery = "CREATE TABLE IF NOT EXISTS Surgery ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "surgery TEXT NOT NULL);";
            s.executeUpdate(table_Surgery);
            System.out.println("Table Surgery created.");

            // Table Symptom (without dependencies)
            String table_Symptom = "CREATE TABLE IF NOT EXISTS Symptom ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "symptom TEXT NOT NULL);";
            s.executeUpdate(table_Symptom);
            System.out.println("Table Symptom created.");

            // Table Disease (without dependencies)
            String table_Disease = "CREATE TABLE IF NOT EXISTS Disease ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "disease TEXT NOT NULL);";
            s.executeUpdate(table_Disease);
            System.out.println("Table Disease created.");

            // Table Episode (depends on Patient)
            String table_Episode = "CREATE TABLE IF NOT EXISTS Episode ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "patient_id INTEGER,"
                    + "date DATETIME NOT NULL,"
                    + "FOREIGN KEY (patient_id) REFERENCES Patient(id) ON DELETE SET NULL);";
            s.executeUpdate(table_Episode);
            System.out.println("Table Episode created.");

            // Table Episode_Surgery (depends on Episode and Surgery)
            String table_Episode_Surgery = "CREATE TABLE IF NOT EXISTS Episode_Surgery ("
                    + "episode_id INTEGER,"
                    + "surgery_id INTEGER,"
                    + "FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE SET NULL,"
                    + "FOREIGN KEY (surgery_id) REFERENCES Surgery(id) ON DELETE SET NULL,"
                    + "PRIMARY KEY (episode_id, surgery_id));";
            s.executeUpdate(table_Episode_Surgery);
            System.out.println("Table Episode_Surgery created.");

            // Table Episode_Symptom (depends on Episode and Symptom)
            String table_Episode_Symptom = "CREATE TABLE IF NOT EXISTS Episode_Symptom ("
                    + "episode_id INTEGER,"
                    + "symptom_id INTEGER,"
                    + "FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE SET NULL,"
                    + "FOREIGN KEY (symptom_id) REFERENCES Symptom(id) ON DELETE SET NULL,"
                    + "PRIMARY KEY (episode_id, symptom_id));";
            s.executeUpdate(table_Episode_Symptom);
            System.out.println("Table Episode_Symptom created.");

            // Table Episode_Disease (depends on Episode and Disease)
            String table_Episode_Disease = "CREATE TABLE IF NOT EXISTS Episode_Disease ("
                    + "episode_id INTEGER,"
                    + "disease_id INTEGER,"
                    + "FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE SET NULL,"
                    + "FOREIGN KEY (disease_id) REFERENCES Disease(id) ON DELETE SET NULL,"
                    + "PRIMARY KEY (episode_id, disease_id));";
            s.executeUpdate(table_Episode_Disease);
            System.out.println("Table Episode_Disease created.");

            // Table Recording (depends on Episode)
            String table_Recording = "CREATE TABLE IF NOT EXISTS Recording ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "episode_id INTEGER,"
                    + "date DATE NOT NULL,"
                    + "filepath TEXT NOT NULL, "
                    + "type TEXT, "
                    + "data TEXT ,"
                    + "FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE CASCADE);";
            s.executeUpdate(table_Recording);
            System.out.println("Table Recording created.");

            // Table Patient_Disease (depends on Patient and Disease)
            String table_Patient_Disease = "CREATE TABLE IF NOT EXISTS Patient_Disease ("
                    + "patient_id INTEGER,"
                    + "disease_id INTEGER,"
                    + "FOREIGN KEY (patient_id) REFERENCES Patient(id) ON DELETE SET NULL,"
                    + "FOREIGN KEY (disease_id) REFERENCES Disease(id) ON DELETE SET NULL,"
                    + "PRIMARY KEY (patient_id, disease_id));";
            s.executeUpdate(table_Patient_Disease);
            System.out.println("Table Patient_Disease created.");

            insertDefaultData(s);
 

        } catch (SQLException e) {
            System.out.println("Database error.");
            e.printStackTrace();
        }
    }

    private void insertDefaultData(Statement s) throws SQLException {
        // Inserts diseases only if table is empty
        String checkDiseases = "SELECT COUNT(*) FROM Disease";
        ResultSet rsDiseases = s.executeQuery(checkDiseases);
        if (rsDiseases.next() && rsDiseases.getInt(1) == 0) {
            String insertDiseases = "INSERT INTO Disease (disease) VALUES "
                    + "('Chronic Heart Failure'),"
                    + "('Hypertension'),"
                    + "('Ischemic Heart Disease'),"
                    + "('Dilated Cardiomyopathy'),"
                    + "('Arrhythmia'),"
                    + "('Atrial Fibrillation'),"
                    + "('Myocardial Infarction'),"
                    + "('Congenital Heart Defect'),"
                    + "('Pulmonary Hypertension'),"
                    + "('Pericarditis');";
            s.executeUpdate(insertDiseases);
            System.out.println("Default diseases inserted.");
        }
        rsDiseases.close();

        // Inserts symptoms only if table is empty
        String checkSymptoms = "SELECT COUNT(*) FROM Symptom";
        ResultSet rsSymptoms = s.executeQuery(checkSymptoms);
        if (rsSymptoms.next() && rsSymptoms.getInt(1) == 0) {
            String insertSymptoms = "INSERT INTO Symptom (symptom) VALUES "
                    + "('Shortness of breath'),"
                    + "('Fatigue'),"
                    + "('Edema'),"
                    + "('Orthopnea'),"
                    + "('Nocturnal dyspnea'),"
                    + "('Chest Pain'),"
                    + "('Palpitations'),"
                    + "('Dizziness'),"
                    + "('Syncope'),"
                    + "('Cough with frothy sputum');";
            s.executeUpdate(insertSymptoms);
            System.out.println("Default symptoms inserted.");
        }
        rsSymptoms.close();

        // Inserts surgeries only if table is empty
        String checkSurgeries = "SELECT COUNT(*) FROM Surgery";
        ResultSet rsSurgeries = s.executeQuery(checkSurgeries);
        if (rsSurgeries.next() && rsSurgeries.getInt(1) == 0) {
            String insertSurgeries = "INSERT INTO Surgery (surgery) VALUES "
                    + "('Heart Transplant'),"
                    + "('Left Ventricular Assist Device (LVAD)'),"
                    + "('Coronary Artery Bypass Grafting (CABG)'),"
                    + "('Implantable Cardioverter Defibrillator (ICD)'),"
                    + "('Aortic Valve Replacement'),"
                    + "('Mitral Valve Repair'),"
                    + "('Transcatheter Aortic Valve Implantation (TAVI)'),"
                    + "('Cardiac Resynchronization Therapy (CRT)'),"
                    + "('Catheter Ablation');";
                    
            s.executeUpdate(insertSurgeries);
            System.out.println("Default surgeries inserted.");
        }
        rsSurgeries.close();
    }
}
