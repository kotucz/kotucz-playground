package tradeworld.multi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tradeworld.Game;
import tradeworld.actions.GameAction;

/**
 *
 * @author Kotuc
 */
public class ServerImpl implements Server {

    private final Game game;
    public static final String name = "TradeWorldServer";
    private List<Client> clients = new LinkedList<Client>();

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

    public void scheduleAction(GameAction action) {
        System.out.println("action arrived: "+action);
        if (action.validate(game.getWorld())) {
//            game.perform(action);
            for (Client client : clients) {
                try {
                    client.scheduleAction(action);
//                    clientInterface.sendMessage("do action !");
                } catch (RemoteException ex) {
                    Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
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

    public void registerClient(Client client) throws RemoteException {
        clients.add(client);
        System.out.println("client " + client + " joined the game");
        client.sendMessage("Welcome to the game");
    }
}
