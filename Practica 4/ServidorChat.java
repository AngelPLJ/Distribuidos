import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServidorChat implements Runnable {
    private final int puerto;
    private final Set<ManejadorCliente> clientes = new HashSet<>();
    private ServerSocket serverSocket;
    private boolean ejecutando = false;
    private int contadorConexiones = 0;

    public ServidorChat(int puerto) {
        this.puerto = puerto;
    }
    public int getPuerto() {
        return puerto;
    }
    public void iniciar() throws IOException {
        serverSocket = new ServerSocket(puerto);
        ejecutando = true;
        new Thread(this).start();
        System.out.println("Servidor de chat iniciado en puerto " + puerto);
    }

    public void detener() throws IOException {
        ejecutando = false;
        serverSocket.close();
    }

    @Override
    public void run() {
        while (ejecutando) {
            try {
                Socket socketCliente = serverSocket.accept();
                contadorConexiones++;
                System.out.println("Nueva conexión aceptada. Total: " + contadorConexiones);
                
                ManejadorCliente manejador = new ManejadorCliente(socketCliente, this);
                clientes.add(manejador);
                new Thread(manejador).start();
            } catch (IOException e) {
                if (ejecutando) {
                    System.err.println("Error aceptando conexión: " + e.getMessage());
                }
            }
        }
    }

    public void broadcast(String mensaje, ManejadorCliente origen) {
        for (ManejadorCliente cliente : clientes) {
            if (cliente != origen) {
                cliente.enviarMensaje(mensaje);
            }
        }
    }

    public void removerCliente(ManejadorCliente cliente) {
        clientes.remove(cliente);
        contadorConexiones--;
    }

    public int getContadorConexiones() {
        return contadorConexiones;
    }
}