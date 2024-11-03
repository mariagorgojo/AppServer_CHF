/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import ifaces.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author carmengarciaprieto
 */
public class Main {
	private static BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static DoctorManager doctorMan;
	private static PatientManager patientMan;
	private static EpisodeManager episodeMan;
	private static RecordingManager recordingMan;
	private static SurgeryManager surgeryMan;
	private static DiseaseManager diseaseMan;
	private static SymptomManager symptomMan;

	public static void main(String[] args) {

		ConnectionManager conMan = new ConnectionManager();
		doctorMan = new JDBCDoctorManager(conMan.getConnection());
		patientMan = new JDBCPatientManager(conMan.getConnection());
		episodeMan = new JDBCEpisodeManager(conMan.getConnection());
		RecordingMan = new JDBCRecordingManager(conMan.getConnection());
		surgeryMan = new JDBCSurgeryManager(conMan.getConnection());
                diseaseMan = new JDBCDiseaseManager(conMan.getConnection());
		symptomMan = new JDBCSymptomManager(conMan.getConnection());
        }
        
}

