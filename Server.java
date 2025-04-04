import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) {
        try {
            System.setSecurityManager(new SecurityManager());

            try {
                LocateRegistry.createRegistry(1099);
                System.out.println("Nouveau rmiregistry créé.");
            } catch (RemoteException e) {
                System.out.println("rmiregistry déjà actif.");
            }

            SessionFactory factory = new SessionFactoryImpl();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Factory", factory);

            System.out.println("Serveur Tic-Tac-Toe prêt !");
            
            // Object lock = new Object();
            // synchronized (lock) {
            //     try {
            //         lock.wait();
            //     } catch (InterruptedException e) {
            //         System.out.println("Serveur arrêté.");
            //     }
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
