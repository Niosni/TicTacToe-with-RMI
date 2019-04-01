//
// TTTService.java: Tic Tac Toe game server
//

import java.rmi.*;

public interface TTTService extends Remote
{
   public TTTBoard getState()                      throws RemoteException;
   public void pick(int col, int row)              throws RemoteException;
   public void reset()                             throws RemoteException;
   // added so client can register self with server for callbacks
   public void register(TTTClientRemote newClient) throws RemoteException;
}