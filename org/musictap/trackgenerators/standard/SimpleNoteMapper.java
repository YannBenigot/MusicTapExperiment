package org.musictap.trackgenerators.standard;
import org.musictap.interfaces.*;

public class SimpleNoteMapper implements INoteMapper
{
	private Position positions[];

	public SimpleNoteMapper(int[][] mapping)
	{
		positions = new Position[16];

		for(int x=0; x<4; x++)
			for(int y=0; y<4; y++)
				positions[mapping[x][y]] = new Position(x, y);
	}

	public Position Map(int ifreq)
	{
		return positions[ifreq];
	}
}