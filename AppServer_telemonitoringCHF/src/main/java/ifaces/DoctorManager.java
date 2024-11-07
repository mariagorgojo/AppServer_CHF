/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifaces;
import pojos.Doctor;
import pojos.Patient;

/**
 *
 * @author carmengarciaprieto
 */
public interface DoctorManager {
    
    public void insertDoctor(Doctor doctor); 
    //public Doctor getDoctor(String d_id);
    public Doctor getDoctorByEmail(String email);
    public int countNumberOfDoctors();
    public Doctor getDoctorById(int id);
    public int getRandomId();
    public Doctor getDoctorByPatient(Patient patient);  //se puede poner con int patient_id
    public Doctor getDoctorByPhone(Integer phone);
}
