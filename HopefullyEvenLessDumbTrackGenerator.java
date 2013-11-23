import java.util.Vector;

public class HopefullyEvenLessDumbTrackGenerator implements ITrackGenerator
{
	private ITransform GetTransform(IAudioFile file) throws AudioReadException
	{
		return new IgnoreSmallHarmonicsTransform(0.5, new TopHarmonicsTransform(8, new PeakTransform(16, new FFTTransform(new JTransformsFFT(8192), new WindowTransform(new HannWindow(), 8192, new KeepTransform(8, new AudioFileTransformAdapter(1024, file)))))));
	}

	final static int MaxFreq = 512;
	final static int Timeout = 10;
	final static double factor = 0.9;
	final static double EnergyFactor = 0.3;

	public Track GenerateTrack(IAudioFile file) throws AudioReadException
	{
		ITransform in = GetTransform(file);
		INoteAllocator noteAllocator = new SimpleNoteAllocator(MaxFreq);
		int blockLength = in.BlockLength();
		double avgEnergy = 0;
		double energy = 0;

		int up[] = new int[16];

		for(int i=0; i<in.StreamLength(); i++)
		{
			double[] data = in.Next();
			double avg = 0;
			int count = 0;

			if(i < 3)
				continue;

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
			//System.out.format("%f %f\n", energy, avgEnergy);

			if(energy >= EnergyFactor * avgEnergy)
			{
				for(int j=0; j<MaxFreq; j++)
				{
					if(data[j] > 0.0)
					{
						int pos = j * 16 / MaxFreq;
	
						if(up[pos] == 0)
							noteAllocator.Add((i-3) * 1024 * 60 / 44100, j);
	
						up[pos] = Timeout;
					}
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
