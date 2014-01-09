package org.musictap.gui;

import org.musictap.audiofiles.WavAudioFile;
import org.musictap.interfaces.*;
import org.musictap.trackfilters.DifficultyTrackFilter;
import org.musictap.trackgenerators.StandardTrackGenerator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.TimeUtils;

public class Application implements ApplicationListener 
{
	private ICell cells[][];
	private int frame;
	private Music music;

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
