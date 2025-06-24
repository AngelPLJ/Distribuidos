package Practica2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class Concurrencia {
    
    private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    private static void crearcliente(){
        Client c = new Client("127.0.0.1", 5000);
    }
    private static void crearServidor(){
        Server s = new Server(5000);
    }
    public static void main(String[] args) {
        Future<?> servidor = executor.submit(() -> {
            crearServidor();
        });
        Future<?> cliente = executor.submit(() -> {
            crearcliente();
        });

        try {
            servidor.get();
            cliente.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}
