import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ManejadorCliente implements Runnable {
    private final Socket socket;
    private final ServidorChat servidor;
    private PrintWriter salida;

    public ManejadorCliente(Socket socket, ServidorChat servidor) {
        this.socket = socket;
        this.servidor = servidor;
    }

    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(
             new InputStreamReader(socket.getInputStream()))) {
            
            salida = new PrintWriter(socket.getOutputStream(), true);
            
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("Mensaje recibido: " + mensaje);
                servidor.broadcast(mensaje, this);
            }
        } catch (IOException e) {
            System.err.println("Error en manejador de cliente: " + e.getMessage());
        } finally {
            servidor.removerCliente(this);
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error cerrando socket: " + e.getMessage());
            }
        }
    }

    public void enviarMensaje(String mensaje) {
        salida.println(mensaje);
    }
}