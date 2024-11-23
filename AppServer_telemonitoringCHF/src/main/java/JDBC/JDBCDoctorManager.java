/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import ifaces.DoctorManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pojos.Doctor;
import pojos.Patient;

/**
 *
 * @author carmengarciaprieto
 */
public class JDBCDoctorManager implements DoctorManager{
    
    private Connection c;

	public JDBCDoctorManager(Connection c) {
		this.c = c;

	}

        
	@Override
    public void insertDoctor(Doctor doctor) {
        try {
            String sql = "INSERT INTO Doctor (dni, password, name, surname, phone, email) VALUES (?,?, ?, ?, ?, ?)";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setString(1, doctor.getDni());
            prep.setString(2, doctor.getPassword());
            prep.setString(3, doctor.getName());
            prep.setString(4, doctor.getSurname());
            prep.setInt(5, doctor.getTelephone());
            prep.setString(6, doctor.getEmail());
            prep.executeUpdate();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error inserting doctor.");
            e.printStackTrace();
        }
    }

    @Override
    public Doctor getDoctorByDNI(String dni) {
        Doctor doctor = null;
        try {
            String sql = "SELECT * FROM Doctor WHERE dni = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setString(1, dni);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                doctor = new Doctor(rs.getInt("id"));
                doctor.setDni(rs.getString("dni"));
                doctor.setPassword(rs.getString("password"));
                doctor.setName(rs.getString("name"));
                doctor.setSurname(rs.getString("surname"));
                doctor.setTelephone(rs.getInt("phone"));
                doctor.setEmail(rs.getString("email"));
            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error getting doctor by DNI.");
            e.printStackTrace();
        }
        return doctor;
    }

    @Override
    public Doctor getDoctorByEmail(String email) {
        Doctor doctor = null;
        try {
            String sql = "SELECT * FROM Doctor WHERE email = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setString(1, email);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                doctor = new Doctor(rs.getInt("id"));
                doctor.setDni(rs.getString("dni"));
                doctor.setName(rs.getString("name"));
                doctor.setSurname(rs.getString("surname"));
                doctor.setTelephone(rs.getInt("phone"));
                doctor.setEmail(rs.getString("email"));
            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error getting doctor by email.");
            e.printStackTrace();
        }
        return doctor;
    }

    @Override
    public int countNumberOfDoctors() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) AS count FROM Doctor";
            PreparedStatement prep = c.prepareStatement(sql);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error counting doctors.");
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public Doctor getDoctorById(int id) {
        Doctor doctor = null;
        try {
            String sql = "SELECT * FROM Doctor WHERE id = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                doctor = new Doctor(rs.getInt("id"));
                doctor.setDni(rs.getString("dni"));
                doctor.setName(rs.getString("name"));
                doctor.setSurname(rs.getString("surname"));
                doctor.setTelephone(rs.getInt("phone"));
                doctor.setEmail(rs.getString("email"));
            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error getting doctor by ID.");
            e.printStackTrace();
        }
        return doctor;
    }

    @Override
    public int getRandomId() {
        int randomId = -1;
        try {
            String sql = "SELECT id FROM Doctor ORDER BY RANDOM() LIMIT 1";
            PreparedStatement prep = c.prepareStatement(sql);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                randomId = rs.getInt("id");
            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error getting random doctor ID.");
            e.printStackTrace();
        }
        return randomId;
    }

    @Override
    public Doctor getDoctorByPatient(Patient patient) {
        Doctor doctor = null;
        try {
            String sql = "SELECT * FROM Doctor WHERE id = (SELECT doctor_id FROM Patient WHERE id = ?)";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setInt(1, patient.getId());
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                doctor = new Doctor(rs.getInt("id"));
                doctor.setDni(rs.getString("dni"));
                doctor.setName(rs.getString("name"));
                doctor.setSurname(rs.getString("surname"));
                doctor.setTelephone(rs.getInt("phone"));
                doctor.setEmail(rs.getString("email"));
            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error getting doctor by patient.");
            e.printStackTrace();
        }
        return doctor;
    }

    @Override
    public Doctor getDoctorByPhone(Integer phone) {
        Doctor doctor = null;
        try {
            String sql = "SELECT * FROM Doctor WHERE phone = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setInt(1, phone);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                doctor = new Doctor(rs.getInt("id"));
                doctor.setDni(rs.getString("dni"));
                doctor.setName(rs.getString("name"));
                doctor.setSurname(rs.getString("surname"));
                doctor.setTelephone(rs.getInt("phone"));
                doctor.setEmail(rs.getString("email"));
            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error getting doctor by phone.");
            e.printStackTrace();
        }
        return doctor;
    }
}