package tradeworld;

import tradeworld.actions.GameAction;
import java.awt.Color;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
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
import tradeworld.multi.ClientId;
import tradeworld.multi.ClientImpl;
import tradeworld.multi.Server;
import tradeworld.multi.ServerImpl;
import tradeworld.war.PhySoldier;
import tradeworld.war.Soldier;
import tradeworld.war.WarChaosFrame;

/**
 *
 * @author Kotuc
 */
public class Game {

    private final World world;
    private final WarChaosFrame chaosFrame;
    private final Server server;
    private final Queue<GameAction> actions = new LinkedList<GameAction>();
//    private final Player player;
    private PlayerId playerid;
    private Time time;
    private final ClientId clientid;
    public Soldier soldier;
    private final Map<PlayerId, Player> players = new HashMap<PlayerId, Player>();

    public World getWorld() {
        return world;
    }


    private Game(Server server) throws RemoteException {
        this.world = new World(this);
//        this.player = new Player("Kotuc", Color.RED, 1000000, world);
//        this.playerid = new Player("Kotuc", Color.RED, 1000000, world).getId();
//        world.addPlayer(player);       
        this.chaosFrame = new WarChaosFrame(this, world);

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
                    System.out.println("Gamepad " + (i++) + ":" + controller.getName());
                    int j = 0;
                    for (Component component : controller.getComponents()) {
                        System.out.println("-->" + (j++) + "\t" + component.getIdentifier());
                    }
                    for (Rumbler rumbler : controller.getRumblers()) {
                        System.out.println("Rumbler " + rumbler.getAxisName());
                        rumbler.rumble(1);
                    }
                }
                if (controller.getType() == Controller.Type.MOUSE) {
                    System.out.println("Mouse " + (i++) + ":" + controller.getName());
                    int j = 0;
                    for (Component component : controller.getComponents()) {
                        System.out.println("-->" + (j++) + "\t" + component.getIdentifier());
                    }
                }
                if (controller.getType() == Controller.Type.KEYBOARD) {
                    System.out.println("Keyboard " + (i++) + ":" + controller.getName());
                    int j = 0;
                    for (Component component : controller.getComponents()) {
                        System.out.print((j++) + ": " + component.getIdentifier() + "\t");
                    }
                    System.out.println();
                }
            }
        }


        // USING: creating local client too
        // NOT: do not have to be a client since using servers world

    }

    public void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    public Player getPlayer(PlayerId id) {
        if (!players.containsKey(id)) {
            throw new AssertionError("player with id " + id + " not found");
        }
        return players.get(id);
    }

    public ClientId getClientid() {
        return clientid;
    }

    public Time getCurrentTime() {
        return this.time;
    }
    private long timeoffset = 0;

    public void setTime(Time time) {
        this.timeoffset = time.getMillis() - System.currentTimeMillis();
        this.time = time;
    }

    public Player getCurrentPlayer() {
        return getPlayer(playerid);
    }

    public static Game joinGame(String host) throws RemoteException, NotBoundException {
        Game game = new Game(ClientImpl.findServer(host));
        Player player = new Player("Albert", Color.BLUE, 1000000, game.world);
        game.addPlayer(player);
        game.playerid = player.getId();
        return game;
    }

    public static Game createGame() throws RemoteException {
        Game game = new Game(null);
//        game.playerid = new Player("Kotuc", Color.RED, 1000000, game.world).getId();
        Player player = new Player("Kotuc", Color.RED, 1000000, game.world);
        game.addPlayer(player);
        game.playerid = player.getId();
        game.addBot();

//        System.out.print("Soldiers ");
//        for (int i = 0; i < 1; i++) {
//            System.out.print(i + ", ");
////            final Soldier sold = new Soldier(new Point3d(Math.random() * 20, Math.random() * 20, 0));
////            sold.id = new ObjectId<Soldier>(sold, i);
////            this.putObject(sold);
//
////            vehicleThread.add(vehicle);
//        }
        return game;
    }

    public Soldier respawnSoldier(PlayerId playerid) {
        Soldier soldier1 = new PhySoldier(world, new Point3d(20 * Math.random(), 20 * Math.random(), 0));
        soldier1.playerid = playerid;
        soldier1.id = createUniqueId(soldier1);
        soldier1.equip();
        world.putObject(soldier1);
        // TODO not deterministic, wheather this or the create action ojbect are used
        // may cause exception

//        sendToAll(new CreateObjectAction(soldier1));

//        return world.getObject(soldier1.id);       

        return soldier1;
//        sendToAll(new SpawnSoldier(new Point3d(Math.random() * 20, Math.random() * 20, 0), playerid));
    }
    private int idcounter = 1000000;

    public <T> ObjectId<T> createUniqueId(T object) {
        return new ObjectId<T>(object, ++idcounter, clientid);
    }
    Set<Bot> bots = new HashSet<Bot>();

    public void addBot() {
        final Player player = new Player("Bot Karel", Color.yellow, 1000, world);
        this.addPlayer(player);
        final Bot bot = new Bot(player.getId(), this);
        bots.add(bot);
//        new Thread() {
//            @Override
//            public void run() {
//                while (true) {
//                    bot.decide();
//                }
//            }
//        }.start();
    }

    public void perform(GameAction action) {
        synchronized (actions) {
            this.actions.add(action);
        }

    }

    public void sendToAll(GameAction action) {
        try {
//            ((AbstractAction) action).time = getCurrentTime();
            ((AbstractAction) action).clientid = this.clientid;
            server.scheduleAction(action);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private boolean paused = false;

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    private void gameStep() {

        // realTime
        //        this.time = new Time(System.currentTimeMillis() + timeoffset);

        userInput();

        if (!isPaused()) {

            this.time = this.time.addSec(0.01);

            synchronized (actions) {
                while (!actions.isEmpty()) {
                    actions.poll().perform(world);
                }

            }
            world.moveAll(time);

            for (Bot bot : bots) {
                bot.decide();
            }

        }

        chaosFrame.updatePanels();

        chaosFrame.nameLabel.setText(getCurrentPlayer().getName());

        Color playercolor = getCurrentPlayer().getColor();

        chaosFrame.nameLabel.setForeground(playercolor);
        chaosFrame.soldierInstanceLabel.setForeground(playercolor);
        chaosFrame.healthLabel.setForeground(playercolor);

        if (soldier != null) {
            chaosFrame.healthLabel.setText("Health: " + soldier.getHealth());
            chaosFrame.soldierInstanceLabel.setText("Instance: " + soldier);
        } else {
            chaosFrame.healthLabel.setText("Health: DEAD");
            chaosFrame.soldierInstanceLabel.setText("Instance: null");
        }

        chaosFrame.timeLabel.setText(new Date(getCurrentTime().getMillis()) + "(" + timeoffset + ")");

        world.getWorld3d().canvas3D.render();

    }
    private final ControllerEnvironment controllerEnvironment;

    /**
     *  y   x   ry  rx  z
     *  0   1   2   9   pov
     */
    private void userInput() {

        Vector3d vector = new Vector3d();
        boolean fire = false;
        double dpan = 0;
        double dtilt = 0;

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
                        dpan += (-0.1 * Math.signum(value) * Math.pow(value, 2));
                    } else if ("ry".equals(name)) {
                        dtilt += (-0.1 * Math.signum(value) * Math.pow(value, 2));
                    } else if ("z".equals(name)) {
                        System.out.println("Z " + value);
                        fire |= (value < -0.5);
                    } else if (value > 0.5) { // pressed
                        if ("0".equals(name)) {
                            fire |= true;
                        } else if ("2".equals(name)) {
//                            respawnSoldier(playerid);
//                            scheduleAction(new SpawnSoldier(new Point3d(Math.random() * 20, Math.random() * 20, 0)));
                        } else if ("1".equals(name)) {
                            System.out.println("JUMP");
                        } else {
                            System.out.println("other " + name);
                        }

                    }
                }

            } // end gamepad

//            System.out.println("controller " + controller.getName());

            if (controller.getType() == Controller.Type.MOUSE) {

                controller.poll();

                for (Component component : controller.getComponents()) {
                    float value = component.getPollData();
                    String name = component.getIdentifier().getName();

                    if ("x".equals(name)) {
                        dpan += (-0.001 * Math.signum(value) * Math.pow(value, 2));
                    } else if ("y".equals(name)) {
                        dtilt += (-0.001 * value);
                    } else if (value > 0.5) { // pressed
                        if ("Left".equals(name)) {
                            fire |= true;
                        }
                    }
                }

            } // end mouse

            if (controller.getType() == Controller.Type.KEYBOARD) {

                controller.poll();

                for (Component component : controller.getComponents()) {
                    float value = component.getPollData();
                    String name = component.getIdentifier().getName();

                    if (value > 0.5) { // pressed
                        if ("W".equals(name)) {
                            vector.y = +1;
                        } else if ("S".equals(name)) {
                            vector.y = -1;
                        } else if ("A".equals(name)) {
                            vector.x = -1;
                        } else if ("D".equals(name)) {
                            vector.x = +1;
                        } else if (" ".equals(name)) {
                            vector.z = +1;
                        } else {
                            System.err.println("No ction for " + name + " defined.");
                        }
                    }
                }
            } // end keyboard
//            System.out.println("controller " + controller.getName());
        }

//        System.out.println("Vector " + vector);
        if (soldier != null) {

            if (soldier.isAlive()) {



                soldier.turnLeft(dpan);
                soldier.turnUp(dtilt);

                {// camera
//                Transform3D viewT = new Transform3D();
//                Point3d pos = new Point3d(soldier.getPos());
//                pos.z += 1.75; // eyes
////            pos.z = 17;
//                Point3d center = new Point3d(pos);
////                sight = new Vector3d(Math.cos(pan), Math.sin(pan), tilt);
//                center.add(soldier.getSightVector());
//
//                viewT.lookAt(pos, center, new Vector3d(0, 0, 1));
////            t3d.lookAt(pos, new Point3d(), new Vector3d(0, 1, 0));
//                viewT.invert();
//                world.getWorld3d().setEye(viewT);
                    Transform3D viewT = new Transform3D();
                    soldier.getLookTransform(viewT);
//                viewT.invert();
                    world.getWorld3d().setEye(viewT);
                }

                soldier.accelerateFirstPerson(vector, time);

                if (vector.z > 0.1) {
                    soldier.jump();
                }

                if (fire) {
                    soldier.fireFP(time);
                }

            } else {
                this.soldier = null;
            }

        }

        if (soldier == null) {
            if (fire) {
                this.soldier = respawnSoldier(playerid);
//                scheduleAction(new SpawnSoldier(new Point3d(Math.random() * 20, Math.random() * 20, 0)));
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

    public boolean isLocal(PlayerId playerid) {
        if (playerid.equals(this.playerid)) {
            return true;
        }

        for (Bot bot : bots) {
            if (playerid.equals(bot.playerid)) {
                return true;
            }

        }
        return false;
    }
}
