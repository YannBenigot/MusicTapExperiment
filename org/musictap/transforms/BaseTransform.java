package org.musictap.transforms;
import org.musictap.interfaces.*;

public abstract class BaseTransform implements ITransform
{
	protected ITransform in;

	public BaseTransform(ITransform in)
	{
		this.in = in;
	}

	public int BlockLength() throws AudioReadException
	{
		return in.BlockLength();
	}

	public int StreamLength() throws AudioReadException
	{
		return in.StreamLength();
	}

	public double[] Next() throws AudioReadException
	{
		return in.Next();
	}

	public void Rewind() throws AudioReadException
	{
		in.Rewind();
	}
}

