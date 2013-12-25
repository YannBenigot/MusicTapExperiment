package org.musictap.transforms;
import org.musictap.interfaces.*;

import java.util.Arrays;

public class KeepTransform extends BaseTransform
{
	private int N, newCount, overlap;
	private double[] data;

	public KeepTransform(int blockCount, ITransform in) throws AudioReadException
	{
		super(in);
		this.N = blockCount * in.BlockLength();
		this.overlap = (blockCount - 1) * in.BlockLength();
		this.newCount = N - overlap;
		this.data = new double[N];
	}

	public int BlockLength()
	{
		return N;
	}

	public double[] Next() throws AudioReadException
	{
		double[] newData = in.Next();
		if(newData == null)
			return null;

		for(int i=0; i<overlap; i++)
			data[i] = data[i+newCount];

		for(int i=0; i<newCount; i++)
			data[i+overlap] = newData[i];

		return data.clone();
	}

	public void Rewind() throws AudioReadException
	{
		Arrays.fill(data, 0);
		in.Rewind();
	}
}
