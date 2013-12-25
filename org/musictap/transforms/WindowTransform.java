package org.musictap.transforms;
import org.musictap.interfaces.*;

public class WindowTransform extends BaseTransform
{
	private int N;
	double[] windowData;
	IWindow window;

	public WindowTransform(IWindow window, int N, ITransform in)
	{
		super(in);
		this.N = N;

		this.windowData = new double[N];
		for(int i=0; i<N; i++)
			this.windowData[i] = window.window(((double)i)/N);
	}

	public double[] Next() throws AudioReadException
	{
		double[] out = in.Next();
		if(out == null)
			return null;

		for(int i=0; i<N; i++)
			out[i] *= windowData[i];

		return out;
	}
}

