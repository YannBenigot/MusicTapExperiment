public class PeakTransform implements ITransform
{
	private int neigh;
	private ITransform in;

	public PeakTransform(ITransform in, int neigh)
	{
		this.neigh = neigh;
		this.in = in;
	}

	public double[] Next() throws AudioReadException
	{
		double[] data = in.Next();
		double[] res = new double[data.length];
		if(res == null)
			return null;

		for(int i=0; i<data.length; i++)
		{
			int j;
			for(j=-neigh; j<=neigh; j++)
			{
				if(j == 0 || i+j < 0 || i+j >= data.length)
					continue;
				
				if(data[i+j] > data[i])
					break;
			}

			if(j == neigh+1)
				res[i] = data[i];
		}

		return res;
	}
}
