import java.util.Vector;

public class SimpleNoteAllocator implements INoteAllocator
{
	private Vector<Note> notes;
	private int maxFreq;

	public SimpleNoteAllocator(int maxFreq)
	{
		this.maxFreq = maxFreq;
		this.notes = new Vector<Note>();
	}		

	public void Add(int t, int freq)
	{
		if(freq >= maxFreq)
			return;

		int reducedFreq = freq * 16 / maxFreq;
		Note n = new Note(t, reducedFreq/4, reducedFreq%4);
		notes.add(n);
	}

	public Iterable<Note> Alloc()
	{
		return notes;
	}
}
