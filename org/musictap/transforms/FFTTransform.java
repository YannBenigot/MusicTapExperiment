package org.musictap.transforms;
import org.musictap.interfaces.*;

public class FFTTransform extends BaseTransform
{
	private IFFT fft;

	public FFTTransform(IFFT fft, ITransform in)
	{
		super(in);
		this.fft = fft;
	}

	public int BlockLength() throws AudioReadException
	{
		return in.BlockLength() / 2;
	}

	public double[] Next() throws AudioReadException
	{
		double[] res = in.Next();
		if(res == null)
			return null;

		return fft.GetFFT(res);
	}
}

