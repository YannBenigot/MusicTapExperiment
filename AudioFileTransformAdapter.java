public class AudioFileTransformAdapter implements ITransform
{
	private IAudioFile in;
	private int blockLength;

	public AudioFileTransformAdapter(int blockLength, IAudioFile in)
	{
		this.blockLength = blockLength;
		this.in = in;
	}

	public int BlockLength()
	{
		return blockLength;
	}

	public int StreamLength() throws AudioReadException
	{
		return in.GetLength() / blockLength;
	}

	public void Rewind() throws AudioReadException
	{
		in.Rewind();
	}

	public double[] Next() throws AudioReadException
	{
		double[] data = in.NextSamples(blockLength);
		return data;
	}
}
