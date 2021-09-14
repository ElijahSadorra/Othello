package board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;


public class Game{
	
	Board whiteboard = new Board("White", 1);
	Board blackboard = new Board("Black", 0);
	int skipped = 0;
	
	public class Board implements ActionListener
	{
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JPanel grid = new JPanel();
		JLabel turnlabel = new JLabel();
		GridLabel[][] arrayButton = new GridLabel[8][8];
		String players = "";
		Integer turn = 0;
		JButton ai = new JButton();
		
		public Board(String player, Integer turns)
		{
			players = player;
			turn = turns;
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle("Othello "+player+" player");
			frame.setLocationRelativeTo(null);
			frame.setMinimumSize( new Dimension(500, 500) );
			frame.setResizable(false);
			
			panel.setLayout(new BorderLayout(1,1));
			grid.setLayout(new GridLayout(8,8));
			
			frame.getContentPane().add(panel, BorderLayout.CENTER);
			
			ai.setText("Greedy AI ( play "+players+" )");
			ai.addActionListener(this);
			
			if (turn == 1)
				turnlabel.setText(player+" player - click place to put piece");
			else
				turnlabel.setText(player+" player - not your turn");
			
			panel.add(turnlabel, BorderLayout.NORTH);
			panel.add(grid, BorderLayout.CENTER);
			panel.add(ai, BorderLayout.SOUTH);
			
			
			for (int i = 0; i<8; i++)
			{
				for (int j = 0; j<8; j++)
				{
					grid.add(arrayButton[i][j] = new GridLabel());
					arrayButton[i][j].setBackground(new Color(0,255,0));
					arrayButton[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
					arrayButton[i][j].setMinimumSize( new Dimension(50, 50) );
					arrayButton[i][j].setPreferredSize( new Dimension(50, 50) );
					arrayButton[i][j].addActionListener(this);
				}
			}
		

			arrayButton[3][3].addCounter("White");
			arrayButton[4][4].addCounter("White");
				
			arrayButton[3][4].addCounter("Black");
			arrayButton[4][3].addCounter("Black");
			
			
			
			frame.pack();
			frame.setVisible(true);
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Object source = e.getSource();
			
			if (turn == 1) 
			{
				if (source == ai)
				{
					int best_i = -1;
					int best_j = -1;
					
					for (int i = 0; i<8; i++)
					{
						for (int j = 0; j<8; j++)
						{
							if (arrayButton[i][j].color == "none" && canClickSpot(i, j, players, 2)!=0)
							{
								if (best_i == -1 && best_j == -1)
								{
									best_i = i;
									best_j = j;
									continue;
								}
							
								if (canClickSpot(i, j, players, 1) > canClickSpot(best_i, best_j, players, 1))
								{
									best_i = i;
									best_j = j;
								}
								
							}
						}	
					}
					
					if (best_i != -1 && best_j != -1)
					{
						canClickSpot(best_i, best_j, players, 0);
						
						if (players == "White")
						{
							arrayButton[best_i][best_j].addCounter("White");
						}
						else
						{
							arrayButton[best_i][best_j].addCounter("Black");
						}
				
						updateboard(players, best_i, best_j);
						frame.pack();
					}
					gameOver(players);
					frame.pack();
					return;
				}
				
						
				for (int i = 0; i<8; i++)
				{
					for (int j = 0; j<8; j++)
					{
						if (source == arrayButton[i][j])
						{
							if (arrayButton[i][j].locked == 1)
							{
								return;
							}
							gameOver(players);
							if (canClickSpot(i, j, players, 0)==0)
							{
								return;
							}

							if (players == "White")
							{
								arrayButton[i][j].addCounter("White");
							}
							else
							{
								arrayButton[i][j].addCounter("Black");
							}
	
							updateboard(players, i, j);
							gameOver(players);
							frame.pack();
						}
						
					}
				}
				
			}			
		}
		
		public int canClickSpot(int i , int j, String current, int checking)
		{
			int taken = 0;
			int flipped = 0;
			int check = checking;
			
			// down take
			int row = i - 1;
			int change = 0;
			while (row > -1 && arrayButton[row][j].color != "none") {
				if (arrayButton[row][j].color != current)
					change = 1;
				row--;
			}
			if (arrayButton[++row][j].color == current && change == 1) {
				for (int k = row; k < i; k++) {
					if (arrayButton[k][j].color != current)
					{
						if (check == 0)
							arrayButton[k][j].swapCounter();
						flipped++;
					}
				}
				taken = 1;
			}
			
			
			// up take
			row = i + 1;
			change = 0;
			while (row < 8 && arrayButton[row][j].color != "none") {
				if (arrayButton[row][j].color != current)
					change = 1;
				row++;
			}
			if (arrayButton[--row][j].color == current && change == 1) {
				for (int k = row; k > i; k--) {
					if (arrayButton[k][j].color != current)
					{
						if (check == 0)
							arrayButton[k][j].swapCounter();
						flipped++;
					}
				}
				taken = 1;
			}
			
			
			// right take
			int column = j - 1;
			change = 0;
			while (column > -1 && arrayButton[i][column].color != "none") {
				if (arrayButton[i][column].color != current)
					change = 1;
				column--;
			}
			if (arrayButton[i][++column].color == current && change == 1) {
				for (int k = column; k < j; k++) {
					if (arrayButton[i][k].color != current)
					{
						if (check == 0)
							arrayButton[i][k].swapCounter();
						flipped++;
					}
				}
				taken = 1;
			}
			
			//left take
			column = j + 1;
			change = 0;
			while (column < 8 && arrayButton[i][column].color != "none") {
				if (arrayButton[i][column].color != current)
					change = 1;
				column++;
			}
			if (arrayButton[i][--column].color == current && change == 1) {
				for (int k = column; k > j; k--) {
					if (arrayButton[i][k].color != current)
					{
						if (check == 0)
							arrayButton[i][k].swapCounter();
						flipped++;
					}
				}
				taken = 1;
			}
			
			//top left take
			column = j + 1;
			row = i + 1;
			change = 0;
			while (column < 8 && row < 8 && arrayButton[row][column].color != "none")
			{
				if (arrayButton[row][column].color != current)
					change = 1;
				column++;
				row++;
			}
			if (arrayButton[--row][--column].color == current && change == 1) {
				while (i != row && j != column)
				{
					if (arrayButton[row][column].color != current)
					{
						if (check == 0)
							arrayButton[row][column].swapCounter();
						flipped++;
					}
					row--;
					column--;
				}
				taken = 1;
			}
			
			// top right take
			column = j - 1;
			row = i+ 1;
			change = 0;
			while (column > -1 && row < 8 && arrayButton[row][column].color != "none")
			{
				if (arrayButton[row][column].color != current)
					change = 1;
				column--;
				row++;
			}
			if (arrayButton[--row][++column].color == current && change == 1) {
				while (i != row && j != column)
				{
					if (arrayButton[row][column].color != current)
					{
						if (check == 0)
							arrayButton[row][column].swapCounter();
						flipped++;
					}
					row--;
					column++;
				}
				taken = 1;
			}
			
			// bottom left take
			column = j + 1;
			row = i - 1;
			change = 0;
			while (column < 8 && row > -1 && arrayButton[row][column].color != "none")
			{
				if (arrayButton[row][column].color != current)
					change = 1;
				column++;
				row--;
			}
			if (arrayButton[++row][--column].color == current && change == 1) {
				while (i != row && j != column)
				{
					if (arrayButton[row][column].color != current)
					{
						if (check == 0)
							arrayButton[row][column].swapCounter();
						flipped++;
					}
					row++;
					column--;
				}
				taken = 1;
			}
			
			// bottom right take
			column = j - 1;
			row = i - 1;
			change = 0;
			while (column > -1 && row > -1 && arrayButton[row][column].color != "none")
			{
				if (arrayButton[row][column].color != current)
					change = 1;
				column--;
				row--;
			}
			if (arrayButton[++row][++column].color == current && change == 1) {
				while (i != row && j != column)
				{
					if (arrayButton[row][column].color != current)
					{
						if (check == 0)
							arrayButton[row][column].swapCounter();
						flipped++;
					}
					row++;
					column++;
				}
				taken = 1;
			}
			
			if (checking == 1)
				return flipped;
			else if (checking == 2)
				return taken;
			return taken;
		}
		
	}



	public void updateboard(String players, int i, int j)
	{
		if (players == "White")
		{
			whiteboard.turnlabel.setText("White player - not your turn");
			whiteboard.turn = 0;
			blackboard.turnlabel.setText("Black player - click place to put piece");
			blackboard.turn = 1;
			
			for (int k = 0; k<8; k++)
			{
				for (int p = 0; p<8; p++)
				{
					if (whiteboard.arrayButton[k][p].color != blackboard.arrayButton[7-k][7-p].color)
					{
						if (blackboard.arrayButton[7-k][7-p].color == "none")
							blackboard.arrayButton[7-k][7-p].addCounter(whiteboard.arrayButton[k][p].color);
						else
							blackboard.arrayButton[7-k][7-p].swapCounter();
					}
				}
			}
		}
		else
		{
			whiteboard.turnlabel.setText("White player - click place to put piece");
			whiteboard.turn = 1;
			blackboard.turnlabel.setText("Black player - not your turn");
			blackboard.turn = 0;
			
			for (int k = 0; k<8; k++)
			{
				for (int p = 0; p<8; p++)
				{
					if (blackboard.arrayButton[k][p].color != whiteboard.arrayButton[7-k][7-p].color)
					{
						if (whiteboard.arrayButton[7-k][7-p].color == "none")
							whiteboard.arrayButton[7-k][7-p].addCounter(blackboard.arrayButton[k][p].color);
						else
							whiteboard.arrayButton[7-k][7-p].swapCounter();
					}
				}
			}
		}
	
	
		blackboard.frame.pack();
		whiteboard.frame.pack();
			
	}

	public void gameOver(String player) 
	{
		
		if (player == "Black")
		{
			for (int i = 0; i<8; i++)
			{
				for (int j = 0; j<8; j++)
				{
					if (blackboard.arrayButton[i][j].color == "none" && blackboard.canClickSpot(i, j, player, 2)!=0)
					{
						skipped = 0;
						return;
					}
				}
			}
			whiteboard.turnlabel.setText("White player - click place to put piece");
			whiteboard.turn = 1;
			blackboard.turnlabel.setText("Black player - not your turn");
			blackboard.turn = 0;
		}
		else
		{
			for (int i = 0; i<8; i++)
			{
				for (int j = 0; j<8; j++)
				{
					if (whiteboard.arrayButton[i][j].color == "none" && whiteboard.canClickSpot(i, j, player, 2)!=0)
					{
						skipped = 0;
						return;
					}
				}
			}
			whiteboard.turnlabel.setText("White player - not your turn");
			whiteboard.turn = 0;
			blackboard.turnlabel.setText("Black player - click place to put piece");
			blackboard.turn = 1;
		}
		blackboard.frame.pack();
		whiteboard.frame.pack();
		skipped++;
		if (skipped == 2 || checkBoardFilled() == 1)
		{
			int black = 0;
			int white = 0;
			for (int i = 0; i<8; i++)
			{
				for (int j = 0; j<8; j++)
				{
					if (whiteboard.arrayButton[i][j].color == "Black")
						black++;
					else if (whiteboard.arrayButton[i][j].color == "White")
						white++;
				}
			}
		
			JOptionPane pane = new JOptionPane();
			
			if (black > white) {
				JOptionPane.showMessageDialog(pane,
						"Game over! Black wins!\nBlack counter: " + black + "\nWhite Counter: " + white);
			}

			else if (black == white) {
				JOptionPane.showMessageDialog(pane,
						"Game over! Draw\nBlack counter: " + black + "\nWhite Counter: " + white);
			} else {
				JOptionPane.showMessageDialog(pane,
						"Game over! White wins!\nBlack counter: " + black + "\nWhite Counter: " + white);
			}
			
			reset();
		}
	}
	
	public int checkBoardFilled()
	{
		for (int i = 0; i<8; i++)
		{
			for (int j = 0; j<8; j++)
			{
				if (blackboard.arrayButton[i][j].color == "none" || whiteboard.arrayButton[i][j].color == "none")
					return 0;
			}
		}
		return 1;
	}
	
	public void reset()
	{
		whiteboard.frame.dispose();
		blackboard.frame.dispose();
		
		whiteboard = new Board("White", 1);
		blackboard = new Board("Black", 0);
		skipped = 0;
	}
	
}