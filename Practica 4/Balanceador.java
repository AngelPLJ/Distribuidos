
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Balanceador {
    private final List<ServidorChat> servidores = new ArrayList<>();
    private final Lock lock = new ReentrantLock();
    private int indice = 0;

    public void agregarServidor(ServidorChat servidor) {
        lock.lock();
        try {
            servidores.add(servidor);
        } finally {
            lock.unlock();
        }
    }

    // Método renombrado a asignarServidor 
    public ServidorChat asignarServidor() {
        lock.lock();
        try {
            if (servidores.isEmpty()) {
                throw new IllegalStateException("No hay servidores disponibles");
            }
            
            // Round Robin simple
            ServidorChat servidor = servidores.get(indice);
            indice = (indice + 1) % servidores.size();
            return servidor;
        } finally {
            lock.unlock();
        }
    }

    // Método auxiliar para obtener información de los servidores
    public String obtenerEstadoServidores() {
        lock.lock();
        try {
            StringBuilder estado = new StringBuilder();
            for (ServidorChat servidor : servidores) {
                estado.append("Servidor en puerto ").append(servidor.getPuerto())
                      .append(" - Conexiones: ").append(servidor.getContadorConexiones())
                      .append("\n");
            }
            return estado.toString();
        } finally {
            lock.unlock();
        }
    }
}