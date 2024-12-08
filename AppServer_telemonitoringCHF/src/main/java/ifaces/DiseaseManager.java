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
            public void insertDisease(String disease);
            public ArrayList<Disease> getDiseasesByEpisode(int episode_id, int patient_id);
            public ArrayList<Disease> getDiseasesByPatient(String patient_id);
            public ArrayList<Disease> getPreviousDiseasesByPatient(int patient_id); //nueva previous diseases
            public String getDiseaseById(int disease_id);
            public void assignDiseaseToEpisode(int disease_id, int episode_id);
            public void assignDiseaseToPatient(int disease_id, int patient_id); //nueva previous diseases
            public ArrayList <Disease> getAllDiseases();
            public int getDiseaseId(String disease);
            
            // nuevas
            public boolean isDiseaseAssociatedWithEpisode(int diseaseId, int episodeId);
            public boolean isDiseaseAssociatedWithPatient(int diseaseId, int patientId); //nueva previous diseases //necesaria????

}
//for commit