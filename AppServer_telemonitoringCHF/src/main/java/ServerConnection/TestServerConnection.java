/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Doctor;

/**
 *
 * @author martaguzman
 */
public class TestServerConnection {
    
    
    public static void main(String args[]) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9001);
        System.out.println("Server started, waiting for connection...");

        Socket socket = serverSocket.accept();
        System.out.println("Connection client created");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Receiving Doctor Information:\n");

        // Leer datos para crear el objeto Doctor
        String dni = bufferedReader.readLine();
        String name = bufferedReader.readLine();
        String surname = bufferedReader.readLine();
        Integer telephone = Integer.parseInt(bufferedReader.readLine());
        String email = bufferedReader.readLine();

        // Crear objeto Doctor con los datos recibidos
        Doctor doctor = new Doctor(dni, name, surname, telephone, email);
        System.out.println("Doctor object created: " + doctor); // no tiene sentido q imprima patients=[]

        // Esperar el comando "Stop" para detener el servidor
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.toLowerCase().contains("stop")) {
                System.out.println("Stopping the server");
                releaseResources(bufferedReader, socket, serverSocket);
                System.exit(0);
            }
            System.out.println(line);
        }
    }

    private static void releaseResources(BufferedReader bufferedReader, Socket socket, ServerSocket serverSocket) {
        try {
            bufferedReader.close();
        } catch (IOException ex) {
            Logger.getLogger(TestServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(TestServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(TestServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
