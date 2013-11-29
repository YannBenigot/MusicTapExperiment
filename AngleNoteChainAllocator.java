// Inexact implementation. A proper one will be a pain, so keeping it simple for now
public class AngleNoteChainAllocator implements INoteChainAllocator
{
	private double dx, dy;
	private double x, y;

	public AngleNoteChainAllocator(double angle, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.dx = Math.cos(angle);
		this.dy = Math.sin(angle);
	}

	public Position Next(boolean up)
	{
		int a = (up ? 1 : -1);
		x += a*dx;
		y += a*dy;
		boolean col = false;

		if(x < 0)
		{
			x *= -1;
			dx *= -1;
			col = true;
		}

		if(y < 0)
		{
			y *= -1;
			dy *= -1;
			col = true;
		}

		if(x >= 4)
		{
			x = 8-x;
			dx *= -1;
			col = true;
			if(x == 4) // Puking permitted
				x -= 1;
		}

		if(y >= 4)
		{
			y = 8-y;
			dy *= -1;
			col = true;
			if(y == 4)
				y -= 1;
		}

		if(col)
			return Next(up);

		return new Position((int)x, (int)y);
	}
}