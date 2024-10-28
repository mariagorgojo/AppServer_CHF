/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author mariagorgojo
 */
public class ConnectionJDBC {
    
    private Connection c;

	public ConnectionJDBC() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:./db/telemedicine.db");
			c.createStatement().execute("PRAGMA foreign_keys=ON");
			System.out.println("Database connection opened.");
			createTables();
			insertTables();
			insertVaccines();

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
        try {
			Statement s = c.createStatement();
			
			String table_Doctor = "CREATE TABLE Doctor ("
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " dni TEXT NOT NULL,"
					+ " name TEXT NOT NULL,"
					+ " surname TEXT NOT NULL,"
					+ " email TEXT NOT NULL,"
                                        + " phone INTEGER NUT NULL);";
                        
			s.executeUpdate(table_Doctor);
			
			String table_Patient = "CREATE TABLE Patient ("
					+ " id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " dni TEXT NOT NULL,"
					+ " name TEXT NOT NULL,"
					+ " surname TEXT NOT NULL,"
                                        + " dob DATE NOT NULL,"
					+ " email TEXT NOT NULL,"
                                        + " phone INTEGER NOT NULL,"
                                        + " gender TEXT NOT NULL,"
					+ " doctor_id INTEGER,"
					+ " FOREIGN KEY (doctor_id) REFERENCES Doctor(id) ON DELETE SET NULL,"
                                        + " FOREIGN KEY (medicalhistory_id) REFERENCES Medicalhistory(id) ON DELETE SET NULL);";
			s.executeUpdate(table_Patient);
			
                          String table_Medicalhistory = "CREATE TABLE MedicalHistory ("
					+ " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                        + " date DATE NUT NULL,"
					+ " weigth INTEGER NOT NULL,"
					+ " heigth INTEGER NOT NULL,"
					+ " diseases," //lista de texto o tabla por separado
                                        + " surgeries," //lista de texto o tabla por separado 
					+ " FOREIGN KEY (patient_id) REFERENCES Patient(id) ON DELETE SET NULL);";
			s.executeUpdate(table_Patient);
			
                            String table_Bitalinosignals = "CREATE TABLE Bitalinosignals ("
					+ " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                        + " date DATE NUT NULL,"
					+ " type TEXT NOT NULL,"
					+ " duration INTEGER NOT NULL,"
					+ " filepath TEXT NOT NULL,"
                                        + " symptoms,"
                                        + " data,"  //BLOB si es todo en binario, TEXT O VARCHAR para texto
					+ " FOREIGN KEY (patient_id) REFERENCES Patient(id) ON DELETE SET NULL);";
			s.executeUpdate(table_Patient);
                        
                        
			s.close();
                        
		} catch (SQLException e) {
			if (e.getMessage().contains("already exist")) {
				return;
			}
			System.out.println("Database error.");
			e.printStackTrace();
		}
    }

    private void insertTables() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void insertVaccines() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
