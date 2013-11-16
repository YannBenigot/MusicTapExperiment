import java.io.File;
import java.io.IOException;

public class WavAudioFile implements IAudioFile
{
	private WavFile file;
	public WavAudioFile(String filename) throws AudioReadException
	{
		try
		{
			file = WavFile.openWavFile(new File(filename));
			file.display();
		}
		catch(Exception e)
		{
			throw new AudioReadException(e);
		}
	}

	public int GetLength()
	{
		return (int) file.getNumFrames()/file.getNumChannels();
	}

	public double[] NextSamples(int length) throws AudioReadException
	{
		try
		{
			double[][] frames = new double[file.getNumChannels()][length];
			file.readFrames(frames, length);

			double[] samples = new double[length];
			for(int i=0; i<length; i++)
			{
				for(int channel = 0; channel < file.getNumChannels(); channel++)
				{
					samples[i] += frames[channel][i];
				}
				samples[i] /= file.getNumChannels();
			}

			return samples;
		}
		catch(Exception e)
		{
			throw new AudioReadException(e);
		}
	}
}

