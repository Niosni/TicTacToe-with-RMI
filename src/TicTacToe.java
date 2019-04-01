//
// TicTacToe.java: distributed tic tac toe game
//

import java.rmi.*;
import java.rmi.server.*; // added to support callbacks
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class TicTacToe extends JFrame
   implements ActionListener, TTTClientRemote
{ 
   protected JButton[][] squares;
   protected JLabel statusLabel = new JLabel();

   protected TTTService remoteTTTBoard;

   public TicTacToe()
   {
      super("Tic Tac Toe, Distributed");
      setSize(650, 400); // larger than needed; resizing done below

      // set up the remote service
      try
      {
         System.out.println("Connecting...");
         String serverObjectName = "rmi://io.uwplatt.edu:"
            + TTTServiceImpl.ServicePort + "/"
            + TTTServiceImpl.ServiceName;
         remoteTTTBoard = (TTTService)Naming.lookup(serverObjectName);
         //
         // the next two lines allow the server to call updateBoard:
         //
         UnicastRemoteObject.exportObject(this);
         remoteTTTBoard.register(this);
      }
      catch ( Exception e )
      {
         System.out.println("Exception: " + e.getMessage());
         e.printStackTrace();
         return;
      }

      // a bit of magic to halt the application if the user closes the window
      addWindowListener(
         new WindowAdapter() {
         public void windowClosing(WindowEvent event) { System.exit(0); }
         });

      // main box; everything placed in this
      JPanel box = new JPanel();
      box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
      getContentPane().add(box);

      box.add(statusLabel, BorderLayout.NORTH);

      // set up the x's and o's
      JPanel xs_and_os = new JPanel();
      xs_and_os.setLayout(new GridLayout(3, 3, 5, 5));
      box.add(xs_and_os);

      squares = new JButton[3][3];
      for(int row = 0; row < 3; ++row)
      {
         for(int col = 0; col < 3; ++col)
         {
            squares[row][col] = new JButton();
            xs_and_os.add(squares[row][col]);
            squares[row][col].addActionListener(this);
         }
      }

      resetBoard();
      
      // resize frame
      pack();
      setSize(300, 300);
   }

   protected void resetBoard()
   {
      for(int row = 0; row < 3; ++row)
         for(int col = 0; col < 3; ++col)
            squares[row][col].setText(" ");
      try
      {
         remoteTTTBoard.reset();
      }
      catch ( RemoteException e )
      {
         System.out.println("Exception: " + e.getMessage());
         e.printStackTrace();
         System.exit(1);
      }
      setStatus(getBoard());
   }

   protected void setStatus(TTTBoard bd)
   {
      char w = bd.winner();
      if ( w == ' ' )
         statusLabel.setText("Click on " + bd.turn() + " square");
      else
         statusLabel.setText("Winner: " + w);
   }

   protected TTTBoard getBoard()
   {
      TTTBoard res = null;
      try
      {
         res = remoteTTTBoard.getState();
      }
      catch ( RemoteException e )
      {
         System.out.println("Exception: " + e.getMessage());
         e.printStackTrace();
         System.exit(1);
      }
      return res;
   }

   protected void pickSquare(int col, int row)
   {
      try
      {
         remoteTTTBoard.pick(col, row);
      }
      catch ( RemoteException e )
      {
         System.out.println("Exception: " + e.getMessage());
         e.printStackTrace();
         System.exit(1);
      }
   }

   // method "called" by remote object to update the state of the game
   public void updateBoard(TTTBoard new_board) throws RemoteException
   {
      for(int row = 0; row < 3; ++row)
         for(int col = 0; col < 3; ++col)
            squares[row][col].setText(new_board.ownerStr(col, row));
      setStatus(new_board);
   }

   public void actionPerformed(ActionEvent event)
   {
      TTTBoard bd = getBoard();

      for(int row = 0; row < 3; ++row)
         for(int col = 0; col < 3; ++col)
            if ( event.getSource() == squares[row][col] )
            {
               if ( bd.isOpen(col, row) )
               {
                  pickSquare(col, row);
                  bd = getBoard();
                  squares[row][col].setText(bd.ownerStr(col, row));
                  setStatus(bd);
               }
               return;
            }
   }
   
   public static void main(String[] args)
   {
      TicTacToe bd = new TicTacToe();
      bd.setVisible(true);
   }
}