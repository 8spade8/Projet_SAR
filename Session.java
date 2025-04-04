import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Session extends Remote {
    void playMove(String playerName, int x, int y) throws RemoteException;
}
