package org.musictap.gui;

import org.musictap.interfaces.ITransform;
import org.musictap.interfaces.Track;

public class TrackPlayMode implements IMode
{
	private Track track;
	private long start;
	public TrackPlayMode(Track track, ITransform transform)
	{
		this.track = track;
		this.header = new DebugHeader(transform);
		this.cells = new NoteCell[4][4];
		for(int i=0; i<16; i++)
			cells[i/4][i%4] = new NoteCell(i/4, i%4, track);
		this.start = -1;
	}
	
	private DebugHeader header;
	@Override
	public IHeader GetHeader()
	{
		return header;
	}

	private NoteCell[][] cells;
	@Override
	public ICell[][] GetCells()
	{
		return cells;
	}

	@Override
	public void Render(long time)
	{
	}

	public void Start()
	{
		start = System.currentTimeMillis();
	}
}
