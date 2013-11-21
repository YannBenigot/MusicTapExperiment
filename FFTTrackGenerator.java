import java.util.Vector;

class FFTTrackGenerator implements ITrackGenerator
{
	static int MinTime = 30;
	static int FrameSize = 1024;
	static int UsedHarmonics = 185; // MAGIC
	static int NoteWidth = UsedHarmonics / 16;
	static double SmoothFactor = 0.9;
	static double NoteFactor = 3;

	public Track GenerateTrack(IAudioFile data) throws AudioReadException
	{
		IFFT fftComputer = new JTransformsFFT(FrameSize);
		double average[] = new double[16];
		double energy[] = new double[16];
		double avg = 0;
		Vector<Note> notes = new Vector<Note>();

		for(int i=0; i<3; i++)
		{
			double[] fft = fftComputer.GetFFT(data.NextSamples(FrameSize));
			for(int j=0; j<16; j++)
			{
				energy[j] = 0;
				for(int k=0; k<NoteWidth; k++)
					energy[j] += fft[1+j*NoteWidth+k] * fft[1+j*NoteWidth+k];
			}
			
			for(int j=0; j<16; j++)
				average[j] += energy[j]/3;
		}

		for(int i=3; i<data.GetLength()/FrameSize-1; i++)
		{
			int t = i * 1024 * 60 / 44100;
			double[] fft = fftComputer.GetFFT(data.NextSamples(FrameSize));

			for(int j=0; j<16; j++)
			{
				energy[j] = 0;
				for(int k=0; k<NoteWidth; k++)
					energy[j] += fft[1+j*NoteWidth+k] * fft[1+j*NoteWidth+k];
			}
			
			for(int j=0; j<16; j++)
				average[j] = average[j] * SmoothFactor + energy[j] * (1-SmoothFactor);

			for(int j=0; j<16; j++)
			{
				if(energy[j] >= NoteFactor * average[j])
				{
					notes.add(new Note(t, j/4, j%4));
				}
			}
		}

		Track t = new Track();
		t.notes = notes;
		return t;
	}
}

