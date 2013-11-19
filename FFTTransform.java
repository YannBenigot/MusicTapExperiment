public class FFTTransform implements ITransform
{
	private IFFT fft;
	private ITransform in;

	public FFTTransform(ITransform in, IFFT fft)
	{
		this.in = in;
		this.fft = fft;
	}

	public double[] Next() throws AudioReadException
	{
		double[] res = in.Next();
		if(res == null)
			return null;

		return fft.GetFFT(res);
	}
}

