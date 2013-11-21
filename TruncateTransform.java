public class TruncateTransform extends BaseTransform
{
	private int N;

	public TruncateTransform(int N, ITransform in)
	{
		super(in);
		this.N = N;
	}

	public int BlockLength()
	{
		return N;
	}

	public double[] Next() throws AudioReadException
	{
		double[] data = in.Next();
		double[] res = new double[N];

		for(int i=0; i<N; i++)
			res[i] = data[i];

		return res;
	}
}
