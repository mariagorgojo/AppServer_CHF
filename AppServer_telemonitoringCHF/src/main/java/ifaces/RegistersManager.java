/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifaces;

/**
 *
 * @author carmengarciaprieto
 */
public class RecordingManager {
        public void insertRecording(Recording recording);
    	public List<Recording> getRecordingsByEpisode(int episode_id);
        public List<Recording> getRecordingsByType(int patient_id, String type); 
        public Recording getRecordingById(int recording_id); 
}
