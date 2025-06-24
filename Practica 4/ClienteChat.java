package com.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteChat {
    public static void main(String[] args) {
        try {
            //Conectarse al balanceador
            System.out.println("Conectando al balanceador (localhost:8080)...");
            Socket socketBalanceador = new Socket("localhost", 8080);
            BufferedReader entradaBalanceador = new BufferedReader(
                new InputStreamReader(socketBalanceador.getInputStream()));
            
            //Recibir servidor asignado
            String respuesta = entradaBalanceador.readLine();
            System.out.println("Respuesta del balanceador: " + respuesta);
            
            if (!respuesta.startsWith("CONECTAR_A:")) {
                System.out.println("Error: " + respuesta);
                return;
            }
            
            int puertoServidor = Integer.parseInt(respuesta.split(":")[1]);
            socketBalanceador.close();
            
            //Conectarse al servidor de chat asignado
            System.out.println("Conectando al servidor de chat (localhost:" + puertoServidor + ")...");
            Socket socketChat = new Socket("localhost", puertoServidor);
            
            
            new Thread(() -> {
                try {
                    BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socketChat.getInputStream()));
                    String mensaje;
                    while ((mensaje = entrada.readLine()) != null) {
                        System.out.println("Mensaje recibido: " + mensaje);
                    }
                } catch (IOException e) {
                    System.out.println("Desconectado del servidor");
                }
            }).start();
            
            //Enviar mensajes al servidor
            PrintWriter salida = new PrintWriter(socketChat.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("Escribe mensajes (escribe 'salir' para terminar):");
            String mensaje;
            while (!(mensaje = scanner.nextLine()).equalsIgnoreCase("salir")) {
                salida.println(mensaje);
            }
            
            socketChat.close();
            System.out.println("Conexión cerrada");
            
        } catch (IOException e) {
            System.err.println("Error en cliente: " + e.getMessage());
        }
    }
}