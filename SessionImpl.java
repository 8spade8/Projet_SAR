import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class SessionImpl extends UnicastRemoteObject implements Session {
    private char[][] board;
    private List<ClientCallback> players;
    private int currentPlayer;

    protected SessionImpl(ClientCallback player1, ClientCallback player2) throws RemoteException {
        super();
        board = new char[3][3];
        resetBoard();
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        currentPlayer = 0;
        updateClients();
    }

    @Override
    public synchronized void playMove(String playerName, int x, int y) throws RemoteException {
        if (board[x][y] == ' ') {
            board[x][y] = (currentPlayer == 0) ? 'X' : 'O';
            if (checkWin()) {
                notifyGameOver("Le joueur " + (currentPlayer + 1) + " a gagné !");
            } else if (isBoardFull()) {
                notifyGameOver("Match nul !");
            } else {
                currentPlayer = 1 - currentPlayer;
                updateClients();
            }
        }
    }

    private void updateClients() throws RemoteException {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).updateBoard(board, i == currentPlayer);
        }
    }

    private void notifyGameOver(String message) throws RemoteException {
        for (ClientCallback client : players) {
            client.gameOver(message);
        }
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = ' ';
    }

    private boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return true;
            if (board[0][i] != ' ' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return true;
        }
        return board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2] || 
               board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0];
    }

    private boolean isBoardFull() {
        for (char[] row : board)
            for (char cell : row)
                if (cell == ' ') return false;
        return true;
    }
}
