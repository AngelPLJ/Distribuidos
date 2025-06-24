import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorBalanceo implements Runnable {
    private final int puerto;
    private final Balanceador balanceador;
    private ServerSocket serverSocket;
    private boolean ejecutando = false;

    public ServidorBalanceo(int puerto, Balanceador balanceador) {
        this.puerto = puerto;
        this.balanceador = balanceador;
    }

    public void iniciar() throws IOException {
        serverSocket = new ServerSocket(puerto);
        ejecutando = true;
        new Thread(this).start();
        System.out.println("Servidor de balanceo iniciado en puerto " + puerto);
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
                new Thread(new ManejadorBalanceo(socketCliente, balanceador)).start();
            } catch (IOException e) {
                if (ejecutando) {
                    System.err.println("Error aceptando conexión: " + e.getMessage());
                }
            }
        }
    }
}