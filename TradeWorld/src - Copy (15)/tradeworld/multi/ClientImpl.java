package tradeworld.multi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import tradeworld.Game;
import tradeworld.actions.GameAction;

/**
 *
 * @author Kotuc
 */
public class ClientImpl implements Client {

    private final Game game;

    public ClientImpl(Game game) {
        this.game = game;
    }

    public static Server findServer(String host) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host);
        return (Server) registry.lookup(ServerImpl.name);
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        ClientImpl client = new ClientImpl(null);
        Server findServer = client.findServer("localhost");
        findServer.scheduleAction(null);

        client.join(findServer);

        System.out.println("done.");
    }

    public ClientId join(Server server) throws RemoteException {
        Client stub =
                (Client) UnicastRemoteObject.exportObject(this, 0);

        Registry registry = LocateRegistry.getRegistry(); // TODO not common
        registry.rebind(stub.toString(), stub); // not common

        return server.registerClient(stub);
    }

    public void sendMessage(String text) throws RemoteException {
        System.out.println("Incomming message: " + text);
    }

    public void scheduleAction(GameAction action) throws RemoteException {
        this.game.perform(action);
    }
}
