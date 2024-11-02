/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifaces;

/**
 *
 * @author carmengarciaprieto
 */
public class DiseaseManager {
            public void insertDisease(Disease disease);
            public List<Disease> getDiseasesByEpisode(int episode_id);
            public List<Disease> getDiseasesByPatient(int patient_id);
            public Disease getDiseaseById(int disease_id);
            public void assignDiseaseToEpisode(int disease_id, int episode_id);
}
