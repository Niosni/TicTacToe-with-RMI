//
// TTTServiceImpl.java: implementation side of Tic Tac Toe game server
//

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

class TTTServiceImpl extends UnicastRemoteObject
   implements TTTService
{
   // modify the following name so it is unique to you (at the least, change
   //   "hasker" to your login name)
   public final static String ServiceName = "hasker-ttt";

   // modify the following port number to some value between 10,000 and 30,000
   // DO NOT USE 1099 UNDER ANY CIRCUMSTANCES!
   public final static int ServicePort = 10000;

   // the object being served up
   protected TTTBoard board = new TTTBoard();

   // I probably wouldn't have to have a constructor for this solution,
   //   but it's here to show the pattern for other constructors
   public TTTServiceImpl() throws RemoteException
   {
      super();
   }

   // implementations of methods in CounterService
   public TTTBoard getState() throws RemoteException
   {
      return board;
   }

   public void pick(int col, int row) throws RemoteException
   {
      board.pick(col, row);
      updateClients();
   }

   public void reset() throws RemoteException
   {
      board.reset();
      updateClients();
   }

   // the next 5 lines allow the server to track who the clients are
   //   the list is synchronized to avoid concurrency problems
   //   (this model works assuming all clients start up at the same time)
   List clients = Collections.synchronizedList(new LinkedList());
   public void register(TTTClientRemote newClient) throws RemoteException
   {
      clients.add(newClient);
   }

   public void updateClients()
   {
      Iterator it = clients.iterator();
      while ( it.hasNext() )
      {
         TTTClientRemote client = (TTTClientRemote)it.next();
         try
         {
            client.updateBoard(board);
         }
         catch ( Exception e )
         {
            // note system does not halt in such situations!
            System.out.println("Could not update client " + client.toString());
         }
      }
          
   }

   public static void main(String args[])
   {
      System.out.println("Initializing TTTService...");
      try
      {
         TTTService cserv = new TTTServiceImpl();
         String serverObjectName = "rmi://localhost:" + ServicePort
            + "/" + ServiceName;
         Naming.rebind(serverObjectName, cserv);
         System.out.println("TTTService running.");
      }
      catch (Exception e)
      {
         System.out.println("Exception: " + e.getMessage());
         e.printStackTrace();
      }
   }
}