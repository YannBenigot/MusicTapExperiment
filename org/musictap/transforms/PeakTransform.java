package org.musictap.transforms;
import org.musictap.interfaces.*;

public class PeakTransform extends BaseTransform
{
	private int neigh;

	public PeakTransform(int neigh, ITransform in)
	{
		super(in);
		this.neigh = neigh;
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
