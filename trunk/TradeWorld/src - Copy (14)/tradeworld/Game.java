package tradeworld;

import tradeworld.actions.GameAction;
import java.awt.Color;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Point3d;
import tradeworld.actions.AbstractAction;
import tradeworld.actions.SpawnSoldier;
import tradeworld.gui.ChaosFrame;
import tradeworld.multi.ClientId;
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
//    private final Player player;
    private final PlayerId playerid;
    private Time time;
    private final ClientId clientid;

    public World getWorld() {
        return world;
    }

    private Game() throws RemoteException {
        this.world = new World(this);
//        this.player = new Player("Kotuc", Color.RED, 1000000, world);
        this.playerid = new Player("Kotuc", Color.RED, 1000000, world).getId();
//        world.addPlayer(player);       
        this.chaosFrame = new ChaosFrame(this, world);

        ServerImpl server1 = new ServerImpl(this);
        this.server = server1.bind();

        ClientImpl client1 = new ClientImpl(this);
        this.clientid = client1.join(server1);

        System.out.print("Soldiers ");
        for (int i = 0; i < 3; i++) {
            System.out.print(i + ", ");
//            final Soldier sold = new Soldier(new Point3d(Math.random() * 20, Math.random() * 20, 0));
//            sold.id = new ObjectId<Soldier>(sold, i);
//            this.putObject(sold);

            scheduleAction(new SpawnSoldier(new Point3d(Math.random() * 20, Math.random() * 20, 0)));

//            vehicleThread.add(vehicle);
        }

        // USING: creating local client too
        // NOT: do not have to be a client since using servers world

    }

    private Game(String host) throws RemoteException, NotBoundException {
        this.world = new World(this);
//        this.player = new Player("Albert", Color.BLUE, 1000000, world);
        this.playerid = new Player("Albert", Color.BLUE, 1000000, world).getId();
//        world.addPlayer(player);
        this.chaosFrame = new ChaosFrame(this, world);

        Server server1 = ClientImpl.findServer(host);
        this.server = server1;

        ClientImpl client1 = new ClientImpl(this);
        this.clientid = client1.join(server1);

        System.out.print("Soldiers ");
        for (int i = 0; i < 1; i++) {
            System.out.print(i + ", ");
//            final Soldier sold = new Soldier(new Point3d(Math.random() * 20, Math.random() * 20, 0));
//            sold.id = new ObjectId<Soldier>(sold, i);
//            this.putObject(sold);

            scheduleAction(new SpawnSoldier(new Point3d(Math.random() * 20, Math.random() * 20, 0)));

//            vehicleThread.add(vehicle);
        }

    }

    public ClientId getClientid() {
        return clientid;
    }

    public Time getCurrentTime() {
        return this.time;
    }

    public Player getCurrentPlayer() {
        return world.getPlayer(playerid);
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
//            ((AbstractAction) action).time = getCurrentTime();
            ((AbstractAction) action).clientid = this.clientid;
            server.scheduleAction(action);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void gameStep() {
        this.time = new Time(System.currentTimeMillis());
        while (!actions.isEmpty()) {
            actions.poll().perform(world);
        }
        world.moveAll(time);
        chaosFrame.updatePanels();
    }

    /**
     * @param args the command line arguments
     * @throws RemoteException 
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
