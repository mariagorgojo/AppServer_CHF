/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import ifaces.DiseaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pojos.Disease;
import pojos.Symptom;

/**
 *
 * @author carmengarciaprieto
 */
public class JDBCDiseaseManager implements DiseaseManager{
    
    private Connection c;

	public JDBCDiseaseManager(Connection c) {
		this.c = c;

	}
        @Override
	public void insertDisease(String disease) {

		try {
			Statement s = c.createStatement();
			String sql = "INSERT OR IGNORE INTO Disease (disease) VALUES ('"+ disease +"')";
			s.executeUpdate(sql);
			s.close();
		} catch (SQLException e) {
			System.out.println("Database exception.");
			e.printStackTrace();
		}

	}
        @Override
        public void assignDiseaseToEpisode(int disease_id, int episode_id) {
                try {
                        String sql = "INSERT INTO Episode_Disease (disease_id, episode_id) VALUES (?,?)";
                        PreparedStatement p = c.prepareStatement(sql);
                        p.setInt(1, disease_id);
                        p.setInt(2, episode_id);
                        p.executeUpdate();
                        p.close();
                } catch (SQLException e) {
                        System.out.println("Database error.");
                        e.printStackTrace();
                }
        }
	@Override
	public ArrayList<Disease> getDiseasesByEpisode(int episode_id){
		ArrayList<Disease> list = new ArrayList<Disease>();
		try {
			String sql = "SELECT disease_id FROM Episode_Disease WHERE episode_id LIKE ?";
			PreparedStatement p = c.prepareStatement(sql);
			p.setInt(1, episode_id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {				
				Disease d = new Disease(rs.getInt("id"), rs.getString("disease"));
				list.add(d);
			}
		} catch (SQLException e) {
			System.out.println("database error");
			e.printStackTrace();
		}
		return list;
	}
        
        @Override
        public ArrayList<Disease> getDiseasesByPatient(String patient_id){
                ArrayList<Disease> list = new ArrayList<Disease>();
		try {
			String sql = "SELECT Disease.* FROM Disease JOIN Episode_Disease ON Disease.id = Episode_Disease.disease_id JOIN Episode ON Episode_Disease.episode_id = Episode.id WHERE Episode.patient_id = ?;";
			PreparedStatement p = c.prepareStatement(sql);
			p.setString(1, patient_id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {				
				Disease d = new Disease(rs.getInt("id"), rs.getString("disease"));
				list.add(d);
			}
		} catch (SQLException e) {
			System.out.println("database error");
			e.printStackTrace();
		}
		return list;
        }
        
        @Override
        public String getDiseaseById(int disease_id){
                try {
			String sql = "SELECT disease FROM Disease WHERE id LIKE ?";
			PreparedStatement p = c.prepareStatement(sql);
			p.setInt(1, disease_id); 
			ResultSet rs = p.executeQuery();
			String name=rs.getString("disease");
			rs.close();
		    p.close();
			return name;
		} catch (SQLException e) {
			System.out.println("database error");
			e.printStackTrace();
		}
		return null;
        }
        @Override
        public Integer getDiseaseId(String disease){
                try {
			String sql = "SELECT id FROM Disease WHERE disease LIKE ?";
			PreparedStatement p = c.prepareStatement(sql);
			p.setString(1, disease); 
			ResultSet rs = p.executeQuery();
			int id = rs.getInt("id");
			rs.close();
		    p.close();
			return id;
		} catch (SQLException e) {
			System.out.println("database error");
			e.printStackTrace();
		}
		return null;
        }
    @Override
    public ArrayList<Disease> getAllDiseases() {
        
       ArrayList<Disease> diseases = new ArrayList<>();
       int counter=0;

    String sql = "SELECT disease FROM Disease";
    try (Statement stmt = c.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next() && counter <10 ) {
            Disease disease = new Disease();
            disease.setDisease(rs.getString("disease")); // Asignar el nombre de la enfermedad
            diseases.add(disease);
            counter++;
        }
    } catch (SQLException e) {
        System.err.println("Error retrieving diseases: " + e.getMessage());
    }

    return diseases;
} 
    
        
        
}
