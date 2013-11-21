public class ZeroPaddingTransform extends BaseTransform
{
	private int N;

	public ZeroPaddingTransform(int N, ITransform in)
	{
		super(in);
		this.N = N;
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

