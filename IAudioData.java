public interface IAudioData
{
	public int GetLength() throws AudioReadException;
	public double[] NextSamples(int length) throws AudioReadException;
	public double[] NextFFT(int length) throws AudioReadException;
}

