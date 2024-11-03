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
    	public ArrayList<Recording> getRecordingsByEpisode(int episode_id);
        public ArrayList<Recording> getRecordingsByType(Type type, int episode_id); 
        public Recording getRecordingById(int recording_id); 
}

//Cuando uses JDBC, deberás convertir el enum a un String antes de almacenarlo en la base de datos
//String typeAsString = recording.getType().toString();

//Para recuperar el enum desde la base de datos, conviertes el String de nuevo al enum:
//Type type = Type.valueOf(resultSet.getString("type"));
