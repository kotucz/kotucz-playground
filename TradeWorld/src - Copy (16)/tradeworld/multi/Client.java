package tradeworld.multi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import tradeworld.actions.GameAction;

/**
 *
 * @author Kotuc
 */
public interface Client extends Remote {

    void sendMessage(String text) throws RemoteException;

    void scheduleAction(GameAction action) throws RemoteException;

}
