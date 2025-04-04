import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SessionFactory extends Remote {
    Session joinGame(ClientCallback player) throws RemoteException;
}
