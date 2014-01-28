package org.musictap.gui;

import java.io.File;

import org.musictap.interfaces.AudioReadException;

import wf.WavFile;

public class WavMusicPlayerFile implements IMusicPlayerFile
{
	private WavFile file;
	private int[] tmpBuffer;
	private final static int tmpBufferSize = 131072;
	
	public WavMusicPlayerFile(String filename) throws AudioReadException
	{
		try
		{
			file = WavFile.openWavFile(new File(filename));
			file.display();
			tmpBuffer = new int[tmpBufferSize];
		}
		catch(Exception exc)
		{
			throw new AudioReadException(exc);
		}
	}
	
	@Override
	public int GetSampleRate()
	{
		return (int) file.getSampleRate();
	}

	@Override
	public int GetChannelCount()
	{
		return file.getNumChannels();
	}

	@Override
	public int NextSamples(short[] buffer, int count) throws AudioReadException
	{
		try
		{
			int read = file.readFrames(tmpBuffer, count / file.getNumChannels()) * file.getNumChannels();
			
			for(int i=0; i<read; i++)
				buffer[i] = (short) tmpBuffer[i];
			
			return read;
		}
		catch(Exception exc)
		{
			throw new AudioReadException(exc);
		}
	}

}
