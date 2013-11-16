import java.util.Vector;

class LessDumbTrackGenerator implements ITrackGenerator
{
	static int MinTime = 30;
	static int FrameSize = 1024;
	public Track GenerateTrack(IAudioData data) throws AudioReadException
	{
		double energy[] = new double[data.GetLength()/FrameSize];
		double avg = 0;

		for(int i=0; i<data.GetLength()/FrameSize-1; i++)
		{
			double[] samples = data.NextSamples(FrameSize);
			double e = 0;
			for(int j=0; j<FrameSize; j++)
			{
				e += samples[j] * samples[j];
			}
			energy[i] = e;
			
			avg += e;
		}

		avg /= (data.GetLength() / FrameSize);

		boolean up = false;
		Vector<Note> notes = new Vector<Note>();
		for(int i=0; i<data.GetLength()/FrameSize-1; i++)
		{
			if(!up && energy[i] > avg)
			{
				int t = i * 1024 * 60 / 44100;
				up = true;
				notes.add(new Note(t, (int)(Math.random()*4), (int)(Math.random()*4)));
			}

			if(up && energy[i] <= avg)
				up = false;
		}

		Track t = new Track();
		t.notes = notes;
		return t;
	}
}

