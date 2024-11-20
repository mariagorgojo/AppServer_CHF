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
                }else if (line.equals("VIEW_DOCTOR_DETAILS")){
                    handleViewDoctorDetails(bufferedReader, printWriter);
                }else if(line.equals("VIEW_DOCTOR_PATIENTS")){
                    handleViewDoctorPatients(bufferedReader, printWriter);
                }else if (line.equals("VIEW_PATIENT_INFORMATION")){
                    handleViewPatientInformation(bufferedReader, printWriter); 
                }else if(line.equals("VIEW_PATIENT_EPISODE")){
                    handleViewPatientEpisode(bufferedReader, printWriter);
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
            Doctor doctor = new Doctor(dni, name, surname, telephone, email);
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
        Patient patientFromDatabase =patientManager.getPatientByDNI(dni);


// Comprobación de si el DNI ya está registrado
        if (patientFromDatabase != null && patientFromDatabase.getDni().equals(dni)) {
            System.out.println("ERROR");
            printWriter.println("INVALID"); // Mensaje de error si el DNI ya está registrado
        } else {
            int bound = doctorManager.countNumberOfDoctors();
            //System.out.println(bound);
            int doctor_id = generateRandomInt(bound);
            Doctor doctor = doctorManager.getDoctorById(doctor_id);
            Patient patient = new Patient(dni, name, surname, email, gender, telephone, dateOfBirth, doctor);
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
        if (doctorFromDatabase != null && doctorFromDatabase.getDni().equals(dni)) { // AÑADIR LAS VERIFICACIONES MEMCIONADAS
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
        if (patientDatabase != null && patientDatabase.getDni().equals(dni)) {
            printWriter.println("VALID");
            System.out.println("Login successful for patient: " + patientDatabase.getName());
        } else {
            printWriter.println("INVALID");
            System.out.println("Invalid login attempt for patient DNI: " + dni);
        }
    }
    
      private static void handleViewDoctorDetails(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException{
        JDBCDoctorManager doctorManager = new JDBCDoctorManager(connection);

        String dni = bufferedReader.readLine();
        Doctor doctorFromDatabase = doctorManager.getDoctorByDNI(dni);
        
        printWriter.println(doctorFromDatabase.toString());
        
    }
      private static void handleViewDoctorPatients(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException{
        JDBCPatientManager patientManager = new JDBCPatientManager(connection);

        String dni = bufferedReader.readLine();
        List<Patient> patients = patientManager.searchPatientsByDoctor(dni);
       
        for (int i = 0; i < patients.size(); i++) {
            
            Patient patient = patients.get(i); // Obtén el paciente en la posición i
            String patientData = String.format("%s,%s,%s", patient.getDni(), patient.getName(), patient.getSurname());
            printWriter.println(patientData); // Enviar los datos del paciente
        }
    }
      
    private static void handleViewPatientEpisode(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException{
        JDBCSurgeryManager surgeryManager = new JDBCSurgeryManager(connection);
        JDBCSymptomManager symptomManager = new JDBCSymptomManager(connection);
        JDBCDiseaseManager diseaseManager = new JDBCDiseaseManager(connection);

        Integer episode_id = Integer.valueOf(bufferedReader.readLine()); 
        
        List<Surgery> surgeries = surgeryManager.getSurgeriesByEpisode(episode_id); 
        List<Symptom> symptoms = symptomManager.getSymptomsByEpisode(episode_id);
        List<Disease> diseases = diseaseManager.getDiseasesByEpisode(episode_id);
        
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
        printWriter.println("END_OF_LIST");
    }
    private static void handleViewPatientInformation(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException{
        JDBCPatientManager patientManager = new JDBCPatientManager(connection);
        String dni = bufferedReader.readLine();
        Patient patientFromDatabase = patientManager.getPatientByDNI(dni);
        
       // printWriter.println(patientFromDatabase.toString());        
      
            String patientData = String.format("%s,%s,%s,%s,%s,%s,%s", patientFromDatabase.getDni(), patientFromDatabase.getName(), patientFromDatabase.getSurname(), 
                    patientFromDatabase.getEmail(), patientFromDatabase.getGender().toString(),patientFromDatabase.getPhoneNumber(), patientFromDatabase.getDob().toString() );
            printWriter.println(patientData); // Enviar los datos del paciente
            
            
            // ME ESTOY LIANDO, IR A DOCTOR MENU (198) --> no se puede simplemente pasarlos episodios del paciente?
            // enviar todos los episodios que tiene ese paciente en concreto
        JDBCEpisodeManager episodeManager = new JDBCEpisodeManager(connection);
      
        ArrayList<Episode> episodes = episodeManager.getEpisodesByPatient(patientFromDatabase.getDni());
                
        for (int i = 0; i < episodes.size(); i++) {           
            Episode episode = episodes.get(i);
            String episodeData = String.format("%s", episode.getDate());
            printWriter.println(episodeData); 
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
