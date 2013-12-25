package org.musictap.trackgenerators;
import org.musictap.interfaces.*;

import java.util.Vector;

class DumbTrackGenerator implements ITrackGenerator
{
	public Track GenerateTrack(IAudioData data)
	{
		Track track = new Track();
		Vector<Note> notes = new Vector<Note>();
		for(int x=0; x<4; x++)
			for(int y=0; y<4; y++)
			{
				notes.add(new Note(30+30*(4*x+y), x, y));
			}

		track.notes = notes;
		return track;
	}
}
