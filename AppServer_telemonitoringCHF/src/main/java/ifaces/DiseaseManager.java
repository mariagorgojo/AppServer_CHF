/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifaces;

import java.util.ArrayList;
import pojos.Disease;

/**
 *
 * @author carmengarciaprieto
 */
public interface DiseaseManager {
            public void insertDisease(Disease disease);
            public ArrayList<Disease> getDiseasesByEpisode(int episode_id);
            public ArrayList<Disease> getDiseasesByPatient(int patient_id);
            public String getDiseaseById(int disease_id);
            public void assignDiseaseToEpisode(int disease_id, int episode_id);
}
