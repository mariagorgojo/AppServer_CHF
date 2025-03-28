/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifaces;

import java.util.ArrayList;
import pojos.Symptom;

/**
 *
 * @author carmengarciaprieto
 */
public interface SymptomManager {
            public void insertSymptom(String symptom);
            public ArrayList<Symptom> getSymptomsByEpisode(int episode_id, int patient_id);
            public ArrayList<Symptom> getSymptomsByPatient(String patient_id);
            public String getSymptomById(int symptom_id);
            public void assignSymptomToEpisode(int symptom_id, int episode_id);
            public ArrayList<Symptom> getAllSymptoms();
            public int getSymptomId(String symptom); 
            //  nuevo
            public boolean isSymptomAssociatedWithEpisode(int symptomId, int episodeId);

            
}
