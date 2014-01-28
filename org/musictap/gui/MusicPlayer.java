package org.musictap.gui;

import org.musictap.interfaces.AudioReadException;
import org.musictap.interfaces.IAudioFile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;

public class MusicPlayer
{
	private static final int AudioBufferSize = 256;
	private IMusicPlayerFile file;
	private int musicPos;
	
	public MusicPlayer(IMusicPlayerFile file)
	{
		this.file = file;
	}
	
	public void Run()
	{
		AudioDevice device = Gdx.audio.newAudioDevice(file.GetSampleRate(), file.GetChannelCount() == 1); // TODO: more than 2 channels?

		musicPos = 0;
		System.out.printf("%d\n", device.getLatency());
		
		short[] buffer = new short[AudioBufferSize]; 
		while(true)
		{
			try
			{
				int read = file.NextSamples(buffer, AudioBufferSize);
			
				device.writeSamples(buffer, 0, AudioBufferSize);
				musicPos += read * 1000 / file.GetSampleRate();
				
				if(read != AudioBufferSize)
					break;
			}
			catch(AudioReadException ex)
			{
				ex.printStackTrace();
				break;
			}
		}
	}
	
	
}
