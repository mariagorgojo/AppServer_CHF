/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import ifaces.SurgeryManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pojos.Surgery;

/**
 *
 * @author carmengarciaprieto
 */
public class JDBCSurgeryManager implements SurgeryManager {
    	
    private Connection c;

	public JDBCSurgeryManager(Connection c) {
		this.c = c;

	}
        @Override
	public void insertSurgery(Surgery surgery) {

		try {
			Statement s = c.createStatement();
			String sql = "INSERT INTO Surgery (id, surgery) VALUES ('" + surgery.getId() + "', '"
					+ surgery.getSurgery()+"')";
			s.executeUpdate(sql);
			s.close();
		} catch (SQLException e) {
			System.out.println("Database exception.");
			e.printStackTrace();
		}

	}
        @Override
            public void assignSurgeryToEpisode(int surgery_id, int episode_id) {
            try {
                    String sql = "INSERT INTO Episode_Surgery (surgery_id, episode_id) VALUES (?,?)";
                    PreparedStatement p = c.prepareStatement(sql);
                    p.setInt(1, surgery_id);
                    p.setInt(2, episode_id);
                    p.executeUpdate();
                    p.close();
            } catch (SQLException e) {
                    System.out.println("Database error.");
                    e.printStackTrace();
            }
    }
        @Override
	public ArrayList<Surgery> getSurgeriesByEpisode(int episode_id){
		ArrayList<Surgery> list = new ArrayList<Surgery>();
		try {
			String sql = "SELECT surgery_id FROM Episode_Surgery WHERE episode_id LIKE ?";
			PreparedStatement p = c.prepareStatement(sql);
			p.setInt(1, episode_id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {				
				Surgery s = new Surgery(rs.getInt("id"), rs.getString("surgery"));
				list.add(s);
			}
		} catch (SQLException e) {
			System.out.println("database error");
			e.printStackTrace();
		}
		return list;
	}
        @Override
        public ArrayList<Surgery> getSurgeriesByPatient(int patient_id){
            	ArrayList<Surgery> list = new ArrayList<Surgery>();
		try {
			String sql = "SELECT Surgery.* FROM Surgery JOIN Episode_Surgery ON Surgery.id = Episode_Surgery.surgery_id JOIN Episode ON Episode_Surgery.episode_id = Episode.id WHERE Episode.patient_id = ?;";
			PreparedStatement p = c.prepareStatement(sql);
			p.setInt(1, patient_id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {				
				Surgery s = new Surgery(rs.getInt("id"), rs.getString("surgery"));
				list.add(s);
			}
		} catch (SQLException e) {
			System.out.println("database error");
			e.printStackTrace();
		}
		return list;
        }
        @Override
        public String getSurgeryById(int surgery_id){
                try {
			String sql = "SELECT surgery FROM Surgery WHERE id LIKE ?";
			PreparedStatement p = c.prepareStatement(sql);
			p.setInt(1, surgery_id); 
			ResultSet rs = p.executeQuery();
			String name=rs.getString("surgery");
			rs.close();
		    p.close();
			return name;
		} catch (SQLException e) {
			System.out.println("database error");
			e.printStackTrace();
		}
		return null;
        }
}
