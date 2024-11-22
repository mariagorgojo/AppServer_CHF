package JDBC;

import ifaces.PatientManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import pojos.Doctor;
import pojos.Patient;
import pojos.Patient.Gender;

public class JDBCPatientManager implements PatientManager {

    private Connection c;

    public JDBCPatientManager(Connection c) {
        this.c = c;
    }

    @Override
    public void insertPatient(Patient patient, Doctor doctor) {
        try {
            String sql = "INSERT INTO Patient (dni, name, surname, email, gender, phone, dob, doctor_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setString(1, patient.getDNI());
            prep.setString(2, patient.getName());
            prep.setString(3, patient.getSurname());
            prep.setString(4, patient.getEmail());
            prep.setString(5, patient.getGender().toString());
            prep.setInt(6, patient.getPhoneNumber());
            prep.setObject(7, patient.getDob());
            prep.setInt(8, doctor != null ? doctor.getId() : null);
            prep.executeUpdate();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error inserting patient.");
            e.printStackTrace();

        }

    }

    @Override
    public ArrayList<Patient> searchPatientsByDoctor(Integer id) {
        ArrayList<Patient> patients = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Patient WHERE doctor_id = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                Patient patient = new Patient(rs.getInt("id"));
                patient.setDni(rs.getString("dni"));
                patient.setName(rs.getString("name"));
                patient.setSurname(rs.getString("surname"));
                patient.setEmail(rs.getString("email"));
                patient.setGender(Gender.valueOf(rs.getString("gender")));
                patient.setPhoneNumber(rs.getInt("phone"));
                //patient.setDob(rs.getObject("dob", java.time.LocalDate.class));
                String dobString = rs.getString("dob");
                if (dobString != null) {
                    patient.setDob(LocalDate.parse(dobString)); // Asegúrate de que el formato sea 'yyyy-MM-dd'
                }
                patients.add(patient);
            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error searching patients by doctor.");
            e.printStackTrace();
        }
        return patients;
    }

    @Override
    public Patient getPatientById(int p_id) {
        Patient patient = null;
        try {
            String sql = "SELECT * FROM Patient WHERE id = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setInt(1, p_id);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                patient = new Patient(rs.getInt("id"));
                patient.setDni(rs.getString("dni"));
                patient.setName(rs.getString("name"));
                patient.setSurname(rs.getString("surname"));
                patient.setEmail(rs.getString("email"));
                patient.setGender(Gender.valueOf(rs.getString("gender")));
                patient.setPhoneNumber(rs.getInt("phone"));
 String dobString = rs.getString("dob");
                if (dobString != null) {
                    patient.setDob(LocalDate.parse(dobString)); // Asegúrate de que el formato sea 'yyyy-MM-dd'
                }            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error getting patient by ID.");
            e.printStackTrace();
        }
        return patient;
    }

    @Override
    public ArrayList<Patient> searchPatientByName(String name) {
        ArrayList<Patient> patients = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Patient WHERE name = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setString(1, name);
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                Patient patient = new Patient(rs.getInt("id"));
                patient.setDni(rs.getString("dni"));
                patient.setName(rs.getString("name"));
                patient.setSurname(rs.getString("surname"));
                patient.setEmail(rs.getString("email"));
                patient.setGender(Gender.valueOf(rs.getString("gender")));
                patient.setPhoneNumber(rs.getInt("phone"));
 String dobString = rs.getString("dob");
                if (dobString != null) {
                    patient.setDob(LocalDate.parse(dobString)); // Asegúrate de que el formato sea 'yyyy-MM-dd'
                }                patients.add(patient);
            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error searching patient by name.");
            e.printStackTrace();
        }
        return patients;
    }

    @Override
    public Patient getPatientByEmail(String email) {
        Patient patient = null;
        try {
            String sql = "SELECT * FROM Patient WHERE email = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setString(1, email);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                patient = new Patient(rs.getInt("id"));
                patient.setDni(rs.getString("dni"));
                patient.setName(rs.getString("name"));
                patient.setSurname(rs.getString("surname"));
                patient.setEmail(rs.getString("email"));
                patient.setGender(Gender.valueOf(rs.getString("gender")));
                patient.setPhoneNumber(rs.getInt("phone"));
 String dobString = rs.getString("dob");
                if (dobString != null) {
                    patient.setDob(LocalDate.parse(dobString)); // Asegúrate de que el formato sea 'yyyy-MM-dd'
                }            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error getting patient by email.");
            e.printStackTrace();
        }
        return patient;
    }

    @Override
    public Patient getPatientByPhone(Integer phone) {
        Patient patient = null;
        try {
            String sql = "SELECT * FROM Patient WHERE phone = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setInt(1, phone);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                patient = new Patient(rs.getInt("id"));
                patient.setDni(rs.getString("dni"));
                patient.setName(rs.getString("name"));
                patient.setSurname(rs.getString("surname"));
                patient.setEmail(rs.getString("email"));
                patient.setGender(Gender.valueOf(rs.getString("gender")));
                patient.setPhoneNumber(rs.getInt("phone"));
 String dobString = rs.getString("dob");
                if (dobString != null) {
                    patient.setDob(LocalDate.parse(dobString)); // Asegúrate de que el formato sea 'yyyy-MM-dd'
                }            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error getting patient by phone.");
            e.printStackTrace();
        }
        return patient;
    }

    @Override
    public Patient getPatientByDNI(String dni) {
        Patient patient = null;
        try {
            String sql = "SELECT * FROM Patient WHERE dni = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setString(1, dni);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                patient = new Patient(rs.getInt("id"));
                patient.setDni(rs.getString("dni"));
                patient.setName(rs.getString("name"));
                patient.setSurname(rs.getString("surname"));
                patient.setEmail(rs.getString("email"));
                patient.setGender(Gender.valueOf(rs.getString("gender")));
                patient.setPhoneNumber(rs.getInt("phone"));
 String dobString = rs.getString("dob");
                if (dobString != null) {
                    patient.setDob(LocalDate.parse(dobString)); // Asegúrate de que el formato sea 'yyyy-MM-dd'
                }
            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error getting doctor by DNI.");
            e.printStackTrace();
        }
        return patient;
    }

}
