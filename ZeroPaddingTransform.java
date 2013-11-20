public class ZeroPaddingTransform implements ITransform
{
	private ITransform in;
	private int N;

	public ZeroPaddingTransform(ITransform in, int N)
	{
		this.N = N;
		this.in = in;
	}

	public double[] Next() throws AudioReadException
	{
		double[] result = new double[N];
		double[] data = in.Next();

		for(int i=0; i<data.length; i++)
			result[i] = data[i];
		
		return result;
	}
}

