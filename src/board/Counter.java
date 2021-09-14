package board;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Counter extends JPanel {
	
	String player = "";
	
	
	public Counter(String player)
	{
		this.player = player;
	}
	
    public void paintComponent(Graphics g)
    {
    	if (player == "White")
    	{
	    	g.setColor(Color.black);
			g.fillOval(3, 0, 50, 50);
			g.setColor(Color.white);
			g.fillOval(5, 2, 46, 46);
	    }
    	else
    	{
    		g.setColor(Color.white);
			g.fillOval(3, 0, 50, 50);
			g.setColor(Color.black);
			g.fillOval(5, 2, 46, 46);
    	}
    }

}
