public interface IAudioFile
{
	public int GetLength() throws AudioReadException;
	public double[] NextSamples(int length) throws AudioReadException;
}

