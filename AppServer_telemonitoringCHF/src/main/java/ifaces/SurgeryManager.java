/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifaces;

import java.util.ArrayList;
import pojos.Surgery;

/**
 *
 * @author carmengarciaprieto
 */
public interface SurgeryManager {
            public void insertSurgery(String surgery);
            public ArrayList<Surgery> getSurgeriesByEpisode(int episode_id);
            public ArrayList<Surgery> getSurgeriesByPatient(String patient_id);
            public String getSurgeryById(int surgery_id);
            public void assignSurgeryToEpisode(int surgery_id, int episode_id);
}
