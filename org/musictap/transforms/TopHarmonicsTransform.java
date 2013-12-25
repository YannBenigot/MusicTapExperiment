package org.musictap.transforms;
import org.musictap.interfaces.*;

public class TopHarmonicsTransform extends BaseTransform
{
	private int N;

	public TopHarmonicsTransform(int N, ITransform in)
	{
		super(in);
		this.in = in;
		this.N = N;
	}

	public double[] Next() throws AudioReadException
	{
		double[] data = in.Next();
		if(data == null)
			return null;

		boolean maxs[] = new boolean[data.length];

		for(int m=0; m<N; m++)
		{
			double max = -1.0;
			int imax = -1;
			for(int i=0; i<data.length; i++)
			{
				if(maxs[i] || data[i] < max)
					continue;
				imax = i;
				max = data[i];
			}
			maxs[imax] = true;
		}


		double[] res = new double[data.length];
		for(int i=0; i<data.length; i++)
		{
			if(maxs[i])
				res[i] = data[i];
		}

		return res;
	}
}
