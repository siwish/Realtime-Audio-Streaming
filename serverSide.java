package cn;

import javax.sound.sampled.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class serverSide {

    public static void main(String[] args) {
        try {
            int serverPort = 12345;
            System.out.println("Server is starting...");
            
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Server is listening on port " + serverPort);
            System.out.println("Waiting for a client to connect...\n");
            
            // Accept client connection
            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
            
            // Configure audio format
            AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            
            // Open and start the SourceDataLine for audio output
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            System.out.println("Speakers opened \nReceiving and playing audio...");
            
            // Set up a buffer for audio data
            byte[] buffer = new byte[1024];
            InputStream inputStream = socket.getInputStream();

            // Continuously read and play audio data
            while (true) {
                int bytesRead = inputStream.read(buffer, 0, buffer.length);
                if (bytesRead == -1) {
                    break; // Exit the loop if the client has closed the connection
                }
                sourceDataLine.write(buffer, 0, bytesRead);
            }

            // Close resources
            sourceDataLine.drain();
            sourceDataLine.close();
            socket.close();
            serverSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}