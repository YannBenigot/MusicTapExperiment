public class Note implements Comparable<Note>
{
	public int t;
	public int x, y;
	int hold;

	public Note(int _t, int _hold, int _x, int _y)
	{
		t = _t;
		x = _x;
		y = _y;
		hold = _hold;
	}

	public int compareTo(Note n)
	{
		return t-n.t;
	}
}
