package board;

import java.awt.Color;

import javax.swing.JButton;

public class GridLabel extends JButton {

	Integer locked = 0;
	String color = "none";
	Counter counter;
	
	
	public void addCounter(String countercolor)
	{
		counter = new Counter(countercolor);
		this.add(counter);
		locked = 1;
		color = countercolor;
		this.updateUI();
	}

	public void swapCounter()
	{
		this.removeAll();
		this.repaint();
		
		if (this.color == "White")
		{
			this.addCounter("Black");
			this.color = "Black";
		}
		else
		{
			this.addCounter("White");
			this.color = "White";
		}
	}
	
}
