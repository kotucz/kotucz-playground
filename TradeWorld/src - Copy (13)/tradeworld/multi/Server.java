package tradeworld.multi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import tradeworld.actions.GameAction;

/**
 *
 * @author Kotuc
 */
public interface Server extends Remote {

    void scheduleAction(GameAction action) throws RemoteException;

    void registerClient(Client client) throws RemoteException;

    GameAction pollAction() throws RemoteException;
}
