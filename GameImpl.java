import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GameImpl extends UnicastRemoteObject implements Game {
    private List<ClientCallback> waitingClients;

    protected GameImpl() throws RemoteException {
        super();
        waitingClients = new ArrayList<>();
    }

    @Override
    public synchronized Session joinGame(ClientCallback client) throws RemoteException {
        waitingClients.add(client);
        if (waitingClients.size() >= 2) {
            ClientCallback player1 = waitingClients.remove(0);
            ClientCallback player2 = waitingClients.remove(0);
            return new SessionImpl(player1, player2);
        } else {
            return null;
        }
    }
}
