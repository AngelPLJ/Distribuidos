import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

class ManejadorBalanceo implements Runnable {
    private final Socket socket;
    private final Balanceador balanceador;

    public ManejadorBalanceo(Socket socket, Balanceador balanceador) {
        this.socket = socket;
        this.balanceador = balanceador;
    }

    @Override
    public void run() {
        try (PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {
            
            ServidorChat servidor = balanceador.asignarServidor();
            int puertoServidor = servidor.getPuerto();
            
            salida.println("CONECTAR_A:" + puertoServidor);
            System.out.println("Cliente asignado a servidor en puerto " + puertoServidor);
            
        } catch (IOException e) {
            System.err.println("Error en manejador de balanceo: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error cerrando socket: " + e.getMessage());
            }
        }
    }
}