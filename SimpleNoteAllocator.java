import java.util.Vector;
import java.util.Collections;

public class SimpleNoteAllocator implements INoteAllocator
{
	private Vector<Note> notes;
	private int maxFreq;
	private int upTime;

	public SimpleNoteAllocator(int maxFreq, int upTime)
	{
		this.maxFreq = maxFreq;
		this.notes = new Vector<Note>();
		this.upTime = upTime;
	}		

	public void Add(int t, int hold, int freq)
	{
		if(freq >= maxFreq)
			return;

		int reducedFreq = freq * 16 / maxFreq;
		Note n = new Note(t, hold, reducedFreq/4, reducedFreq%4);
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
