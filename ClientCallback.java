import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote {
    void updateBoard(char[][] board, boolean isMyTurn) throws RemoteException;
    void gameOver(String message) throws RemoteException;
}
