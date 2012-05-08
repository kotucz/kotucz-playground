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
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Rumbler;
import tradeworld.actions.AbstractAction;
import tradeworld.actions.SpawnSoldier;
import tradeworld.gui.ChaosFrame;
import tradeworld.multi.ClientId;
import tradeworld.multi.ClientImpl;
import tradeworld.multi.Server;
import tradeworld.multi.ServerImpl;
import tradeworld.war.Soldier;

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
    private PlayerId playerid;
    private Time time;
    private final ClientId clientid;
    public Soldier soldier;

    public World getWorld() {
        return world;
    }

    private Game(Server server) throws RemoteException {
        this.world = new World(this);
//        this.player = new Player("Kotuc", Color.RED, 1000000, world);
//        this.playerid = new Player("Kotuc", Color.RED, 1000000, world).getId();
//        world.addPlayer(player);       
        this.chaosFrame = new ChaosFrame(this, world);

        if (server == null) { // create one
            ServerImpl server1 = new ServerImpl(this);
            server = server1.bind();
        }

        this.server = server;

        ClientImpl client1 = new ClientImpl(this);
        this.clientid = client1.join(server);

        this.controllerEnvironment = ControllerEnvironment.getDefaultEnvironment();

        {
            int i = 0;
            for (Controller controller : controllerEnvironment.getControllers()) {
                if (controller.getType() == Controller.Type.GAMEPAD) {
                    System.out.println("controller " + (i++) + ":" + controller.getName());
                    int j = 0;
                    for (Component component : controller.getComponents()) {
                        System.out.println("-->" + (j++) + "\t" + component.getIdentifier());
                    }
                    for (Rumbler rumbler : controller.getRumblers()) {
                        System.out.println("Rumbler " + rumbler.getAxisName());
                        rumbler.rumble(1);
                    }
                }
            }
        }


        // USING: creating local client too
        // NOT: do not have to be a client since using servers world

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
        Game game = new Game(ClientImpl.findServer(host));
        game.playerid = new Player("Albert", Color.BLUE, 1000000, game.world).getId();
        return game;
    }

    public static Game createGame() throws RemoteException {
        Game game = new Game(null);
        game.playerid = new Player("Kotuc", Color.RED, 1000000, game.world).getId();
        System.out.print("Soldiers ");
        for (int i = 0; i < 1; i++) {
            System.out.print(i + ", ");
//            final Soldier sold = new Soldier(new Point3d(Math.random() * 20, Math.random() * 20, 0));
//            sold.id = new ObjectId<Soldier>(sold, i);
//            this.putObject(sold);

            game.scheduleAction(new SpawnSoldier(new Point3d(Math.random() * 20, Math.random() * 20, 0)));

//            vehicleThread.add(vehicle);
        }
        return game;
    }

    public void perform(GameAction action) {
        synchronized (actions) {
            this.actions.add(action);
        }
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
        userInput();
        synchronized (actions) {
            while (!actions.isEmpty()) {
                actions.poll().perform(world);
            }
        }
        world.moveAll(time);
        chaosFrame.updatePanels();
    }
    private final ControllerEnvironment controllerEnvironment;
    private Vector3d vector = new Vector3d();
    private Vector3d sight = new Vector3d(1, 0, 0);
    private double pan = 0;
    private double tilt = 0;

    /**
     *  y   x   ry  rx  z
     *  0   1   2   9   pov
     */
    private void userInput() {

        boolean fire = false;

        for (Controller controller : controllerEnvironment.getControllers()) {

            if (controller.getType() == Controller.Type.GAMEPAD) {

                controller.poll();

                for (Component component : controller.getComponents()) {
//                    if ("x".equals(component.getName())) {
//                        double x = component.getPollData();
//
//                    }
                    float value = component.getPollData();
                    String name = component.getIdentifier().getName();
//                }
//
//                EventQueue queue = controller.getEventQueue();
//                Event event = new Event();
//
//                while (queue.getNextEvent(event)) {
//                    float value = event.getValue();
//                    String name = event.getComponent().getIdentifier().getName();

                    if ("x".equals(name)) {
                        vector.x = value;
                    } else if ("y".equals(name)) {
                        vector.y = -value;
                    } else if ("rx".equals(name)) {
                        pan += -value / 10;
                    } else if ("ry".equals(name)) {
                        tilt = -value;
                    } else if ("z".equals(name)) {
                        System.out.println("Z " + value);
                        fire |= (value < -0.5);
                    } else if (value > 0.5) { // pressed
                        if ("0".equals(name)) {
                            fire |= true;
                        } else if ("2".equals(name)) {
                            scheduleAction(new SpawnSoldier(new Point3d(Math.random() * 20, Math.random() * 20, 0)));
                        } else if ("1".equals(name)) {
                            System.out.println("JUMP");
                        } else {
                            System.out.println("other " + name);
                        }
                    }
                }

            }

//            System.out.println("controller " + controller.getName());
        }

//        System.out.println("Vector " + vector);
        if (soldier != null) {
            {// camera
                Transform3D t3d = new Transform3D();
                Point3d pos = new Point3d(soldier.getPos(getCurrentTime()));
                pos.z += 1.0;// 1.75
//            pos.z = 17;
                Point3d center = new Point3d(pos);

                sight = new Vector3d(Math.cos(pan), Math.sin(pan), tilt);

                center.add(sight);

                t3d.lookAt(pos, center, new Vector3d(0, 0, 1));
//            t3d.lookAt(pos, new Point3d(), new Vector3d(0, 1, 0));
                t3d.invert();
                world.getWorld3d().setEye(t3d);
            }
            {
                Transform3D t3d = new Transform3D();
//            center = new Point3d(pos);
//            center.z -= 1;
//            t3d.lookAt(pos, center, sight);
                t3d.rotZ(pan - Math.PI / 2);
                Vector3d vec = new Vector3d(vector);
                t3d.transform(vec);
//            vec.z = 0;
                vec.scale(2.5);
                soldier.go(vec, getCurrentTime());
            }
            if (fire) {
                Vector3d vec = new Vector3d(sight);
                vec.normalize();
                vec.scale(5);
                soldier.fire(vec, getCurrentTime());
                System.out.println("FIRE");

            }

        } else {
            if (fire) {
                scheduleAction(new SpawnSoldier(new Point3d(Math.random() * 20, Math.random() * 20, 0)));
                System.out.println("RESPAWN");
            }
        }



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
