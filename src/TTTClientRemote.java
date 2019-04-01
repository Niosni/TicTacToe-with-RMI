//
// TTTClientRemote.java: interface for callbacks from server to client
//

import java.rmi.*;

public interface TTTClientRemote extends Remote
{
   void updateBoard(TTTBoard new_board) throws RemoteException;
}