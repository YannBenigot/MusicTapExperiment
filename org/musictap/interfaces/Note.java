package org.musictap.interfaces;

public class Note implements Comparable<Note>
{
	public int t;
	public int x, y;
	public int hold;
	public double priority;

	public Note(int _t, int _hold, double _priority, int _x, int _y)
	{
		t = _t;
		x = _x;
		y = _y;
		hold = _hold;
		priority = _priority;
	}

	public int compareTo(Note n)
	{
		return t-n.t;
	}
}
