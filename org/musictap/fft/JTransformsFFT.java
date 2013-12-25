package org.musictap.fft;
import org.musictap.interfaces.*;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class JTransformsFFT implements IFFT
{
	private DoubleFFT_1D fft;
	public JTransformsFFT(int n)
	{
		fft = new DoubleFFT_1D(n);
	}

	public double[] GetFFT(double[] samples)
	{
		double[] result = new double[samples.length/2];
		fft.realForward(samples);

		for(int i=0; i<samples.length/2; i++)
			result[i] = Math.sqrt(samples[2*i] * samples[2*i] + samples[2*i+1] * samples[2*i+1]);

		return result;
	}
}

