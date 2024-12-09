/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import ifaces.AdministratorManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pojos.Administrator;
import pojos.Doctor;

/**
 *
 * @author carmengarciaprieto
 */
public class JDBCAdministratorManager implements AdministratorManager{
      private Connection c;

	public JDBCAdministratorManager(Connection c) {
		this.c = c;

	}

        
	@Override
    public void insertAdministrator(Administrator administrator) {
        try {
            String sql = "INSERT INTO Administrator (dni, password) VALUES (?,?)";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setString(1, administrator.getDni());
            prep.setString(2, administrator.getPassword());
            prep.executeUpdate();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error inserting administrator.");
            e.printStackTrace();
        }
    }

    @Override
    public Administrator getAdministratorByDNI(String dni) {
        Administrator administrator = null;
        try {
            String sql = "SELECT * FROM Administrator WHERE dni = ?";
            PreparedStatement prep = c.prepareStatement(sql);
            prep.setString(1, dni);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                administrator = new Administrator(rs.getInt("id"));
                administrator.setDni(rs.getString("dni"));
                administrator.setPassword(rs.getString("password"));

            }
            rs.close();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Error getting administrator by DNI.");
            e.printStackTrace();
        }
        return administrator;
    }

}
