import java.util.Vector;
import java.util.Collections;
import java.util.Iterator;

class NoteData
{
	public int t;
	public int hold;
	public int freq;
	public boolean keep;
	public boolean updated;

	public NoteData(int t, int freq, boolean keep)
	{
		this.t = t;
		this.hold = 1;
		this.freq = freq;
		this.keep = keep;
		this.updated = true;
	}
}

public class HopefullyEvenLessDumbTrackGenerator implements ITrackGenerator
{
	final static int MaxNote = 8;

	private ITransform GetTransform(IAudioFile file) throws AudioReadException
	{
		return 
		new IgnoreSmallHarmonicsTransform(0.5, 
			new TopHarmonicsTransform(MaxNote, 
				new PeakTransform(16, 
					new FFTTransform(new JTransformsFFT(8192), 
						new WindowTransform(new HannWindow(), 8192, 
							new KeepTransform(8, 
								new AudioFileTransformAdapter(1024, file)))))));
	}

	final static int MaxFreq = 512;
	final static int Timeout = 10;
	final static double factor = 0.9;
	final static double EnergyFactor = 0.3;
	final static int MaxFreqSpacing = 3;
	final static int MinHoldTime = 30;

	public Track GenerateTrack(IAudioFile file) throws AudioReadException
	{
		ITransform in = GetTransform(file);
		INoteAllocator noteAllocator = new SimpleNoteAllocator(MaxFreq, Timeout);
		int blockLength = in.BlockLength();
		double avgEnergy = 0;
		double energy = 0;

		Vector<NoteData> waitingNotes = new Vector<NoteData>();

		for(int i=0; i<in.StreamLength(); i++)
		{
			double[] data = in.Next();
			double avg = 0;
			int count = 0;

			if(i < 3)
				continue;

			// Get new notes
			energy = 0;
			for(int j=0; j<MaxFreq; j++)
			{
				if(data[j] == 0.0)
					continue;

				avg += data[j];
				energy += data[j] * data[j];
				count++;
			}

			avg /= count;
			avgEnergy = avgEnergy * factor + energy * (1 - factor);

			boolean keep = energy >= EnergyFactor * avgEnergy;

			for(int j=0; j<MaxFreq; j++)
			{
				if(data[j] > 0.0)
				{
					boolean noteFound = false;
					for(NoteData noteData : waitingNotes)
					{
						if(Math.abs(noteData.freq-j) <= MaxFreqSpacing)
						{
							noteData.updated = true;
							noteData.keep = noteData.keep || (keep && data[j] >= avg);
							noteData.hold++;
							noteFound = true;
							break;
						}
					}

					if(!noteFound)
					{
						NoteData noteData = new NoteData(i, j, keep && data[j] >= avg);
						waitingNotes.add(noteData);
					}
					
				}
			}

			Iterator<NoteData> noteIt = waitingNotes.iterator();
			while(noteIt.hasNext())
			{
				NoteData noteData = noteIt.next();

				if(noteData.keep && !noteData.updated)
					noteAllocator.Add((noteData.t-3) * 1024 * 60 / 44100, (noteData.hold > MinHoldTime ? noteData.hold : 1), noteData.freq);

				if(!noteData.updated)
					noteIt.remove();

				noteData.updated = false;
			}
		}

		Track t = new Track();
		t.notes = noteAllocator.Alloc();
		return t;
	}
}
