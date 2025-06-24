import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            Balanceador balanceador = new Balanceador();
            
            ServidorChat servidor1 = new ServidorChat(8081);
            ServidorChat servidor2 = new ServidorChat(8082);
            ServidorChat servidor3 = new ServidorChat(8083);
            
            servidor1.iniciar();
            servidor2.iniciar();
            servidor3.iniciar();
            
            balanceador.agregarServidor(servidor1);
            balanceador.agregarServidor(servidor2);
            balanceador.agregarServidor(servidor3);
            
            ServidorBalanceo servidorBalanceo = new ServidorBalanceo(8080, balanceador);
            servidorBalanceo.iniciar();
            
            System.out.println("Sistema iniciado. Balanceador en puerto 8080, servidores en 8081-8083");
            
            // Mantener la aplicación corriendo
            System.in.read();
            
            // Detener todo al salir
            servidorBalanceo.detener();
            servidor1.detener();
            servidor2.detener();
            servidor3.detener();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}