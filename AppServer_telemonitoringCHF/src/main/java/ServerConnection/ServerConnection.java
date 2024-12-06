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
import JDBC.JDBCAdministratorManager;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Administrator;
import pojos.Disease;
import pojos.Doctor;
import pojos.Episode;
import pojos.Patient;
import pojos.Patient.Gender;
import pojos.Recording;
import pojos.Surgery;
import pojos.Symptom;

public class ServerConnection {

    //private static ConnectionManager dataBaseManager;
    // private static Connection connection;
    public static void main(String args[]) throws IOException, SQLException {

        ServerSocket serverSocket = new ServerSocket(9090);
        System.out.println("Server started, waiting for connection...");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            // Crear un hilo para manejar cada cliente
            Thread clientThread = new Thread(new ClientHandler(socket, serverSocket));
            clientThread.start();
        }

    }

    private static class ClientHandler implements Runnable {

        private Socket socket;
        private BufferedReader bufferedReader;
        private PrintWriter printWriter;
        private Connection connection;
        private ConnectionManager connectionManager; // Instancia específica para cada cliente
        private ServerSocket serverSocket; // Referencia al ServerSocket

        public ClientHandler(Socket socket, ServerSocket serverSocket) {
            this.socket = socket;
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            try {
                // Conexión a la base de datos
                //dataBaseManager = new ConnectionManager();
                connectionManager = new ConnectionManager();
                connection = connectionManager.getConnection();

                // Inicializar recursos del cliente
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(socket.getOutputStream(), true);

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.equals("LOGOUT")) { // REMEMBER: MANDAR STOP SIEMPRE , CHECK CREO Q TDO OKAY??
                        System.out.println("Client logging out"); //salir del bicle, terminar el hilo
                        break;
                    }

                    switch (line) {
                        case "REGISTER_DOCTOR":
                            handleDoctorRegister(bufferedReader, printWriter);
                            break;
                        case "REGISTER_PATIENT":
                            handlePatientRegister(bufferedReader, printWriter);
                            break;
                        case "REGISTER_ADMINISTRATOR":
                            handleAdministratorRegister(bufferedReader, printWriter);
                            break;
                        case "LOGIN_ADMINISTRATOR":
                            handleAdministratorLogin(bufferedReader, printWriter);
                            break;
                        case "LOGIN_DOCTOR":
                            handleDoctorLogin(bufferedReader, printWriter);
                            break;
                        case "LOGIN_PATIENT":
                            handlePatientLogin(bufferedReader, printWriter);
                            break;
                        case "SHUTDOWN":
                            handleShutDown(bufferedReader, printWriter);
                            break;
                        case "VIEW_DOCTOR_DETAILS":
                            handleViewDoctorDetails(bufferedReader, printWriter);
                            break;
                        case "VIEW_DOCTOR_PATIENTS":
                            handleViewDoctorPatients(bufferedReader, printWriter);
                            break;
                        case "VIEW_PATIENT_INFORMATION":
                            handleViewPatientInformation(bufferedReader, printWriter);
                            break;
                        case "VIEW_PATIENT_EPISODES":
                            handlePatientEpisodesAndDetails(bufferedReader, printWriter);
                            break;
                        case "AVAILABLE_DISEASES":
                            handleGetAvailableDiseases(printWriter);
                            break;
                        case "AVAILABLE_SURGERIES":
                            handleGetAvailableSurgeries(printWriter);
                            break;
                        case "AVAILABLE_SYMPTOMS":
                            handleGetAvailableSymptoms(printWriter);
                            break;
                        case "INSERT_EPISODE":
                            handleInsertEpisode(bufferedReader, printWriter);
                            break;
                        case "VIEW_EPISODE_ALL_DETAILS":
                            handleViewEpisodeAllDetails(bufferedReader, printWriter);
                            break;
                        case "VIEW_EPISODES_DOCTOR":
                            handleViewPatientEpisodeByDoctor(bufferedReader, printWriter);
                            break;
                        default:
                            System.err.println("Unknown command: " + line);
                            break;
                    }
                }
            } catch (IOException | SQLException e) {
                System.err.println("Error handling client: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // Liberar recursos
                releaseResources();
            }
        }
        // releaseResources(bufferedReader, printWriter, socket, serverSocket);
        // NS SI ES CORRECTO, XQ EN EL LOGIN MANDA STOP TB Y NO QUIRO CERRAR
        // CD haya + de 1 cliente ns si es correcto ponerlo asi -> cierra todos??

        // CAMBIAR
        //System.exit(0);  // VOLVER: ns si cierra el socket entero para los demas clientes ??
        /* }
                    if (line == null) {
                        System.err.println("Received null command. Client may have disconnected.");
                        return;
                    }
                    if (line.equals("REGISTER_DOCTOR")) {
                        handleDoctorRegister(bufferedReader, printWriter);
                    } else if (line.equals("REGISTER_PATIENT")) {

                        handlePatientRegister(bufferedReader, printWriter);
                    } else if (line.equals("LOGIN_DOCTOR")) {
                        handleDoctorLogin(bufferedReader, printWriter);
                    } else if (line.equals("LOGIN_PATIENT")) {
                        handlePatientLogin(bufferedReader, printWriter);
                    } else if (line.equals("VIEW_DOCTOR_DETAILS")) {
                        handleViewDoctorDetails(bufferedReader, printWriter);
                    } else if (line.equals("VIEW_DOCTOR_PATIENTS")) {
                        handleViewDoctorPatients(bufferedReader, printWriter);
                    } else if (line.equals("VIEW_PATIENT_INFORMATION")) {
                        handleViewPatientInformation(bufferedReader, printWriter);
                    } else if (line.equals("VIEW_PATIENT_EPISODES")) {
                        handlePatientEpisodesAndDetails(bufferedReader, printWriter); //used for the patient: list of episodes + select episode + select surgery/disease/sysmptom/recording(files)
                    } else if (line.equals("AVAILABLE_DISEASES")) {
                        handleGetAvailableDiseases(printWriter);
                    } else if (line.equals("AVAILABLE_SURGERIES")) {
                        handleGetAvailableSurgeries(printWriter);
                    } else if (line.equals("AVAILABLE_SYMPTOMS")) {
                        handleGetAvailableSymptoms(printWriter);
                    } else if (line.equals("INSERT_EPISODE")) {
                        handleInsertEpisode(bufferedReader, printWriter);
                    } else if (line.equals("VIEW_EPISODE_ALL_DETAILS")) { // so the doctor can see ecg/emg + the rest
                        System.out.println("TEST SERVER");
                        handleViewEpisodeAllDetails(bufferedReader, printWriter);

                    } else if (line.equals("VIEW_EPISODES_DOCTOR")) {
                        handleViewPatientEpisodeByDoctor(bufferedReader, printWriter);
                    }

                }

            } catch (IOException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
            releaseResources();
        }
        }*/
        private void releaseResources() {
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
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (IOException | SQLException e) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        private void handleDoctorRegister(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
            JDBCDoctorManager doctorManager = new JDBCDoctorManager(connection);

            String dni = bufferedReader.readLine();
            String password = bufferedReader.readLine();
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
                Doctor doctor = new Doctor(dni, password, name, surname, telephone, email);
                doctorManager.insertDoctor(doctor); // Insert doctor in the db
                printWriter.println("VALID"); // Mensaje de confirmación de registro exitoso
                System.out.println("Doctor registered on db: " + doctor);
            }

        }

        private void handlePatientRegister(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
            JDBCPatientManager patientManager = new JDBCPatientManager(connection);
            JDBCDoctorManager doctorManager = new JDBCDoctorManager(connection);

            String dni = bufferedReader.readLine();
            System.out.println("DNI received: " + dni);

            String password = bufferedReader.readLine();
            System.out.println("Password received: " + password);

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
                if (bound < 1) {
                    System.out.println("No doctor can be assigned");

                }

                try {
                    int doctor_id = generateRandomInt(bound);
                    Doctor doctor = doctorManager.getDoctorById(doctor_id);

                    //
                    if (dni == null || password == null || name == null || surname == null || telephone == null || email == null || dateOfBirth == null || gender == null) {
                        printWriter.println("INVALID");
                        System.out.println("Missing patient data. Registration failed.");
                        return;
                    }
                    Patient patient = new Patient(dni, password, name, surname, email, gender, telephone, dateOfBirth, doctor);
                    patientManager.insertPatient(patient, doctor.getId());
                    printWriter.println("VALID"); // Mensaje de confirmación de registro exitoso
                    System.out.println("Patient registered on db: " + patient);

                    //
                } catch (Exception e) {
                    System.out.println("Error registering patient: " + e.getMessage());
                    e.printStackTrace();
                    printWriter.println("ERROR"); // Informar al cliente del error
                }

            }
        }

        public static int generateRandomInt(int bound) {
            Random random = new Random();
            return random.nextInt(bound) + 1;
        }

        private void handleAdministratorRegister(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
            JDBCAdministratorManager administratorManager = new JDBCAdministratorManager(connection);

            String dni = bufferedReader.readLine();
            String password = bufferedReader.readLine();

            // Verificar si el doctor ya existe en la base de datos
            Administrator administratorFromDatabase = administratorManager.getAdministratorByDNI(dni);

            // Comprobación de si el DNI ya está registrado
            if (administratorFromDatabase != null && administratorFromDatabase.getDni().equals(dni)) {
                printWriter.println("INVALID"); // Enviar mensaje de error si el DNI ya está registrado
            } else {
                Administrator administrator = new Administrator(dni, password);
                administratorManager.insertAdministrator(administrator); // Insert doctor in the db
                printWriter.println("VALID"); // Mensaje de confirmación de registro exitoso
                System.out.println("Administrator registered on db: " + administrator);
            }

        }

        private void handleDoctorLogin(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
            JDBCDoctorManager doctorManager = new JDBCDoctorManager(connection);

            String dni = bufferedReader.readLine();
            String password = bufferedReader.readLine();

            // Verificar si el doctor existe en la base de datos
            Doctor doctorFromDatabase = doctorManager.getDoctorByDNI(dni);
            if (doctorFromDatabase != null && doctorFromDatabase.getDni().equals(dni)
                    && doctorFromDatabase.getPassword().equals(password)) {
                System.out.println(password);

                printWriter.println("VALID");
                System.out.println("Login successful for doctor: " + doctorFromDatabase.getName());

            } else {

                printWriter.println("INVALID");
                System.out.println("Invalid login attempt for doctor DNI: " + dni);
            }
        }

        private void handlePatientLogin(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
            JDBCPatientManager patientManager = new JDBCPatientManager(connection);

            String dni = bufferedReader.readLine();
            String password = bufferedReader.readLine();

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

        private void handleAdministratorLogin(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
            JDBCAdministratorManager administratorManager = new JDBCAdministratorManager(connection);

            String dni = bufferedReader.readLine();
            String password = bufferedReader.readLine();

            // falta contraseña
            Administrator administratorDatabase = administratorManager.getAdministratorByDNI(dni);
            System.out.println(administratorDatabase.toString());
            if (administratorDatabase != null && administratorDatabase.getDni().equals(dni) && administratorDatabase.getPassword().equals(password)) {
                printWriter.println("VALID");
                System.out.println("Login successful for administrator: " + administratorDatabase.getDni());
            } else {
                printWriter.println("INVALID");
                System.out.println("Invalid login attempt for administrator DNI: " + dni);
            }
        }

        private void handleShutDown(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
            try {
                System.out.println("Shutdown signal received. Closing server...");

                // Cerrar la conexión a la base de datos
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("Database connection closed.");
                }

                // Cerrar el socket del servidor
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                    System.out.println("Server socket closed.");
                }
                System.out.println("Server is shutting down now...");
                // Cerrar el servidor principal
                System.exit(0); // Termina la aplicación completamente

            } catch (SQLException e) {
                System.err.println("Error while shutting down: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    // Liberar recursos de entrada/salida
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (printWriter != null) {
                        printWriter.close();
                    }
                } catch (IOException ex) {
                    System.err.println("Error closing I/O resources: " + ex.getMessage());
                }
            }
        }

        private void handleViewDoctorDetails(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
            JDBCDoctorManager doctorManager = new JDBCDoctorManager(connection);

            String dni = bufferedReader.readLine();
            Doctor doctorFromDatabase = doctorManager.getDoctorByDNI(dni);
            System.out.println("CONNECTION SERVER");

            String doctorData = String.format("%s;%s;%s;%s;%s", doctorFromDatabase.getDni(), doctorFromDatabase.getName(), doctorFromDatabase.getSurname(),
                    doctorFromDatabase.getTelephone(), doctorFromDatabase.getEmail());
            printWriter.println(doctorData); // Send doctor data
        }

        private void handleViewDoctorPatients(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
            JDBCPatientManager patientManager = new JDBCPatientManager(connection);
            JDBCDoctorManager dM = new JDBCDoctorManager(connection);
            String dni = bufferedReader.readLine();
            Doctor doctor = dM.getDoctorByDNI(dni);
            System.out.println(doctor.getId());
            List<Patient> patients = patientManager.searchPatientsByDoctor(doctor.getId());
            System.out.println(patients);

            if (patients.size() == 0) {
                printWriter.println("EMPTY");
            } else {
                for (int i = 0; i < patients.size(); i++) {

                    Patient patient = patients.get(i);
                    String patientData = String.format("%s;%s;%s", patient.getDNI(), patient.getName(), patient.getSurname());
                    System.out.println("dentro del for" + patientData);

                    printWriter.println(patientData); // Enviar los datos del paciente
                    if (i == (patients.size() - 1)) {
                        printWriter.println("END_OF_LIST");
                    }
                }
            }
        }

        private void handleViewEpisodeAllDetails(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException, SQLException {
            try {
                if (connection == null || connection.isClosed()) {
                    System.err.println("Database connection is not available.");
                    return;
                }

                JDBCSurgeryManager surgeryManager = new JDBCSurgeryManager(connection);
                JDBCSymptomManager symptomManager = new JDBCSymptomManager(connection);
                JDBCDiseaseManager diseaseManager = new JDBCDiseaseManager(connection);
                JDBCRecordingManager recordingManager = new JDBCRecordingManager(connection);

                int selectedEpisodeId = Integer.parseInt(bufferedReader.readLine());
                System.out.println("Read: " + selectedEpisodeId);
                int patient_id = Integer.parseInt(bufferedReader.readLine());
                System.out.println("Read: " + selectedEpisodeId);

                List<Surgery> surgeries = surgeryManager.getSurgeriesByEpisode(selectedEpisodeId, patient_id);
                List<Symptom> symptoms = symptomManager.getSymptomsByEpisode(selectedEpisodeId, patient_id);
                List<Disease> diseases = diseaseManager.getDiseasesByEpisode(selectedEpisodeId, patient_id);
                List<Recording> recordings = recordingManager.getRecordingsByEpisode(selectedEpisodeId, patient_id);

                // Enviar detalles del episodio al cliente
                if (!surgeries.isEmpty()) {
                    printWriter.println("SURGERIES");
                    for (Surgery surgery : surgeries) {
                        System.out.println("Sending: " + String.format("SURGERIES;%s", surgery.getSurgery()));

                        printWriter.println(String.format("SURGERIES;%s", surgery.getSurgery()));
                    }
                }
                if (!symptoms.isEmpty()) {

                    printWriter.println("SYMPTOMS");
                    for (Symptom symptom : symptoms) {
                        System.out.println("Sending: " + String.format("SYMTOMS;%s", symptom.getSymptom()));

                        printWriter.println(String.format("SYMPTOMS;%s", symptom.getSymptom()));

                    }

                }
                if (!diseases.isEmpty()) {

                    printWriter.println("DISEASES");
                    for (Disease disease : diseases) {
                        printWriter.println(String.format("DISEASES;%s", disease.getDisease()));
                        System.out.println("Sending: " + String.format("DISEASES;%s", disease.getDisease()));

                    }
                }
                if (!recordings.isEmpty()) { // para el doctor -> indicar printWriter-> DOCTOR y le mande la data

                    printWriter.println("RECORDINGS");

                    for (Recording recording : recordings) {
                        String id = String.valueOf(recording.getId());
                        String signalPath = recording.getSignal_path();
                        ArrayList<Integer> data = recording.getData();

                        // Convertir el array de datos a un string encapsulado : [ , , , ]
                        StringBuilder dataString = new StringBuilder("[");
                        for (int i = 0; i < data.size(); i++) {
                            dataString.append(data.get(i));
                            if (i < data.size() - 1) {
                                dataString.append(",");
                            }
                        }
                        dataString.append("]"); // Cerrar el array

                        // Enviar el mensaje: ID, ruta, datos del array
                        String message = String.format("RECORDINGS;%s;%s;%s", id, signalPath, dataString.toString());
                        System.out.println("Sending: " + message);
                        printWriter.println(message); // Enviar mensaje completo
                    }
                }
                printWriter.println("END_OF_LIST"); // Marcar el fin de los detalles
            } catch (Exception e) {
                System.err.println("Error handling VIEW_EPISODE_ALL_DETAILS: " + e.getMessage());
                e.printStackTrace();
            }

        }

        private void handlePatientEpisodesAndDetails(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException, SQLException {
            if (connection == null || connection.isClosed()) {
                System.err.println("Database connection is not available.");
                return;
            }

            JDBCEpisodeManager episodeManager = new JDBCEpisodeManager(connection);
            JDBCPatientManager patientManager = new JDBCPatientManager(connection);
            JDBCSurgeryManager surgeryManager = new JDBCSurgeryManager(connection);
            JDBCSymptomManager symptomManager = new JDBCSymptomManager(connection);
            JDBCDiseaseManager diseaseManager = new JDBCDiseaseManager(connection);
            JDBCRecordingManager recordingManager = new JDBCRecordingManager(connection);

            String dni = bufferedReader.readLine();

            // Obtener el paciente desde la base de datos
            Patient patientFromDatabase = patientManager.getPatientByDNI(dni);

            // Obtener episodios del paciente
            ArrayList<Episode> episodes = episodeManager.getEpisodesByPatient(patientFromDatabase.getId());

            // Enviar la lista de episodios al cliente
            for (Episode episode : episodes) {
                printWriter.println(String.format("%d;%s", episode.getId(), episode.getDate()));
            }
            printWriter.println("END_OF_LIST"); // Marcar el fin de la lista

            if (bufferedReader.readLine().equals("VIEW_EPISODE_DETAILS")) {
                // Leer el ID del episodio seleccionado por el cliente
                String selectedEpisodeIdString = bufferedReader.readLine();

                String patient_dni = bufferedReader.readLine();

                Patient patientFromDatabase2 = patientManager.getPatientByDNI(patient_dni);
                int patient_id = patientFromDatabase2.getId();

                int selectedEpisodeId = Integer.parseInt(selectedEpisodeIdString);

                List<Surgery> surgeries = surgeryManager.getSurgeriesByEpisode(selectedEpisodeId, patient_id);
                List<Symptom> symptoms = symptomManager.getSymptomsByEpisode(selectedEpisodeId, patient_id);
                List<Disease> diseases = diseaseManager.getDiseasesByEpisode(selectedEpisodeId, patient_id);
                List<Recording> recordings = recordingManager.getRecordingsByEpisode(selectedEpisodeId, patient_id);

                // Enviar detalles del episodio al cliente
                if (!surgeries.isEmpty()) {
                    printWriter.println("SURGERIES");
                    for (Surgery surgery : surgeries) {
                        System.out.println("Sending: " + String.format("SURGERIES;%s", surgery.getSurgery()));

                        printWriter.println(String.format("SURGERIES;%s", surgery.getSurgery()));
                    }
                }
                if (!symptoms.isEmpty()) {

                    printWriter.println("SYMPTOMS");
                    for (Symptom symptom : symptoms) {
                        System.out.println("Sending: " + String.format("SYMTOMS;%s", symptom.getSymptom()));

                        printWriter.println(String.format("SYMPTOMS;%s", symptom.getSymptom()));

                    }

                }
                if (!diseases.isEmpty()) {

                    printWriter.println("DISEASES");
                    for (Disease disease : diseases) {
                        printWriter.println(String.format("DISEASES;%s", disease.getDisease()));

                    }
                }
                if (!recordings.isEmpty()) {

                    printWriter.println("RECORDINGS");
                    for (Recording recording : recordings) {

                        System.out.println("Sending: " + String.format("RECORDINGS;%d;%s", recording.getId(), recording.getSignal_path()));

                        printWriter.println(String.format("RECORDINGS;%d;%s", recording.getId(), recording.getSignal_path())); // Enviar ID y ruta

                    }
                }
                printWriter.println("END_OF_DETAILS"); // Marcar el fin de los detalles
            }
        }

        private void handleViewPatientEpisodeByDoctor(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException, SQLException {

            if (connection == null || connection.isClosed()) {
                System.err.println("Database connection is not available.");
                return;
            }

            JDBCEpisodeManager episodeManager = new JDBCEpisodeManager(connection);
            JDBCPatientManager patientManager = new JDBCPatientManager(connection);
            JDBCSurgeryManager surgeryManager = new JDBCSurgeryManager(connection);
            JDBCSymptomManager symptomManager = new JDBCSymptomManager(connection);
            JDBCDiseaseManager diseaseManager = new JDBCDiseaseManager(connection);
            JDBCRecordingManager recordingManager = new JDBCRecordingManager(connection);

            String dni = bufferedReader.readLine();

            // Obtener el paciente desde la base de datos
            Patient patientFromDatabase = patientManager.getPatientByDNI(dni);

            // Obtener episodios del paciente
            ArrayList<Episode> episodes = episodeManager.getEpisodesByPatient(patientFromDatabase.getId());

            // Enviar la lista de episodios al cliente
            for (Episode episode : episodes) {
                printWriter.println(String.format("%d;%s", episode.getId(), episode.getDate()));
            }
            printWriter.println("END_OF_LIST"); // Marcar el fin de la lista

        }

        private void handleViewPatientInformation(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {

            JDBCPatientManager patientManager = new JDBCPatientManager(connection);
            String dni = bufferedReader.readLine();
            Patient patientFromDatabase = patientManager.getPatientByDNI(dni);

            // printWriter.println(patientFromDatabase.toString());        
            String patientData = String.format("%s;%s;%s;%s;%s;%s;%s;%s", patientFromDatabase.getId(), patientFromDatabase.getDNI(), patientFromDatabase.getName(), patientFromDatabase.getSurname(),
                    patientFromDatabase.getEmail(), patientFromDatabase.getGender().toString(), patientFromDatabase.getPhoneNumber(), patientFromDatabase.getDob().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            printWriter.println(patientData); // Enviar los datos del paciente
        }

        private void handleGetAvailableDiseases(PrintWriter printWriter) {
            JDBCDiseaseManager diseaseManager = new JDBCDiseaseManager(connection);

            try {
                List<Disease> diseases = diseaseManager.getAllDiseases(); // Obtener todas las enfermedades
                for (Disease disease : diseases) {
                    printWriter.println(disease.getDisease()); // Enviar cada enfermedad al cliente
                }
                printWriter.println("END_OF_LIST"); // Marcar el final de la lista
                printWriter.flush();
            } catch (Exception e) {
                printWriter.println("ERROR: " + e.getMessage());
                printWriter.flush();
                e.printStackTrace();
            }

        }

        private void handleGetAvailableSurgeries(PrintWriter printWriter) {
            JDBCSurgeryManager surgeryManager = new JDBCSurgeryManager(connection);

            try {
                List<Surgery> surgeries = surgeryManager.getAllSurgeries(); // Obtener todas las enfermedades
                for (Surgery surgery : surgeries) {
                    printWriter.println(surgery.getSurgery()); // Enviar cada enfermedad al cliente
                }
                printWriter.println("END_OF_LIST"); // Marcar el final de la lista
                printWriter.flush();
            } catch (Exception e) {
                printWriter.println("ERROR: " + e.getMessage());
                printWriter.flush();
                e.printStackTrace();
            }

        }

        private void handleGetAvailableSymptoms(PrintWriter printWriter) {
            JDBCSymptomManager symptomManager = new JDBCSymptomManager(connection);

            try {
                List<Symptom> symptoms = symptomManager.getAllSymptoms(); // Obtener todas las enfermedades
                for (Symptom symptom : symptoms) {
                    printWriter.println(symptom.getSymptom()); // Enviar cada enfermedad al cliente
                }
                printWriter.println("END_OF_LIST"); // Marcar el final de la lista
                //printWriter.flush();
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                // printWriter.flush();
                e.printStackTrace();
            }

        }

        private void handleInsertEpisode(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
            try {
                // Managers para interactuar con la base de datos
                JDBCEpisodeManager episodeManager = new JDBCEpisodeManager(connection);
                JDBCSymptomManager symptomManager = new JDBCSymptomManager(connection);
                JDBCRecordingManager recordingManager = new JDBCRecordingManager(connection);

                String line = bufferedReader.readLine();
                if (line.equals("CREATE_EPISODE")) {
                    // Crear un nuevo episodio
                    int patientId = Integer.parseInt(bufferedReader.readLine());
                    LocalDateTime episodeDate = LocalDateTime.parse(bufferedReader.readLine());

                    // Crear el objeto episodio
                    Episode episode = new Episode();
                    episode.setPatient_id(patientId);
                    episode.setDate(episodeDate);

                    // Insertar el episodio y obtener su ID
                    episodeManager.insertEpisode(episode);
                    int episodeId = episodeManager.getEpisodeId(episodeDate, patientId);

                    // Procesar síntomas y grabaciones
                    while (!(line = bufferedReader.readLine()).equals("END_OF_EPISODE")) {
                        String[] parts = line.split("\\|");
                        switch (parts[0]) {
                            case "SYMPTOM":
                                String symptomName = parts[1];
                                int symptomId = symptomManager.getSymptomId(symptomName);
                                if (symptomId == -1) {
                                    symptomManager.insertSymptom(symptomName);
                                    symptomId = symptomManager.getSymptomId(symptomName);
                                }
                                symptomManager.assignSymptomToEpisode(symptomId, episodeId);
                                break;

                            case "RECORDING":
                                try {
                                    // Validar longitud de parts
                                    if (parts.length < 5) {
                                        System.err.println("Invalid RECORDING data: insufficient parts");
                                        printWriter.println("ERROR: Invalid RECORDING data");
                                        return;
                                    }

                                    // Extraer datos
                                    Recording.Type type = Recording.Type.valueOf(parts[1]);
                                    LocalDateTime recordingDate = LocalDateTime.parse(parts[2]);
                                    String signalPath = parts[3];
                                    String dataString = parts[4]; // Datos separados por comas

                                    // Validar dataString
                                    if (dataString == null || dataString.isEmpty()) {
                                        System.err.println("Error: dataString is null or empty");
                                        printWriter.println("ERROR: dataString is null or empty");
                                        return;
                                    }

                                    // Procesar datos
                                    ArrayList<Integer> data = new ArrayList<>();
                                    try {
                                        String[] dataPoints = dataString.split(",");
                                        for (String dataPoint : dataPoints) {
                                            data.add(Integer.parseInt(dataPoint.trim()));
                                        }
                                    } catch (NumberFormatException e) {
                                        System.err.println("Error parsing dataString: " + e.getMessage());
                                        printWriter.println("ERROR: Invalid data format in dataString");
                                        return;
                                    }

                                    System.out.println("Raw data string: " + dataString);
                                    System.out.println("Parsed data points: " + data);

                                    // Crear y guardar la grabación
                                    Recording recording = new Recording(type, recordingDate, signalPath, data, episodeId);
                                    recordingManager.insertRecording(recording);

                                } catch (Exception e) {
                                    System.err.println("Error processing RECORDING: " + e.getMessage());
                                    printWriter.println("ERROR: " + e.getMessage());
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                throw new IllegalArgumentException("Tipo desconocido: " + parts[0]);
                        }
                    }

                    printWriter.println(episodeId); // Enviar el ID del episodio al cliente
                    printWriter.flush();
                } else if (line.equals("UPDATE_EPISODE")) {
                    // Actualizar un episodio existente con enfermedades y cirugías
                    JDBCDiseaseManager diseaseManager = new JDBCDiseaseManager(connection);
                    JDBCSurgeryManager surgeryManager = new JDBCSurgeryManager(connection);

                    int episodeId = Integer.parseInt(bufferedReader.readLine());
                    while (!(line = bufferedReader.readLine()).equals("END_OF_UPDATE")) {
                        String[] parts = line.split("\\|");
                        switch (parts[0]) {
                            case "DISEASE":
                                String diseaseName = parts[1];
                                int diseaseId = diseaseManager.getDiseaseId(diseaseName);
                                if (diseaseId == -1) {
                                    diseaseManager.insertDisease(diseaseName);
                                    diseaseId = diseaseManager.getDiseaseId(diseaseName);
                                }
                                diseaseManager.assignDiseaseToEpisode(diseaseId, episodeId);
                                break;

                            case "SURGERY":
                                String surgeryName = parts[1];
                                int surgeryId = surgeryManager.getSurgeryId(surgeryName);
                                if (surgeryId == -1) {
                                    surgeryManager.insertSurgery(surgeryName);
                                    surgeryId = surgeryManager.getSurgeryId(surgeryName);
                                }
                                surgeryManager.assignSurgeryToEpisode(surgeryId, episodeId);
                                break;

                            default:
                                throw new IllegalArgumentException("Tipo desconocido: " + parts[0]);
                        }
                    }

                    printWriter.println("SUCCESS");
                    printWriter.flush();
                }
            } catch (Exception e) {
                printWriter.println("ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        }
        /* private void handleInsertEpisode(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
            try {
                // Crear managers para acceder a la base de datos
                JDBCEpisodeManager episodeManager = new JDBCEpisodeManager(connection);
                JDBCSymptomManager symptomManager = new JDBCSymptomManager(connection);
                JDBCSurgeryManager surgeryManager = new JDBCSurgeryManager(connection);
                JDBCRecordingManager recordingManager = new JDBCRecordingManager(connection);
                JDBCDiseaseManager diseaseManager = new JDBCDiseaseManager(connection);

                int patientId = Integer.parseInt(bufferedReader.readLine());
                LocalDateTime episodeDate = LocalDateTime.parse(bufferedReader.readLine());

                // Crear el objeto episodio
                Episode episode = new Episode();
                episode.setPatient_id(patientId);
                episode.setDate(episodeDate);

                // Insertar el episodio y obtener el ID generado
                episodeManager.insertEpisode(episode);
                System.out.println("EPISODE INSERTED IN THE SERVER" + episode);
                // asignacion disease, surgey, symtom
                int episodeId = episodeManager.getEpisodeId(episodeDate, patientId);

                System.out.println("episode Id" + episodeId);
                // Leer elementos asociados al episodio
                String line;
                while (!((line = bufferedReader.readLine()).equals("END_OF_EPISODE"))) {
                    System.out.println("line:" + line);

                    String[] parts = line.split("\\|");
                    switch (parts[0]) {
                        case "DISEASE":
                            String diseaseName = parts[1];
                            /*
                        for (int i = 0; i < diseaseManager.getAllDiseases().size(); i++) {
                            if (!diseaseName.equals(diseaseManager.getAllDiseases().get(i).getDisease())) {
                                diseaseManager.insertDisease(diseaseName); // Inserta o ignora si ya existe
                            }
                        }
                        int diseaseId = diseaseManager.getDiseaseId(diseaseName); // Recupera el ID
                        diseaseManager.assignDiseaseToEpisode(diseaseId, episodeId); // Asigna al episodio
                             

                            // Recuperar ID si existe, o insertar y luego recuperar
                            int diseaseId = diseaseManager.getDiseaseId(diseaseName);
                            System.out.println("DISEASE ID " + diseaseId);
                            System.out.println("EPISODE ID " + episodeId);
                            if (diseaseId == -1) { // -1 o un valor especial significa que no existe
                                diseaseManager.insertDisease(diseaseName);
                                diseaseId = diseaseManager.getDiseaseId(diseaseName);
                            }
                            // Asociar solo si no está ya asociado con el episodio
                            //if (!diseaseManager.isDiseaseAssociatedWithEpisode(diseaseId, episodeId)) {
                            diseaseManager.assignDiseaseToEpisode(diseaseId, episodeId);
                            //}
                            break;

                        case "SYMPTOM":
                            String symptomName = parts[1];
                            /*for (int i = 0; i < symptomManager.getAllSymptoms().size(); i++) {
                            if (!symptomName.equals(symptomManager.getAllSymptoms().get(i).getSymptom())) {
                                symptomManager.insertSymptom(symptomName); // Inserta o ignora si ya existe
                            }
                        }
                        int symptomId = symptomManager.getSymptomId(symptomName); // Recupera el ID
                        symptomManager.assignSymptomToEpisode(symptomId, episodeId); // Asigna al episodio

                            int symptomId = symptomManager.getSymptomId(symptomName);
                            System.out.println("Symptom ID " + symptomId);
                            System.out.println("EPISODE ID " + episodeId);
                            if (symptomId == -1) {
                                symptomManager.insertSymptom(symptomName);
                                symptomId = symptomManager.getSymptomId(symptomName);
                            }
                            // if (!symptomManager.isSymptomAssociatedWithEpisode(symptomId, episodeId)) {
                            symptomManager.assignSymptomToEpisode(symptomId, episodeId);
                            //}

                            break;

                        case "SURGERY":
                            String surgeryName = parts[1];
                            /*for (int i = 0; i < surgeryManager.getAllSurgeries().size(); i++) {
                            if (!surgeryName.equals(surgeryManager.getAllSurgeries().get(i).getSurgery())) {
                                surgeryManager.insertSurgery(surgeryName); // Inserta o ignora si ya existe
                            }
                        }
                        int surgeryId = surgeryManager.getSurgeryId(surgeryName); // Recupera el ID
                        surgeryManager.assignSurgeryToEpisode(surgeryId, episodeId); // Asigna al episodio

                            int surgeryId = surgeryManager.getSurgeryId(surgeryName);
                            System.out.println("Surgery ID " + surgeryId);
                            System.out.println("EPISODE ID " + episodeId);
                            if (surgeryId == -1) {
                                surgeryManager.insertSurgery(surgeryName);
                                surgeryId = surgeryManager.getSurgeryId(surgeryName);
                            }
                            //  if (!surgeryManager.isSurgeryAssociatedWithEpisode(surgeryId, episodeId)) {
                            surgeryManager.assignSurgeryToEpisode(surgeryId, episodeId);
                            //}

                            break;

                        case "RECORDING":
                            try {
                                // Validar longitud de parts
                                if (parts.length < 5) {
                                    System.err.println("Invalid RECORDING data: insufficient parts");
                                    printWriter.println("ERROR: Invalid RECORDING data");
                                    return;
                                }

                                // Extraer datos
                                Recording.Type type = Recording.Type.valueOf(parts[1]);
                                LocalDateTime recordingDate = LocalDateTime.parse(parts[2]);
                                String signalPath = parts[3];
                                String dataString = parts[4]; // Datos separados por comas

                                // Validar dataString
                                if (dataString == null || dataString.isEmpty()) {
                                    System.err.println("Error: dataString is null or empty");
                                    printWriter.println("ERROR: dataString is null or empty");
                                    return;
                                }

                                // Procesar datos
                                ArrayList<Integer> data = new ArrayList<>();
                                try {
                                    String[] dataPoints = dataString.split(",");
                                    for (String dataPoint : dataPoints) {
                                        data.add(Integer.parseInt(dataPoint.trim()));
                                    }
                                } catch (NumberFormatException e) {
                                    System.err.println("Error parsing dataString: " + e.getMessage());
                                    printWriter.println("ERROR: Invalid data format in dataString");
                                    return;
                                }

                                System.out.println("Raw data string: " + dataString);
                                System.out.println("Parsed data points: " + data);

                                // Crear y guardar la grabación
                                Recording recording = new Recording(type, recordingDate, signalPath, data, episodeId);
                                recordingManager.insertRecording(recording);

                            } catch (Exception e) {
                                System.err.println("Error processing RECORDING: " + e.getMessage());
                                printWriter.println("ERROR: " + e.getMessage());
                                e.printStackTrace();
                            }
                            break;

                        default:
                            throw new IllegalArgumentException("Unknown element type: " + parts[0]);
                    }
                }

                // Confirmar éxito al cliente
                printWriter.println("SUCCESS");
                printWriter.flush();
            } catch (Exception e) {
                printWriter.println("ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        }*/

    }
}
