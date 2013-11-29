import java.util.Iterator;
import java.util.Vector;

public class SimpleNoteChainsGenerator implements INoteChainsGenerator
{
	private double freqFactor;
	private int maxChainTime;

	public SimpleNoteChainsGenerator(double freqFactor, int maxChainTime)
	{
		this.freqFactor = freqFactor;
		this.maxChainTime = maxChainTime;
	}

	public Iterable<Iterable<ChainNoteData>> GenerateNoteChains(Iterable<ChainNoteData> notes)
	{
		Vector<Vector<ChainNoteData>> currentChains = new Vector<Vector<ChainNoteData>>();
		Vector<Iterable<ChainNoteData>> readyChains = new Vector<Iterable<ChainNoteData>>();

		for(ChainNoteData note: notes)
		{
			boolean found = false;
			for(Vector<ChainNoteData> chain: currentChains)
			{
				ChainNoteData l = chain.lastElement();
				if(Math.abs(l.freq-note.freq) < freqFactor * note.freq)
				{
					chain.add(l);
					found = true;
					break;
				}
			}

			if(!found)
			{
				Vector<ChainNoteData> newChain = new Vector<ChainNoteData>();
				newChain.add(note);
				currentChains.add(newChain);
			}

			Iterator<Vector<ChainNoteData>> it = currentChains.iterator();
			while(it.hasNext())
			{
				Vector<ChainNoteData> chain = it.next();
				ChainNoteData l = chain.lastElement();
				if(note.t > maxChainTime + l.hold + l.t)
				{
					it.remove();
					readyChains.add(chain);
				}
			}
		}

		readyChains.addAll(currentChains);

		return readyChains;
	}
}