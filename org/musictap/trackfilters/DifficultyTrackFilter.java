package org.musictap.trackfilters;
import org.musictap.interfaces.*;

import java.util.Vector;
import java.util.LinkedList;
import java.util.Iterator;

public class DifficultyTrackFilter implements ITrackFilter
{
	private int maxNotesPerSec;

	public DifficultyTrackFilter(int maxNotesPerSec)
	{
		this.maxNotesPerSec = maxNotesPerSec;
	}

	public Track Filter(Track in)
	{
		Vector<Note> notes = new Vector<Note>();

		LinkedList<Note> currentNotes = new LinkedList<Note>();

		for(Note note: in.notes)
		{
			currentNotes.add(note);

			if(currentNotes.size() > maxNotesPerSec)
			{
				// First, do some cleanup
				Iterator<Note> it = currentNotes.iterator();
				while(it.hasNext())
				{
					Note currentNote = it.next();
					if(currentNote.t + 60 < note.t)
					{
						it.remove();
						notes.add(currentNote);
					}
				}

				// Still too many notes? Time to remove least important notes
				while(currentNotes.size() > maxNotesPerSec)
				{
					double worstPrio = 1e9;
					Note worstNote = null;
					for(Note currentNote: currentNotes)
					{
						if(currentNote.priority < worstPrio)
						{
							worstNote = currentNote;
							worstPrio = currentNote.priority;
						}
					}

					currentNotes.remove(worstNote);
				}
			}
		}

		notes.addAll(currentNotes);
		Track t = new Track();
		t.notes = notes;
		return t;
	}
}