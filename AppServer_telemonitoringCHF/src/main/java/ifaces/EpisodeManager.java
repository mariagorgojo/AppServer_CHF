/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifaces;

import java.time.LocalDate;
import java.util.ArrayList;
import pojos.Episode;

/**
 *
 * @author carmengarciaprieto
 */
public interface EpisodeManager {
        public void insertEpisode(Episode episode);
    	public ArrayList<Episode> getEpisodesByPatient(int patient_id); 
    	public ArrayList<Episode> getEpisodesByDate(int patient_id, LocalDate date); //aqui poner una funcion que acceda a los episodes del paciente y una vez ahi a los de la fecha de esee paciente
        public Episode getEpisodeById(int patient_id,int episode_id);
}
