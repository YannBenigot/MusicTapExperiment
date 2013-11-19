public class KeepTransform implements ITransform
{
	private IAudioFile in;
	private int N, overlap;
	private int newCount;
	private double[] data;

	public KeepTransform(IAudioFile file, int N, int overlap)
	{
		this.in = file;
		this.N = N;
		this.newCount = N - overlap;
		this.overlap = overlap;
		this.data = new double[N];
	}

	public double[] Next() throws AudioReadException
	{
		double[] newData = in.NextSamples(newCount);
		if(newData == null)
			return null;

		for(int i=0; i<overlap; i++)
			data[i] = data[i+newCount];

		for(int i=0; i<newCount; i++)
			data[i+overlap] = newData[i];

		return data.clone();
	}

}
