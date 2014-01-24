package org.musictap.gui;

import org.musictap.audiofiles.WavAudioFile;
import org.musictap.fft.JTransformsFFT;
import org.musictap.interfaces.*;
import org.musictap.trackfilters.DifficultyTrackFilter;
import org.musictap.trackgenerators.StandardTrackGenerator;
import org.musictap.transforms.AudioFileTransformAdapter;
import org.musictap.transforms.FFTTransform;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Application implements ApplicationListener 
{
	private Music music;
	private String filename;
	private long start;
	private IMode currentMode;
	public OrthographicCamera cellCamera[][];
	
	public static SpriteBatch Batch;
	public static OrthographicCamera Camera;
	
	public Application(String filename)
	{
		this.filename = filename;
		System.out.println("Filename: "+filename);
	}

	// FIXME: a mode switch may happen while the user is touching the screen, breaking touchUp/touchDown consistency
	private class TapInput implements InputProcessor
	{
		Position positions[];
		boolean touched[][];
		
		final static int MaxPointers = 32;
		public TapInput()
		{
			positions = new Position[MaxPointers];
			touched = new boolean[4][4];
		}
		
		@Override
		public boolean touchDown(int x, int y, int pointer, int button)
		{
			int cx = (int) x * 4 / Gdx.graphics.getWidth();
			int cy = (int) y * 4 / Gdx.graphics.getHeight();
			if(cx >= 4 || cy >= 4)
				return false;
			
			if(!touched[cx][cy])
			{
				touched[cx][cy] = true;
				positions[pointer] = new Position(cx, cy);
				currentMode.GetCells()[cx][cy].OnTouchStart((long)(music.getPosition()*60.0f));
				return false;
			}
			
			return false;
		}
		
		@Override
		public boolean touchUp(int x, int y, int pointer, int button)
		{
			Position pos = positions[pointer];
			if(pos != null)
			{
				if(touched[pos.X][pos.Y])
				{
					touched[pos.X][pos.Y] = false;
					positions[pointer] = null;
					currentMode.GetCells()[pos.X][pos.Y].OnTouchEnd((long)(music.getPosition()*60.0f));
				}
			}
			
			return false;
		}

		@Override
		public boolean keyDown(int keycode)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyUp(int keycode)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyTyped(char character)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean scrolled(int amount)
		{
			// TODO Auto-generated method stub
			return false;
		}
	}
	public void create () 
	{
		try
		{
			cellCamera = new OrthographicCamera[4][4];
			for(int x=0; x<4; x++)
				for(int y=0; y<4; y++)
				{
					cellCamera[x][y] = new OrthographicCamera();
					cellCamera[x][y].setToOrtho(true, 4.0f, 6.0f);
					cellCamera[x][y].update();
					cellCamera[x][y].translate(-1.0f * x, -2.0f -1.0f * y);
				}
			IAudioFile file = new WavAudioFile(filename);
			ITrackGenerator trackGenerator = new StandardTrackGenerator();
			Track track = trackGenerator.GenerateTrack(file);
			ITrackFilter filter = new DifficultyTrackFilter(4);
			track = filter.Filter(track);

			FileHandle handle = Gdx.files.absolute(filename);
			if(handle == null)
				System.out.println("NULL HANDLE!");
			if(!handle.exists())
				System.out.println("FILE DOES NOT EXIST!");
			music = Gdx.audio.newMusic(handle);
			TrackPlayMode mode = new TrackPlayMode(track, new FFTTransform(new JTransformsFFT(8192), new AudioFileTransformAdapter(8192, new WavAudioFile(filename))));
			currentMode = mode;
		
			TapInput tapInput = new TapInput();
			Gdx.input.setInputProcessor(tapInput);
			
			Camera = new OrthographicCamera();
			Camera.setToOrtho(true, 4.0f, 6.0f);

			Batch = new SpriteBatch();
			
			music.play();
			mode.Start();
			start = System.currentTimeMillis();
		}
		catch(Exception e)
		{
			// TODO: Create an IMode for error reporting
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void render () 
	{	
		Gdx.gl.glClearColor(0,  0,  0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		long time = (System.currentTimeMillis() - start) * 60 / 1000;
		
		for(int x=0; x<4; x++)
			for(int y=0; y<4; y++)
				currentMode.GetCells()[x][y].Update(time);
		
		Camera.update();
		Batch.setProjectionMatrix(Camera.combined);
		Batch.begin();
		currentMode.GetHeader().Render(time);
		for(int x=0; x<4; x++)
			for(int y=0; y<4; y++)
			{
				Batch.end();
				cellCamera[x][y].update();
				Batch.setProjectionMatrix(cellCamera[x][y].combined);
				Batch.begin();
				currentMode.GetCells()[x][y].Draw(time);
			}
		Batch.end();
	}

	public void resize (int width, int height) 
	{
	}

	public void pause () 
	{
	}

	public void resume () 
	{
	}

	public void dispose () 
	{
	}
}
