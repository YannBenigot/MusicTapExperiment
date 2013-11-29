import java.util.Vector;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

class ChainNoteData implements Comparable<ChainNoteData>
{
	public int t;
	public int hold;
	public int freq;
	public ChainNoteData(int t, int hold, int freq)
	{
		this.t = t;
		this.hold = hold;
		this.freq = freq;
	}

	public int compareTo(ChainNoteData n)
	{
		return t-n.t;
	}
}

public class NoteChainsAllocator implements INoteAllocator
{
	private Vector<ChainNoteData> notes;
	private INoteMapper originMapper;
	private int maxFreq, timeout;

	final private static int[][] mapping =
	{
		{ 0,  1,  5,  6},
		{ 2,  4,  7, 12},
		{ 3,  8, 11, 13},
		{ 9, 10, 14, 15}
	};

	public NoteChainsAllocator(int maxFreq, int timeout)
	{
		notes = new Vector<ChainNoteData>();
		originMapper = new SimpleNoteMapper(mapping);
		this.maxFreq = maxFreq;
		this.timeout = timeout;
	}

	public void Add(int t, int hold, int freq)
	{
		notes.add(new ChainNoteData(t, hold, freq));
	}

	private class ChainState
	{
		public Iterator<ChainNoteData> it;
		public ChainNoteData el;
		public boolean up;
		public Position p;
		public INoteChainAllocator allocator;
	}

	public Iterable<Note> Alloc()
	{
		Collections.sort(notes);

		INoteChainsGenerator generator = new SimpleNoteChainsGenerator(0.3, 120);

		Iterable<Iterable<ChainNoteData>> chains = generator.GenerateNoteChains(notes);

		int up[][] = new int[4][4];

		Vector<Note> trackNotes = new Vector<Note>();

		HashMap<Iterable<ChainNoteData>, ChainState> states = new HashMap<Iterable<ChainNoteData>, ChainState>();
		int totalCount = 0;
		for(Iterable<ChainNoteData> chain: chains)
		{
			ChainState s = new ChainState();
			states.put(chain, s);
			s.it = chain.iterator();
			s.el = s.it.next();
			Position p = originMapper.Map((int)(16*Math.sqrt((double)s.el.freq/maxFreq)));
			s.p = p;
			s.up = true;
			s.allocator = new AngleNoteChainAllocator(2, p.X, p.Y);
			totalCount++;
		}
		System.out.format("Got %d chains on %d notes\n", totalCount, notes.size());

		int t = 0;
		int finishedCount = 0;
		int skipped = 0;
		while(finishedCount < totalCount)
		{
			for(Iterable<ChainNoteData> chain: chains)
			{
				ChainState state = states.get(chain);
				if(state.el.t == t)
				{
					HashSet<Position> positions = new HashSet<Position>();
					while(up[state.p.X][state.p.Y] != 0)
					{
						positions.add(state.p);
						state.p = state.allocator.Next(state.up);
						if(positions.contains(state.p))
						{
							skipped++;
							break;
						}
					}

					if(up[state.p.X][state.p.Y] == 0)
					{
						Note n = new Note(state.el.t, state.el.hold, state.p.X, state.p.Y);
						trackNotes.add(n);
						up[state.p.X][state.p.Y] = timeout + state.el.hold;
					}

					do
					{	if(state.it.hasNext())
						{
							ChainNoteData nel = state.it.next();
							state.up = nel.freq >= state.el.freq;
							state.el = nel;
							state.p = state.allocator.Next(state.up);
						}
						else
						{
							skipped++;
							finishedCount++;
							break;
						}
					} while(state.el.t == t);
				}
			}

			t++;
			for(int x=0; x<4; x++)
				for(int y=0; y<4; y++)
					if(up[x][y] > 0)
						up[x][y]--;
		}

		System.out.format("Skipped %d notes", skipped);

		return trackNotes;
	}
}