import java.util.HashSet;

public class TopHarmonicsTransform implements ITransform
{
	private ITransform in;
	private int N;

	public TopHarmonicsTransform(ITransform in, int N)
	{
		this.in = in;
		this.N = N;
	}

	public double[] Next()
	{
		double[] data = in.Next();
		if(data == null)
			return null;

		HashSet<int> maxs = new HashSet<int>();

		for(int m=0; m<N; m++)
		{
			double max = 0;
			int imax = -1;
			for(int i=0; i<data.length; i++)
			{
				if(maxs.contains(i) || data[i] < max)
					continue;
				imax = i;
				max = data[i];
			}
			maxs.add(imax);
		}


		double[] res = new double[data.length];
		for(int m: maxs)
		{
			res[m] = data[m];
		}

		return res;
	}
}
