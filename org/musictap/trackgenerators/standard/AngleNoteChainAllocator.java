package org.musictap.trackgenerators.standard;
import org.musictap.interfaces.*;

// Inexact implementation. A proper one will be a pain, so keeping it simple for now
public class AngleNoteChainAllocator implements INoteChainAllocator
{
	private int dx, dy;
	private int x, y;

	public AngleNoteChainAllocator(int angle, int x, int y)
	{
		this.x = x;
		this.y = y;
		switch(angle)
		{
			case 0: dx = 1; dy = 0; break;
			case 1: dx = 1; dy = 1; break;
			case 2: dx = 0; dy = 1; break;
			case 3: dx = -1; dy = 1; break;
			case 4: dx = -1; dy = 0; break;
			case 5: dx = -1; dy = -1; break;
			case 6: dx = 0; dy = -1; break;
			case 7: dx = 1; dy = -1; break;
			default: dx = 0; dy = 0; break;
		}
	}

	public Position Next(boolean up)
	{
		int a = (up ? 1 : -1);
		x += a*dx;
		y += a*dy;
		boolean col = false;

		if(x < 0)
		{
			x = 1;
			dx *= -1;
			col = true;
		}

		if(y < 0)
		{
			y = 1;
			dy *= -1;
			col = true;
		}

		if(x >= 4)
		{
			x = 2;
			dx *= -1;
			col = true;
		}

		if(y >= 4)
		{
			y = 2;
			dy *= -1;
			col = true;
		}

		return new Position((int)x, (int)y);
	}
}