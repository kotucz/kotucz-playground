package tradeworld;

import java.awt.Color;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import tradeworld.graphics.ChaosFrame;
import tradeworld.multi.ClientImpl;
import tradeworld.multi.Server;
import tradeworld.multi.ServerImpl;

/**
 *
 * @author Kotuc
 */
public class Game {

    private final List<Player> players = new LinkedList<Player>();
    private final World world;
    private final ChaosFrame chaosFrame;
    private final Server server;
    private final Queue<GameAction> actions = new LinkedList<GameAction>();
    public final Player player;

    public World getWorld() {
        return world;
    }

    private Game() throws RemoteException {
        this.world = new World(this);
        this.player = new Player("Kotuc", Color.RED, 1000000, world);
        this.chaosFrame = new ChaosFrame(this, world);

        ServerImpl server1 = new ServerImpl(this);
        this.server = server1.bind();

        ClientImpl client1 = new ClientImpl(this);
        client1.join(server1);

        // USING: creating local client too
        // NOT: do not have to be a client since using servers world

    }

    private Game(String host) throws RemoteException, NotBoundException {
        this.world = new World(this);
        this.player = new Player("Albert", Color.BLUE, 1000000, world);
        this.chaosFrame = new ChaosFrame(this, world);

        Server server1 = ClientImpl.findServer(host);
        this.server = server1;

        ClientImpl client1 = new ClientImpl(this);
        client1.join(server1);

    }

    public static Game joinGame(String host) throws RemoteException, NotBoundException {
        return new Game(host);
    }

    public static Game createGame() throws RemoteException {
        return new Game();
    }

    public void perform(GameAction action) {
        this.actions.add(action);
    }

    public void scheduleAction(GameAction action) {
        try {
            server.scheduleAction(action);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void gameStep() {
        while (!actions.isEmpty()) {
            actions.poll().perform(world);
        }
        world.moveAll();
        chaosFrame.updatePanels();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException {
        Game.createGame().start();
    }

    public void start() {
        new Thread() {

            @Override
            public void run() {
                while (true) {
                    gameStep();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();

    }
}
