/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerConnection;

/**
 *
 * @author martaguzman
 */
import JDBC.ConnectionManager;
import JDBC.JDBCDiseaseManager;
import JDBC.JDBCDoctorManager;
import JDBC.JDBCEpisodeManager;
import JDBC.JDBCPatientManager;
import JDBC.JDBCRecordingManager;
import JDBC.JDBCSurgeryManager;
import JDBC.JDBCSymptomManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.sql.Connection;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Disease;
import pojos.Doctor;
import pojos.Episode;
import pojos.Patient;
import pojos.Patient.Gender;
import pojos.Recording;
import pojos.Surgery;
import pojos.Symptom;

public class ModifServerConnection {

    // carmen modificar : bd ficcticia para q no me diera error
    //private static final Map<String, Doctor> doctorDatabase = new HashMap<>(); // carmen modificar 
    //private static final Map<String, Patient> patientDatabase = new HashMap<>();
    // Maria
    // Atributes that allow the database management and connection
    private static ConnectionManager dataBaseManager;
    private static Connection connection;

    public static void main(String args[]) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9001);
        System.out.println("Server started, waiting for connection...");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            // Maria 
            // Inicializar el ConnectionManager para la base de datos
            dataBaseManager = new ConnectionManager();
            connection = dataBaseManager.getConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("STOP")) { // REMEMBER: MANDAR STOP SIEMPRE , CHECK CREO Q TDO OKAY??
                    System.out.println("Stopping the server");
                    releaseResources(bufferedReader, printWriter, socket, serverSocket);
                    // NS SI ES CORRECTO, XQ EN EL LOGIN MANDA STOP TB Y NO QUIRO CERRAR
                    // CD haya + de 1 cliente ns si es correcto ponerlo asi -> cierra todos??

                    // CAMBIAR
                    //System.exit(0);  // VOLVER: ns si cierra el socket entero para los demas clientes ??
                }

                if (line.equals("REGISTER_DOCTOR")) {
                    handleDoctorRegister(bufferedReader, printWriter);
                } else if (line.equals("REGISTER_PATIENT")) {
                    // FALTA SABER QUE HACER CON EL DOCTOR DEL INSERT PATIENT
                    handlePatientRegister(bufferedReader, printWriter);
                } else if (line.equals("LOGIN_DOCTOR")) {
                    handleDoctorLogin(bufferedReader, printWriter);
                } else if (line.equals("LOGIN_PATIENT")) {
                    handlePatientLogin(bufferedReader, printWriter);
                } else if (line.equals("VIEW_DOCTOR_DETAILS")) {
                    handleViewDoctorDetails(bufferedReader, printWriter);
                } else if (line.equals("VIEW_DOCTOR_PATIENTS")) {
                    //System.out.println("I'm in view doctor_patirnts before function");
                    handleViewDoctorPatients(bufferedReader, printWriter);
                    // System.out.println("I'm in view doctor_patirnts after function");
                } else if (line.equals("VIEW_PATIENT_INFORMATION")) {
                    handleViewPatientInformation(bufferedReader, printWriter);
                } else if (line.equals("VIEW_PATIENT_EPISODES")) {
                    handlePatientEpisodesAndDetails(bufferedReader, printWriter); //list of episodes + select episode + select recording
                }else if (line.equals("GET_AVAILABLE_DISEASES")){
                    handleGetAvailableDiseases(printWriter);                    
                }else if (line.equals("INSERT_NEW_DISEASE")){
                    handleGetAvailableDiseases(printWriter);                    
                handleInsertNewDisease(bufferedReader, printWriter);
                }else if(line.equals("INSERT_EPISODE")){
                    handleInsertEpisode(bufferedReader, printWriter); 
                }
            }
        }
    }

    private static void handleDoctorRegister(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        // manager + the conncection with the database
        JDBCDoctorManager doctorManager = new JDBCDoctorManager(connection);

        String dni = bufferedReader.readLine();
        String password = bufferedReader.readLine(); // Encript ...+ adelante
        String name = bufferedReader.readLine();
        String surname = bufferedReader.readLine();
        Integer telephone = Integer.parseInt(bufferedReader.readLine());
        String email = bufferedReader.readLine();

        // Verificar si el doctor ya existe en la base de datos
        Doctor doctorFromDatabase = doctorManager.getDoctorByDNI(dni);

        // Comprobación de si el DNI ya está registrado
        if (doctorFromDatabase != null && doctorFromDatabase.getDni().equals(dni)) {
            printWriter.println("INVALID"); // Enviar mensaje de error si el DNI ya está registrado
        } else {
            Doctor doctor = new Doctor(dni,password, name, surname, telephone, email);
            doctorManager.insertDoctor(doctor); // Insert doctor in the db
            printWriter.println("VALID"); // Mensaje de confirmación de registro exitoso
            System.out.println("Doctor registered on db: " + doctor);
        }

    }

    private static void handlePatientRegister(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        JDBCPatientManager patientManager = new JDBCPatientManager(connection);
        JDBCDoctorManager doctorManager = new JDBCDoctorManager(connection);

        String dni = bufferedReader.readLine();
        String password = bufferedReader.readLine(); // Password received securely
        String name = bufferedReader.readLine();
        String surname = bufferedReader.readLine();
        Integer telephone = Integer.parseInt(bufferedReader.readLine());
        String email = bufferedReader.readLine();
        String dateOfBirthInput = bufferedReader.readLine();
        String genderInput = bufferedReader.readLine();

        Gender gender = Gender.valueOf(genderInput.toUpperCase());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateOfBirth = LocalDate.parse(dateOfBirthInput, formatter);
        // Verificar si el patient ya existe en la base de datos
        Patient patientFromDatabase = patientManager.getPatientByDNI(dni);

// Comprobación de si el DNI ya está registrado
        if (patientFromDatabase != null && patientFromDatabase.getDNI().equals(dni)) {
            System.out.println("ERROR");
            printWriter.println("INVALID"); // Mensaje de error si el DNI ya está registrado
        } else {
            int bound = doctorManager.countNumberOfDoctors();
            //System.out.println(bound);
            int doctor_id = generateRandomInt(bound);
            Doctor doctor = doctorManager.getDoctorById(doctor_id);
            Patient patient = new Patient(dni,password, name, surname, email, gender, telephone, dateOfBirth, doctor);
            patientManager.insertPatient(patient, doctor);
            printWriter.println("VALID"); // Mensaje de confirmación de registro exitoso
            System.out.println("Patient registered on db: " + patient);
        }
    }

    public static int generateRandomInt(int bound) {
        Random random = new Random();
        return random.nextInt(bound) + 1;
    }

    private static void handleDoctorLogin(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        JDBCDoctorManager doctorManager = new JDBCDoctorManager(connection);

        String dni = bufferedReader.readLine();
        String password = bufferedReader.readLine(); // Encrypte + adelante
        
        // FALTA QUE COMPRUEBE TAMBIÉN LA PASSWORD!! Y COMPROBAR SI EL DNI ES DE UN PACIENTE O DEL DOCTOR!!
        // Verificar si el doctor existe en la base de datos
        Doctor doctorFromDatabase = doctorManager.getDoctorByDNI(dni);
        if (doctorFromDatabase != null && doctorFromDatabase.getDni().equals(dni) && 
                doctorFromDatabase.getPassword().equals(password)) { // AÑADIR LAS VERIFICACIONES MEMCIONADAS
                    System.out.println(password);

            printWriter.println("VALID");
            System.out.println("Login successful for doctor: " + doctorFromDatabase.getName());

        } else {
            
            printWriter.println("INVALID");
            System.out.println("Invalid login attempt for doctor DNI: " + dni);
        }
    }

    private static void handlePatientLogin(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        JDBCPatientManager patientManager = new JDBCPatientManager(connection);

        String dni = bufferedReader.readLine();
        String password = bufferedReader.readLine(); // Encrypte + ADELANTE

        // VALID SI ESTÁ REGISTRADO Y SI COINCIDE LA PASSWORD CON EL DNI !!
        // falta contraseña
        Patient patientDatabase = patientManager.getPatientByDNI(dni);
        System.out.println(patientDatabase.toString());
        if (patientDatabase != null && patientDatabase.getDNI().equals(dni) && patientDatabase.getPassword().equals(password)) {
            printWriter.println("VALID");
            System.out.println("Login successful for patient: " + patientDatabase.getName());
        } else {
            printWriter.println("INVALID");
            System.out.println("Invalid login attempt for patient DNI: " + dni);
        }
    }

    private static void handleViewDoctorDetails(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        JDBCDoctorManager doctorManager = new JDBCDoctorManager(connection);

        String dni = bufferedReader.readLine();
        Doctor doctorFromDatabase = doctorManager.getDoctorByDNI(dni);

        printWriter.println(doctorFromDatabase.toString());

    }

    private static void handleViewDoctorPatients(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        JDBCPatientManager patientManager = new JDBCPatientManager(connection);
        JDBCDoctorManager dM = new JDBCDoctorManager(connection);
        String dni = bufferedReader.readLine();
        Doctor doctor = dM.getDoctorByDNI(dni);
        System.out.println(doctor.getId());
        List<Patient> patients = patientManager.searchPatientsByDoctor(doctor.getId());
        System.out.println(patients);
        
        if (patients.size()==0){
            printWriter.println("EMPTY");
        }else{
        for (int i = 0; i < patients.size(); i++) {

            Patient patient = patients.get(i); // Obtén el paciente en la posición i
            //System.out.println("dentro del for");
            String patientData = String.format("%s,%s,%s", patient.getDNI(), patient.getName(), patient.getSurname());
            printWriter.println(patientData); // Enviar los datos del paciente
            if (i == (patients.size() - 1)) {
                printWriter.println("END_OF_LIST");
            }
        }
        }
    }

    private static void handlePatientEpisodesAndDetails(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        // Crear los gestores necesarios
        JDBCEpisodeManager episodeManager = new JDBCEpisodeManager(connection);
        JDBCPatientManager patientManager = new JDBCPatientManager(connection);
        JDBCSurgeryManager surgeryManager = new JDBCSurgeryManager(connection);
        JDBCSymptomManager symptomManager = new JDBCSymptomManager(connection);
        JDBCDiseaseManager diseaseManager = new JDBCDiseaseManager(connection);
        JDBCRecordingManager recordingManager = new JDBCRecordingManager(connection);

        // Leer el DNI del paciente
        String dni = bufferedReader.readLine();

        // Obtener el paciente desde la base de datos
        Patient patientFromDatabase = patientManager.getPatientByDNI(dni);

        if (patientFromDatabase == null) {
            printWriter.println("ERROR: Patient not found");
            return;
        }

        // Obtener episodios del paciente
        ArrayList<Episode> episodes = episodeManager.getEpisodesByPatient(patientFromDatabase.getId());

        if (episodes.isEmpty()) {
            printWriter.println("ERROR: No episodes found for the patient");
            return;
        }

        // Enviar la lista de episodios al cliente
        for (Episode episode : episodes) {
            printWriter.println(String.format("%d,%s", episode.getId(), episode.getDate()));
        }
        printWriter.println("END_OF_LIST"); // Marcar el fin de la lista

        // Leer el ID del episodio seleccionado por el cliente
        String selectedEpisodeIdString = bufferedReader.readLine();

        if (selectedEpisodeIdString == null || selectedEpisodeIdString.isEmpty()) {
            printWriter.println("ERROR: No episode selected");
            return;
        }

        Integer selectedEpisodeId;
        try {
            selectedEpisodeId = Integer.valueOf(selectedEpisodeIdString);
        } catch (NumberFormatException e) {
            printWriter.println("ERROR: Invalid episode ID");
            return;
        }

        // Verificar que el ID seleccionado pertenece a la lista de episodios
        boolean validSelection = episodes.stream().anyMatch(e -> e.getId().equals(selectedEpisodeId));

        if (!validSelection) {
            printWriter.println("ERROR: Episode ID not found in the list");
            return;
        }

        // Obtener detalles del episodio seleccionado
        List<Surgery> surgeries = surgeryManager.getSurgeriesByEpisode(selectedEpisodeId);
        List<Symptom> symptoms = symptomManager.getSymptomsByEpisode(selectedEpisodeId);
        List<Disease> diseases = diseaseManager.getDiseasesByEpisode(selectedEpisodeId);
        List<Recording> recordings = recordingManager.getRecordingsByEpisode(selectedEpisodeId);

        // Enviar detalles del episodio al cliente
        printWriter.println("SURGERIES");
        for (Surgery surgery : surgeries) {
            printWriter.println(String.format("%s", surgery.getSurgery()));
        }
        printWriter.println("SYMPTOMS");
        for (Symptom symptom : symptoms) {
            printWriter.println(String.format("%s", symptom.getSymptom()));
        }
        printWriter.println("DISEASES");
        for (Disease disease : diseases) {
            printWriter.println(String.format("%s", disease.getDisease()));
        }
        printWriter.println("RECORDINGS");
        for (Recording recording : recordings) {
            printWriter.println(String.format("%d,%s", recording.getId(), recording.getSignal_path())); // Enviar ID y ruta
        }
        printWriter.println("END_OF_DETAILS"); // Marcar el fin de los detalles

        // Leer el ID del recording seleccionado por el cliente
        String selectedRecordingIdString = bufferedReader.readLine();

        if (selectedRecordingIdString == null || selectedRecordingIdString.isEmpty()) {
            printWriter.println("ERROR: No recording selected");
            return;
        }

        Integer selectedRecordingId;
        try {
            selectedRecordingId = Integer.valueOf(selectedRecordingIdString);
        } catch (NumberFormatException e) {
            printWriter.println("ERROR: Invalid recording ID");
            return;
        }

        // Verificar que el recording pertenece al episodio seleccionado
        boolean validRecording = recordings.stream().anyMatch(r -> r.getId().equals(selectedRecordingId));

        if (!validRecording) {
            printWriter.println("ERROR: Recording ID not found in the episode");
            return;
        }

        // Obtener detalles del recording seleccionado
        Recording selectedRecording = recordings.stream()
                .filter(r -> r.getId().equals(selectedRecordingId))
                .findFirst()
                .orElse(null);

        if (selectedRecording == null) {
            printWriter.println("ERROR: Recording details not found");
            return;
        }

        // Enviar detalles del recording al cliente
        printWriter.println("RECORDING_DETAILS");
        printWriter.println(String.format("ID: %d", selectedRecording.getId()));
        printWriter.println(String.format("Type: %s", selectedRecording.getType()));
        printWriter.println(String.format("Duration: %d seconds", selectedRecording.getDuration()));
        printWriter.println(String.format("Date: %s", selectedRecording.getDate()));
        printWriter.println(String.format("Signal Path: %s", selectedRecording.getSignal_path()));
        printWriter.println(String.format("Episode ID: %d", selectedRecording.getEpisode_id()));
        printWriter.println("DATA");
        for (Integer dataPoint : selectedRecording.getData()) {
            printWriter.println(dataPoint); // Enviar cada punto de datos de la grabación
        }
        printWriter.println("END_OF_RECORDING_DETAILS"); // Marcar el fin de los detalles del recording
    }

    /*private static void handleViewPatientEpisode(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException{
        JDBCSurgeryManager surgeryManager = new JDBCSurgeryManager(connection);
        JDBCSymptomManager symptomManager = new JDBCSymptomManager(connection);
        JDBCDiseaseManager diseaseManager = new JDBCDiseaseManager(connection);
        JDBCRecordingManager recordingManager = new JDBCRecordingManager(connection);

        Integer episode_id = Integer.valueOf(bufferedReader.readLine()); 
        
        List<Surgery> surgeries = surgeryManager.getSurgeriesByEpisode(episode_id); 
        List<Symptom> symptoms = symptomManager.getSymptomsByEpisode(episode_id);
        List<Disease> diseases = diseaseManager.getDiseasesByEpisode(episode_id);
        List<Recording> recordings =recordingManager.getRecordingsByEpisode(episode_id);
        
        for (int i = 0; i < surgeries.size(); i++) {           
            Surgery surgery = surgeries.get(i);
            String surgeryData = String.format("%s", surgery.getType());
            printWriter.println(surgeryData); 
        }
        for (int i = 0; i < symptoms.size(); i++) {           
            Symptom symptom = symptoms.get(i); 
            String symptomData = String.format("%s", symptom.getType());
            printWriter.println(symptomData); 
        }
        for (int i = 0; i < diseases.size(); i++) {           
            Disease disease = diseases.get(i); 
            String diseaseData = String.format("%s", disease.getDisease());
            printWriter.println(diseaseData); 
        } 
        
        for (int i = 0; i < recordings.size(); i++) {           
            Recording recording = recordings.get(i); 
            String recordingData = String.format("%s", recording.getSignal_path());
            printWriter.println(recordingData); 
        } 
        printWriter.println("END_OF_LIST");
    }*/
    private static void handleViewPatientInformation(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        JDBCPatientManager patientManager = new JDBCPatientManager(connection);
        String dni = bufferedReader.readLine();
        Patient patientFromDatabase = patientManager.getPatientByDNI(dni);

        // printWriter.println(patientFromDatabase.toString());        
        String patientData = String.format("%s,%s,%s,%s,%s,%s,%s", patientFromDatabase.getDNI(), patientFromDatabase.getName(), patientFromDatabase.getSurname(),
                patientFromDatabase.getEmail(), patientFromDatabase.getGender().toString(), patientFromDatabase.getPhoneNumber(), patientFromDatabase.getDob().toString());
        printWriter.println(patientData); // Enviar los datos del paciente
    }

    // ME ESTOY LIANDO, IR A DOCTOR MENU (198) --> no se puede simplemente pasarlos episodios del paciente?
    // enviar todos los episodios que tiene ese paciente en concreto
    /*private static void handlePatientEpisodes(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        JDBCEpisodeManager episodeManager = new JDBCEpisodeManager(connection);
        JDBCPatientManager patientManager = new JDBCPatientManager(connection);
        String dni = bufferedReader.readLine();
        Patient patientFromDatabase = patientManager.getPatientByDNI(dni);
        ArrayList<Episode> episodes = episodeManager.getEpisodesByPatient(patientFromDatabase.getId());

        for (int i = 0; i < episodes.size(); i++) {
            Episode episode = episodes.get(i);
            String episodeData = String.format("%s", episode);
            printWriter.println(episodeData);
        }

    }
*/
    
    private static void handleGetAvailableDiseases(PrintWriter printWriter) {
    JDBCDiseaseManager diseaseManager = new JDBCDiseaseManager(connection);

    try {
        List<Disease> diseases = diseaseManager.getAllDiseases(); // Obtener todas las enfermedades
        for (Disease disease : diseases) {
            printWriter.println(disease.getDisease()); // Enviar cada enfermedad al cliente
        }
        printWriter.println("END_OF_LIST"); // Marcar el final de la lista
    } catch (Exception e) {
        printWriter.println("ERROR: " + e.getMessage());
        e.printStackTrace();
    }
}

    
   private static void handleInsertNewDisease(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        String diseaseName = bufferedReader.readLine();
    try {
        JDBCDiseaseManager diseaseManager = new JDBCDiseaseManager(connection);
        diseaseManager.insertDisease(diseaseName);
        printWriter.println("SUCCESS");
    } catch (Exception e) {
        printWriter.println("ERROR");
        e.printStackTrace();
    }
   
   }
    
    private static void handleInsertEpisode(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
    try {
        // Crear managers para acceder a la base de datos
        JDBCEpisodeManager episodeManager = new JDBCEpisodeManager(connection);
        JDBCSymptomManager symptomManager = new JDBCSymptomManager(connection);
        JDBCSurgeryManager surgeryManager = new JDBCSurgeryManager(connection);
        JDBCRecordingManager recordingManager = new JDBCRecordingManager(connection);
        JDBCDiseaseManager diseaseManager = new JDBCDiseaseManager(connection);

        // **Paso 1: Enviar datos disponibles al cliente**
        printWriter.println("AVAILABLE_DISEASES");
        List<Disease> diseases = diseaseManager.getAllDiseases();
        for (Disease disease : diseases) {
            printWriter.println(disease.getDisease());
        }
        printWriter.println("END_OF_LIST");

        printWriter.println("AVAILABLE_SYMPTOMS");
        List<Symptom> symptoms = symptomManager.getAllSymptoms();
        for (Symptom symptom : symptoms) {
            printWriter.println(symptom.getSymptom());
        }
        printWriter.println("END_OF_LIST");

        printWriter.println("AVAILABLE_SURGERIES");
        List<Surgery> surgeries = surgeryManager.getAllSurgeries();
        for (Surgery surgery : surgeries) {
            printWriter.println(surgery.getSurgery());
        }
        printWriter.println("END_OF_LIST");

        // **Paso 2: Leer los datos del episodio enviados por el cliente**
        int patientId = Integer.parseInt(bufferedReader.readLine());
        LocalDate episodeDate = LocalDate.parse(bufferedReader.readLine());

        // Crear el objeto episodio
        Episode episode = new Episode();
        episode.setPatient_id(patientId);
        episode.setDate(episodeDate);

        // Insertar el episodio y obtener el ID generado
        episodeManager.insertEpisode(episode);
        int episodeId = episode.getId();

        // Leer elementos asociados al episodio
        String line;
        while (!(line = bufferedReader.readLine()).equals("END_OF_EPISODE")) {
            String[] parts = line.split("\\|");
            switch (parts[0]) {
                case "DISEASE":
                    String diseaseName = parts[1];
                    diseaseManager.insertDisease(diseaseName); // Inserta o ignora si ya existe
                    int diseaseId = diseaseManager.getDiseaseId(diseaseName); // Recupera el ID
                    diseaseManager.assignDiseaseToEpisode(diseaseId, episodeId); // Asigna al episodio
                    break;

                case "SYMPTOM":
                    String symptomName = parts[1];
                    symptomManager.insertSymptom(symptomName); // Inserta o ignora si ya existe
                    int symptomId = symptomManager.getSymptomId(symptomName); // Recupera el ID
                    symptomManager.assignSymptomToEpisode(symptomId, episodeId); // Asigna al episodio
                    break;

                case "SURGERY":
                    String surgeryName = parts[1];
                    surgeryManager.insertSurgery(surgeryName); // Inserta o ignora si ya existe
                    int surgeryId = surgeryManager.getSurgeryId(surgeryName); // Recupera el ID
                    surgeryManager.assignSurgeryToEpisode(surgeryId, episodeId); // Asigna al episodio
                    break;

                case "RECORDING":
                    Recording.Type type = Recording.Type.valueOf(parts[1]);
                    int duration = Integer.parseInt(parts[2]);
                    LocalDate recordingDate = LocalDate.parse(parts[3]);
                    String signalPath = parts[4];

                    // Procesar datos de grabación
                    ArrayList<Integer> data = new ArrayList<>();
                    String dataPoint;
                    while (!(dataPoint = bufferedReader.readLine()).equals("END_OF_RECORDING_DATA")) {
                        data.add(Integer.parseInt(dataPoint));
                    }

                    // Crear y guardar la grabación
                    Recording recording = new Recording(type, duration, recordingDate, signalPath, data, episodeId);
                    recordingManager.insertRecording(recording);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown element type: " + parts[0]);
            }
        }

        // Confirmar éxito al cliente
        printWriter.println("SUCCESS");
    } catch (Exception e) {
        printWriter.println("ERROR: " + e.getMessage());
        e.printStackTrace();
    }
}
    private static void releaseResources(BufferedReader bufferedReader, PrintWriter printWriter, Socket socket, ServerSocket serverSocket) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (printWriter != null) {
                printWriter.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ModifServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

}
