package tradeworld.multi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import tradeworld.Game;
import tradeworld.actions.AbstractAction;
import tradeworld.actions.GameAction;

/**
 *
 * @author Kotuc
 */
public class ServerImpl implements Server {

    private final Game game;
    public static final String name = "TradeWorldServer";
    private final Set<ClientHandler> clients = new HashSet<ClientHandler>();
    private final List<GameAction> actions = new ArrayList<GameAction>();

    public ServerImpl(Game game) {
        this.game = game;
    }

    public Server bind() throws RemoteException {

        Server server = this;

        Server stub =
                (Server) UnicastRemoteObject.exportObject(server, 0);

        Registry registry = LocateRegistry.getRegistry();
        registry.rebind(name, stub);

        System.out.println("Server bound.");
        return stub;
    }
    private int lastActionId = 1000;

    public void scheduleAction(GameAction action) {

        ((AbstractAction) action).id = ++lastActionId;

        System.out.println("action arrived: " + action);
        if (action.validate(game.getWorld())) {
//            game.perform(action);
            synchronized (actions) {
                for (ClientHandler client : clients) {
                    client.actionQueue.add(action);//scheduleAction(action);
//                    clientInterface.sendMessage("do action !");
//                    } catch (RemoteException ex) {
//                        Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                }
                actions.add(action);
            }
//            System.out.println("Action " + action + " scheduled");
        }
    }

    public GameAction pollAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) throws RemoteException {
        System.err.println("Do not forget to run rmiregistry.");
        System.err.println("Put dist/TradeWorld.jar into CLASSPATH for rmiregistry.");
        ServerImpl server = new ServerImpl(null);
        server.bind();
    }
    private int lastClientId = 100;

    public ClientId registerClient(Client client) throws RemoteException {

        ClientId clientId = new ClientId(++lastClientId, "Client " + lastClientId);
        ClientHandler clientHandler = new ClientHandler(clientId, client);

        clients.add(clientHandler);

        new Thread(clientHandler).start();

        System.out.println("client " + client + " joined the game");
        client.sendMessage("Welcome to the game");

        return clientId;
    }

    class ClientHandler implements Runnable {

        final ClientId clientid;
        final Client client;
        final BlockingQueue<GameAction> actionQueue = new LinkedBlockingQueue<GameAction>();

        public ClientHandler(ClientId id, Client client) {
            this.client = client;
            this.clientid = id;
            actionQueue.addAll(actions);
        }

        public void run() {
            try {
                while (true) {
                    GameAction action = actionQueue.take();
                    client.scheduleAction(action);
                }
            } catch (Exception ex) {
                Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

