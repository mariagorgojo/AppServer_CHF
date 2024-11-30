/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifaces;

import java.util.ArrayList;
import pojos.Doctor;
import pojos.Patient;

/**
 *
 * @author carmengarciaprieto
 */
public interface PatientManager {
    public void insertPatient(Patient patient, int doctor);
    public ArrayList<Patient> searchPatientsByDoctor(Integer id);
    public Patient getPatientById(int p_id);
    public ArrayList<Patient> searchPatientByName(String name);
    public Patient getPatientByEmail(String email); 
    public Patient getPatientByPhone(Integer phone);
    public Patient getPatientByDNI(String dni);
}
