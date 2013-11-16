public class AudioData implements IAudioData
{
	public IAudioFile file;
	public IFFT fft;

	public AudioData(IAudioFile _file, IFFT _fft)
	{
		fft = _fft;
		file = _file;
	}

	public int GetLength() throws AudioReadException
	{
		return file.GetLength();
	}

	public double[] NextSamples(int length) throws AudioReadException
	{
		return file.NextSamples(length);
	}

	public double[] NextFFT(int length) throws AudioReadException
	{
		double[] samples = file.NextSamples(length);

		return fft.GetFFT(samples);
	}
}
