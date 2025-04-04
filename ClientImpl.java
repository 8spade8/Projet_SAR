import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientImpl extends UnicastRemoteObject implements ClientCallback {
    private Session session;
    private boolean isYourTurn;
    private Scanner scanner;

    protected ClientImpl() throws RemoteException {
        super();
        scanner = new Scanner(System.in);
        this.isYourTurn = false;
    }

    public void connectToServer() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            SessionFactory factory = (SessionFactory) registry.lookup("Factory");
            session = factory.joinGame(this);

            if (session == null) {
                System.out.println("En attente d'un autre joueur...");
            } else {
                System.out.println("Partie trouvée ! Jeu en cours...");
            }

            while (true) {
                if (isYourTurn) {
                    makeMove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeMove() {
        boolean validMove = false;
            while (validMove == false) {
                try {
                System.out.println("Entrez votre coup (ligne colonne) : ");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                session.playMove("joueur", x, y);
                validMove = true;
            } catch (Exception e) {
                System.out.println("Coup invalide, réessayez.");
            }
        }
    }

    @Override
    public void updateBoard(char[][] board, boolean isYourTurn) throws RemoteException {
        this.isYourTurn = isYourTurn;
        System.out.println("Grille mise à jour :");
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell == ' ' ? "-" : cell);
                System.out.print(" ");
            }
            System.out.println();
        }
        if (isYourTurn) {
            System.out.println("C'est votre tour !");
            makeMove();
        }
        else {
            System.out.println("Attendez votre tour...");
        }
    }

    @Override
    public void gameOver(String message) throws RemoteException {
        System.out.println("FIN DE PARTIE : " + message);
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            // 🔹 Activer le Security Manager
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
    
            ClientImpl client = new ClientImpl();
            client.connectToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
