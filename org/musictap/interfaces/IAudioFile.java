package org.musictap.interfaces;

public interface IAudioFile
{
	int GetLength() throws AudioReadException;
	double[] NextSamples(int length) throws AudioReadException;
	void Rewind() throws AudioReadException;
}

