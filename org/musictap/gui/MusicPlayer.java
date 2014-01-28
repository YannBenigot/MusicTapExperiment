package org.musictap.gui;

import org.musictap.interfaces.AudioReadException;
import java.util.concurrent.locks.ReentrantLock;
import org.musictap.interfaces.IAudioFile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;

public class MusicPlayer
{
	private static final int AudioBufferSize = 256;
	private IMusicPlayerFile file;
	private long musicPos;
	private long fullPos;
	private ReentrantLock lock;
	
	public MusicPlayer(IMusicPlayerFile file)
	{
		this.file = file;
		this.lock = new ReentrantLock();
		musicPos = 0;
		fullPos = 0;
	}
	
	public void Start()
	{
		Thread thread = new Thread(new Runnable() 
		{
			public void run()
			{
				AudioDevice device = Gdx.audio.newAudioDevice(file.GetSampleRate(), file.GetChannelCount() == 1); // TODO: more than 2 channels?
		
				System.out.printf("%d\n", device.getLatency());
				
				short[] buffer = new short[AudioBufferSize]; 
				while(true)
				{
					try
					{
						int read = file.NextSamples(buffer, AudioBufferSize);
					
						device.writeSamples(buffer, 0, AudioBufferSize);
						lock.lock();
						
						musicPos += read;
						fullPos = (musicPos - device.getLatency()) * 1000 / file.GetSampleRate() / file.GetChannelCount();
						lock.unlock();
						
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
		});
		thread.start();
	}
	
	public long GetPosition()
	{
		long pos;
		lock.lock();
		pos = fullPos;
		lock.unlock();
		return pos; // NOT GOOD.
	}
}
