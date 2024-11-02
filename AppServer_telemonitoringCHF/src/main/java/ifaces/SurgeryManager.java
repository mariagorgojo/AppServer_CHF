/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifaces;

/**
 *
 * @author carmengarciaprieto
 */
public class SurgeryManager {
            public void insertSurgery(Surgery surgery);
            public List<Surgery> getSurgeriesByEpisode(int episode_id);
            public List<Surgery> getSurgeriesByPatient(int patient_id);
            public Surgery getSurgeryById(int surgery_id);
            public void assignSurgeryToEpisode(int surgery_id, int episode_id);
}
