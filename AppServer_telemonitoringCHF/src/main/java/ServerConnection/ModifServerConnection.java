/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerConnection;

/**
 *
 * @author martaguzman
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Doctor;
import pojos.Patient;
import pojos.Patient.Gender;




public class ModifServerConnection {
    
     // carmen modificar : bd ficcticia para q no me diera error
    private static final Map<String, Doctor> doctorDatabase = new HashMap<>(); // carmen modificar 
    private static final Map<String, Patient> patientDatabase = new HashMap<>();

    public static void main(String args[]) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9001);
        System.out.println("Server started, waiting for connection...");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("STOP")) { // REMEMBER: MANDAR STOP SIEMPRE , CHECK CREO Q TDO OKAY??
                    System.out.println("Stopping the server");
                    releaseResources(bufferedReader, printWriter, socket, serverSocket);
                    // NS SI ES CORRECTO, XQ EN EL LOGIN MANDA STOP TB Y NO QUIRO CERRAR
                    // CD haya + de 1 cliente ns si es correcto ponerlo asi -> cierra todos??
                    System.exit(0);  // VOLVER: ns si cierra el socket entero para los demas clientes ??
                    
                }

                if (line.equals("REGISTER_DOCTOR")) {
                    handleDoctorRegister(bufferedReader, printWriter);
                } else if (line.equals("REGISTER_PATIENT")) {
                    handlePatientRegister(bufferedReader, printWriter);
                } else if (line.equals("LOGIN_DOCTOR")) {
                    handleDoctorLogin(bufferedReader, printWriter);
                } else if (line.equals("LOGIN_PATIENT")) {
                    handlePatientLogin(bufferedReader, printWriter);
                }
            }
        }
    }

    private static void handleDoctorRegister(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        
        String dni = bufferedReader.readLine();
        String password = bufferedReader.readLine(); // Encript ...+ adelante
        String name = bufferedReader.readLine();
        String surname = bufferedReader.readLine();
        Integer telephone = Integer.parseInt(bufferedReader.readLine());
        String email = bufferedReader.readLine();

        Doctor doctorFromDatabase = doctorDatabase.get(dni); // Carmen modificar. 

        // Comprobación de si el DNI ya está registrado
        if (doctorFromDatabase != null && doctorFromDatabase.getDni().equals(dni)) { 
            printWriter.println("INVALID"); // Enviar mensaje de error si el DNI ya está registrado
        } else {
            Doctor doctor = new Doctor(dni, name, surname, telephone, email);
            doctorDatabase.put(dni, doctor); // Carmen modificar
            printWriter.println("VALID"); // Mensaje de confirmación de registro exitoso
            System.out.println("Doctor registered on db: " + doctor);
        }

    }

    private static void handlePatientRegister(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        String dni = bufferedReader.readLine();
        String password = bufferedReader.readLine(); // Password received securely
        String name = bufferedReader.readLine();
        String surname = bufferedReader.readLine();
        Integer telephone = Integer.parseInt(bufferedReader.readLine());
        String email = bufferedReader.readLine();

        String genderInput = bufferedReader.readLine();
        Gender gender = Gender.valueOf(genderInput.toUpperCase());

        String dateOfBirthInput = bufferedReader.readLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateOfBirth = LocalDate.parse(dateOfBirthInput, formatter);

        
         Patient patientFromDatabase = patientDatabase.get(dni); // Carmen modificar. 

        // Comprobación de si el DNI ya está registrado
        if (patientFromDatabase != null && patientFromDatabase.getDni().equals(dni)) {
            printWriter.println("INVALID"); // Mensaje de error si el DNI ya está registrado
        } else {
            Patient patient = new Patient(dni, name, surname, email, gender, telephone, dateOfBirth);
            patientDatabase.put(dni, patient); // Carmen modificar
            printWriter.println("VALID"); // Mensaje de confirmación de registro exitoso
            System.out.println("Doctor registered on db: " + patient);
        }
    }

    private static void handleDoctorLogin(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        String dni = bufferedReader.readLine();
        String password = bufferedReader.readLine(); // Encrypte + adelante
        
        // CARMEN MODIFICAR PARA QUE: VALID SI ESTÁ REGISTRADO, SI ES DOCTOR Y SI COINCIDE LA PASSWORD CON EL DNI !!

        Doctor doctor = doctorDatabase.get(dni); // Carmen modificar
        if (doctor != null && doctor.getDni().equals(dni)) { // AÑADIR LAS VERIFICACIONES MEMCIONADAS
            printWriter.println("VALID"); 
        } else {
            printWriter.println("INVALID");
            System.out.println("Invalid login attempt for doctor DNI: " + dni);
        }
    }

    private static void handlePatientLogin(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        String dni = bufferedReader.readLine();
        String password = bufferedReader.readLine(); // Encrypte + ADELANTE

        // CARMEN MODIFICAR PARA QUE: VALID SI ESTÁ REGISTRADO, SI ES DOCTOR Y SI COINCIDE LA PASSWORD CON EL DNI !!
        Patient patient = patientDatabase.get(dni); // Carmen modificar.
        if (patient != null && patient.getDni().equals(dni)) {
            printWriter.println("VALID");
            System.out.println("Login successful for patient: " + patient.getName());
        } else {
            printWriter.println("INVALID");
            System.out.println("Invalid login attempt for patient DNI: " + dni);
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





    

