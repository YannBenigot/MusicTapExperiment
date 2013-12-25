package org.musictap.trackgenerators.standard;
import org.musictap.interfaces.*;

import java.util.Vector;
import java.util.Collections;

public class SimpleNoteAllocator implements INoteAllocator
{
	private Vector<Note> notes;
	private int maxFreq;
	private int upTime;
	private INoteMapper noteMapper;
	final static int[][] mapping =
	{
		{ 0,  1,  5,  6},
		{ 2,  4,  7, 12},
		{ 3,  8, 11, 13},
		{ 9, 10, 14, 15}
	};

	public SimpleNoteAllocator(int maxFreq, int upTime)
	{
		this.maxFreq = maxFreq;
		this.notes = new Vector<Note>();
		this.upTime = upTime;
		this.noteMapper = new SimpleNoteMapper(mapping);
	}		

	public void Add(int t, int hold, int freq, double priority)
	{
		if(freq >= maxFreq)
			return;

		int reducedFreq = freq * 16 / maxFreq;

		Position p = noteMapper.Map(reducedFreq);
		Note n = new Note(t, hold, priority, p.X, p.Y);
		notes.add(n);
	}

	public Iterable<Note> Alloc()
	{
		int[] up = new int[16];
		Vector<Note> finalNotes = new Vector<Note>();

		Collections.sort(notes);

		int t = 0;
		for(Note n: notes)
		{
			for(; t<n.t; t++)
			{
				for(int i=0; i<16; i++)
					if(up[i] > 0)
						up[i]--;
			}

			int pos = n.x * 4 + n.y;

			if(up[pos] == 0)
			{
				finalNotes.add(n);
				up[pos] = n.hold + upTime;
			}
		}
		return finalNotes;
	}
}
