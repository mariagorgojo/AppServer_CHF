/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifaces;

import java.util.ArrayList;
import pojos.Recording;
import pojos.Recording.Type;

/**
 *
 * @author carmengarciaprieto
 */
public interface RecordingManager {
        public void insertRecording(Recording recording);
    	public ArrayList<Recording> getRecordingsByEpisode(int episode_id, int patient_id);
        public ArrayList<Recording> getRecordingsByType(Type type, int episode_id); 
        public Recording getRecordingById(int recording_id); 
}


