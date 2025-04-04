import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Game extends Remote {
    Session joinGame(ClientCallback client) throws RemoteException;
}
