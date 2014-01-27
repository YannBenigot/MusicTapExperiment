package org.musictap.trackimporters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Vector;

import org.musictap.interfaces.Note;
import org.musictap.interfaces.Track;


public class YubiosiTrackImporter implements ITrackImporter
{

	@Override
	public Track ImportTrack(Reader stream) throws IOException
	{
		BufferedReader reader = new BufferedReader(stream);
		String name, wtf1, bpmS, wtf2, wtf3, noteCountS;
		int noteCount;
		float bpm;
		
		name = reader.readLine();
		wtf1 = reader.readLine();
		bpmS = reader.readLine();
		wtf2 = reader.readLine();
		wtf3 = reader.readLine();
		noteCountS = reader.readLine();
		
		bpm = Float.parseFloat(bpmS);
		noteCount = Integer.parseInt(noteCountS);
		
		Vector<Note> notes = new Vector<Note>();
		
		for(int i=0; i<noteCount; i++)
		{
			int time = Integer.parseInt(reader.readLine());
			// Yubiosi: time unit is 1/4 beat
			// This project: time unit is ms
			notes.add(new Note((int) (time / (bpm / 60) * 1000 / 4), 1, 1, 0, 0));
		}
		
		for(int i=0; i<noteCount; i++)
		{
			int noteExp = Integer.parseInt(reader.readLine());
			int note = (int)(Math.log(noteExp)/Math.log(2)); // Should probably be an AND of several notes, but we don't care about that right now
			notes.get(i).x = note%4;
			notes.get(i).y = note/4;
		}
		
		Track track = new Track();
		track.notes = notes;
		
		return track;
	}

}
