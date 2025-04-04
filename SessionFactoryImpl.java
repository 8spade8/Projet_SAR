import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class SessionFactoryImpl extends UnicastRemoteObject implements SessionFactory {
    private Map<ClientCallback, ClientCallback> waitingPlayers;
    private int activeGames = 0;
    private static final int MAX_GAMES = 10;

    protected SessionFactoryImpl() throws RemoteException {
        super();
        waitingPlayers = new HashMap<>();
    }

    @Override
    public synchronized Session joinGame(ClientCallback player) throws RemoteException {
        if (activeGames >= MAX_GAMES) {
            throw new RemoteException("Le serveur est plein. Essayez plus tard.");
        }

        if (waitingPlayers.isEmpty()) {
            waitingPlayers.put(player, null);
            return null; 
        } else {
            ClientCallback player1 = waitingPlayers.keySet().iterator().next();
            waitingPlayers.remove(player1);
            activeGames++;
            return new SessionImpl(player1, player);
        }
    }
}
