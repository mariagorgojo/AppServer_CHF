/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifaces;

/**
 *
 * @author carmengarciaprieto
 */
public class SymptomManager {
            public void insertSymptom(Symptom symptom);
            public List<Symptom> getSymptomsByEpisode(int episode_id);
            public List<Symptom> getSymptomsByPatient(int patient_id);
            public Symptom getSymptomById(int symptom_id);
            public void assignSymptomToEpisode(int symptom_id, int episode_id);
}
