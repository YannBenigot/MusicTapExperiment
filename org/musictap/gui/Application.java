package org.musictap.gui;

import org.musictap.audiofiles.WavAudioFile;
import org.musictap.interfaces.*;
import org.musictap.trackfilters.DifficultyTrackFilter;
import org.musictap.trackgenerators.StandardTrackGenerator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.TimeUtils;

public class Application implements ApplicationListener 
{
	private ICell cells[][];
	private Music music;

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
			y = Gdx.graphics.getHeight()-y;
			int cx = (int) x * 4 / 512;
			int cy = (int) y * 4 / 512;
			if(cy >= 4)
				return false;
			
			if(!touched[cx][cy])
			{
				touched[cx][cy] = true;
				positions[pointer] = new Position(cx, cy);
				cells[cx][cy].OnTouchStart((long)(music.getPosition()*60.0f));
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
					cells[pos.X][pos.Y].OnTouchEnd((long)(music.getPosition()*60.0f));
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
			IAudioFile file = new WavAudioFile("test.wav");
			ITrackGenerator trackGenerator = new StandardTrackGenerator();
			Track track = trackGenerator.GenerateTrack(file);
			ITrackFilter filter = new DifficultyTrackFilter(7);
			track = filter.Filter(track);
		
			music = Gdx.audio.newMusic(new FileHandle("test.wav"));
			cells = new ICell[4][4];
			for(int x=0; x<4; x++)
				for(int y=0; y<4; y++)
					cells[x][y] = new NoteCell(x, y, track);
		
			music.play();
			TapInput tapInput = new TapInput();
			Gdx.input.setInputProcessor(tapInput);
			
			OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void render () 
	{
		if(!music.isPlaying())
			return;
		Gdx.gl.glClearColor(0,  0,  0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		long time = (long) (music.getPosition() * 60.0f);
		
		for(int x=0; x<4; x++)
			for(int y=0; y<4; y++)
				cells[x][y].Update(time);
		
		for(int x=0; x<4; x++)
			for(int y=0; y<4; y++)
				cells[x][y].Draw(time);
		
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
