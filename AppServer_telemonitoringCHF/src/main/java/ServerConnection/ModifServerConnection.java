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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Doctor;

import java.net.ServerSocket;


import java.io.PrintWriter;



public class ModifServerConnection {

    // Mapa para simular base de datos de Doctores (para validación de login) -> CARMEN MODIFICAR 
    private static final Map<String, Doctor> doctorDatabase = new HashMap<>();

    public static void main(String args[]) throws IOException {
        
        ServerSocket serverSocket = new ServerSocket(9001);
        System.out.println("Server started, waiting for connection...");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            // Configuración de entradas y salidas para la comunicación con el cliente
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("STOP")) {
                    System.out.println("Stopping the server");
                    releaseResources(bufferedReader, printWriter, socket, serverSocket);
                    System.exit(0);
                }

                // Si el comando es para registro de doctor
                if (line.equals("REGISTER")) {
                    handleRegister(bufferedReader, printWriter);
                }
                // Si el comando es para validación de login
                else if (line.equals("LOGIN")) {
                    handleLogin(bufferedReader, printWriter);
                }
            }
        }
    }

    // Método para manejar el registro de un doctor
    private static void handleRegister(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        // Leer datos para crear el objeto Doctor
        String dni = bufferedReader.readLine();
        String name = bufferedReader.readLine();
        String surname = bufferedReader.readLine();
        Integer telephone = Integer.parseInt(bufferedReader.readLine());
        String email = bufferedReader.readLine();

        // Crear objeto Doctor con los datos recibidos
        Doctor doctor = new Doctor(dni, name, surname, telephone, email);

        // Guardar el doctor en la "base de datos"  -> CARMEN CHECKEAR || MODIFICAR 
        doctorDatabase.put(dni, doctor);
        System.out.println("Doctor registered: " + doctor);

        // Responder al cliente con un mensaje de éxito
        printWriter.println("Doctor registered successfully.");
    }

    // Método para manejar la validación de login
    private static void handleLogin(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        // Leer DNI y contraseña
        String dni = bufferedReader.readLine();
        String password = bufferedReader.readLine(); //  la contraseña sería cifrada + adelante

        // Buscar al doctor en la "base de datos" -> CARMEN CHECKEAR || MODIFICAR 
        Doctor doctor = doctorDatabase.get(dni);

        if (doctor != null && doctor.getDni().equals(dni)) { // VOLVER
            // Si el servidor responde con "VALID", las credenciales son correctas 
            // IMPORTANTE CHECKEAR, Y EN ESTE ORDEN:
            //1. VALIDAR si el DNI corresponde con el de un doctor 
            //2.VALIDAR SI DNI de DOCTOR corresponde con la password
            // Aquí puedes agregar la validación de la contraseña, si es necesario
            // En este ejemplo, se asume que el DNI es suficiente para "validar" el login
            printWriter.println("VALID");
            System.out.println("Login successful for doctor: " + doctor.getName());
        } else {
            printWriter.println("INVALID");
            System.out.println("Invalid login attempt for DNI: " + dni);
        }
    }

    // Método para liberar los recursos
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
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

    

