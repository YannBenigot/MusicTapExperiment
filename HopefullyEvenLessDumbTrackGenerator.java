import java.util.Vector;

public class HopefullyEvenLessDumbTrackGenerator implements ITrackGenerator
{
	private ITransform GetTransform(IAudioFile file) throws AudioReadException
	{
		return new TopHarmonicsTransform(8, new PeakTransform(16, new FFTTransform(new JTransformsFFT(8192), new WindowTransform(new HannWindow(), 8192, new KeepTransform(8, new AudioFileTransformAdapter(1024, file))))));
	}

	final static double NoteKeepFactor = 0.05;
	final static int MaxFreq = 512;
	final static int Timeout = 3;

	public Track GenerateTrack(IAudioFile file) throws AudioReadException
	{
		ITransform in = GetTransform(file);
		INoteAllocator noteAllocator = new SimpleNoteAllocator(MaxFreq);
		int blockLength = in.BlockLength();

		int up[] = new int[16];

		for(int i=0; i<in.StreamLength(); i++)
		{
			double[] data = in.Next();
			double avg = 0;
			int count = 0;

			if(i < 3)
				continue;

			for(int j=0; j<MaxFreq; j++)
			{
				if(data[j] == 0.0)
					continue;

				avg += data[j];
				count++;
			}

			avg /= count;

			for(int j=0; j<MaxFreq; j++)
			{
				if(data[j] > avg * NoteKeepFactor)
				{
					int pos = j * 16 / MaxFreq;

					if(up[pos] == 0)
						noteAllocator.Add((i-3) * blockLength * 60 / 44100, j);

					up[pos] = Timeout;
				}
			}

			for(int j=0; j<16; j++)
				if(up[j] > 0)
					up[j]--;
		}

		Track t = new Track();
		t.notes = noteAllocator.Alloc();
		return t;
	}
}
