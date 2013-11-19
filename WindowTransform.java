public class WindowTransform implements ITransform
{
	private int N;
	private ITransform in;
	double[] windowData;
	IWindow window;

	public WindowTransform(ITransform in, IWindow window, int N)
	{
		this.N = N;
		this.in = in;

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

