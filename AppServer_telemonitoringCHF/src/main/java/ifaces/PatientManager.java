/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifaces;

/**
 *
 * @author carmengarciaprieto
 */
public class PatientManager {
    public void insertPatient(Patient patient, Doctor doctor);
    public List<Patient> searchPatientsByDoctor(int d_id);
    public Patient getPatient(int p_id);
    public List<Patient> searchPatientByName(String name);
    public Patient getPatientByEmail(String email); 
    public Patient getPatientByPhone(Integer phone);
}
