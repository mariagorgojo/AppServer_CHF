/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

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
			Statement s = c.createStatement();
			String sql = "INSERT INTO Doctor (id, dni, name, surname, phone, email) VALUES ('" + doctor.getId() + "', '"
					+ doctor.getDni() "," + doctor.getName() + "', '" + doctor.getSurname() + "','" + doctor.getPhone() + "," doctor.getEmail()+"')";
			s.executeUpdate(sql);
			s.close();
		} catch (SQLException e) {
			System.out.println("Database exception.");
			e.printStackTrace();
		}

	}
}
