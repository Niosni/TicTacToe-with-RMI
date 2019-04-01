//
// TTTBoard.java: track state of Tic Tac Toe game, both who owns what
//      square and whose turn it is
//

import java.io.*;

public class TTTBoard implements Serializable
{
   private char[][] _owner;
   private char _turn;
   private boolean have_winner = false;

   public TTTBoard()
   {
      _owner = new char[3][3];
      //      for(int row = 0; row < 3; ++row)
      //         _owner[row] = new char[3];
      reset();
   }

   public void reset()
   {
      for(int row = 0; row < 3; ++row)
      {
         for(int col = 0; col < 3; ++col)
            _owner[row][col] = ' ';
      }
      _turn = 'X';
      have_winner = false;
   }

   public char owner(int col, int row)
   {
      return _owner[row][col];
   }

   public String ownerStr(int col, int row)
   {
      char o = owner(col, row);
      if ( o == 'O' )
         return "O";
      else if ( o == 'X' )
         return "X";
      else
         return " ";
   }

   public boolean isOpen(int col, int row)
   {
      return !have_winner && owner(col, row) == ' ';
   }

   public char turn() { return _turn; }

   public void pick(int col, int row)
   {
      assert !have_winner && _owner[row][col] == ' ';
      _owner[row][col] = _turn;
      if ( _turn == 'X' )
         _turn = 'O';
      else
         _turn = 'X';
   }

   public char winner()
   {
      have_winner = true;
      for(int col = 0; col < 3; ++col)
      {
         if (    _owner[0][col] != ' '
              && _owner[0][col] == _owner[1][col]
              && _owner[1][col] == _owner[2][col] )
            return _owner[0][col];
      }
      for(int row = 0; row < 3; ++row)
      {
         if (    _owner[row][0] != ' '
              && _owner[row][0] == _owner[row][1]
              && _owner[row][1] == _owner[row][2] )
            return _owner[row][0];
      }
      if ( _owner[1][1] != ' ' )
      {
         if ( _owner[0][0] == _owner[1][1] && _owner[1][1] == _owner[2][2] )
            return _owner[1][1];
         if ( _owner[2][0] == _owner[1][1] && _owner[1][1] == _owner[0][2] )
            return _owner[1][1];
      }
      have_winner = false;
      return ' ';
   }
}