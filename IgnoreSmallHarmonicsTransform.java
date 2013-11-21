public class IgnoreSmallHarmonicsTransform extends BaseTransform
{
	private double keepFactor;

	public IgnoreSmallHarmonicsTransform(double keepFactor, ITransform in)
	{
		super(in);
		this.keepFactor = keepFactor;
	}

	public double[] Next() throws AudioReadException
	{
		double[] data = in.Next();
		double avg = 0.0;
		int count = 0;

		for(int i=0; i<data.length; i++)
		{
			if(data[i] != 0.0)
			{
				avg += data[i];
				count++;
			}
		}

		avg /= count;

		for(int i=0; i<data.length; i++)
			if(data[i] < avg * keepFactor)
				data[i] = 0;

		return data;
	}
}

