public class Position
{
	public int X;
	public int Y;
	public Position(int X, int Y)
	{
		this.X = X;
		this.Y = Y;
	}

	public int hashCode()
	{
		return X*4+Y;
	}

	public boolean equals(Object o)
	{
		Position p = (Position) o;
		return p.X == X && p.Y == Y;
	}
}