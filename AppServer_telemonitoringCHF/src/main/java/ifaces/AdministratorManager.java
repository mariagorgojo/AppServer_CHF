/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifaces;

import pojos.Administrator;

/**
 *
 * @author carmengarciaprieto
 */
public interface AdministratorManager {
    
    public void insertAdministrator(Administrator administrator);
    public Administrator getAdministratorByDNI(String dni);
    
}
