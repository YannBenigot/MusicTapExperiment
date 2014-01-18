package org.musictap.gui;

import java.util.Iterator;
import java.util.Vector;

import org.musictap.interfaces.Note;
import org.musictap.interfaces.Track;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;

public class NoteCell implements ICell
{
	private int x, y;
	private Vector<Note> notes;
	private Iterator<Note> noteIterator;
	private Note currentNote;
	private boolean noteValidated;
	private boolean noteTapped;
	private Color color;

	public NoteCell(int x, int y, Track track)
	{
		this.x = x;
		this.y = y;
		notes = new Vector<Note>();
		noteValidated = false;
		noteTapped = false;
		for (Note n : track.notes)
			if (n.x == x && n.y == y)
				notes.add(n);

		noteIterator = notes.iterator();
		if(noteIterator.hasNext())
			currentNote = noteIterator.next();
		else
			currentNote = null;
		
		color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	public void OnTouchStart(long time) {
		System.out.printf("start %d %d %d\n", x, y, time);
		if(currentNote == null)
			return;
		
		if(currentNote.t - GameParameters.TimeTolerance < time || currentNote.t + GameParameters.TimeTolerance > time)
		{
			noteTapped = true;
			
			if(currentNote.hold <= 1)
			{
				//Success();
				noteValidated = true;
			}
		}
		
		color = new Color(0.0f, 1.0f, 0.0f, 1.0f);
	}

	@Override
	public void OnTouchEnd(long time)
	{
		System.out.printf("stop %d %d %d\n", x, y, time);
		if(currentNote == null)
			return;
		
		noteTapped = false;
		
		if(currentNote.hold > 1 && currentNote.t + currentNote.hold - GameParameters.TimeTolerance < time)
		{
			//Success();
			noteValidated = true;
		}
		
		color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	public void Draw(long time)
	{
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		Matrix4 m = new Matrix4();
		m.idt();
		shapeRenderer.setProjectionMatrix(m);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(color);
		color.g = (1.0f + (float)Math.sin(time/60))/2;
		shapeRenderer.rect(x * 64+1, y * 64+1, 64-1, 64-1);
		shapeRenderer.end();
	
		if(currentNote == null)
			return;
		
		if(time < currentNote.t && time + GameParameters.NoteAnticipationDelay >= currentNote.t)
		{
			float f = 1.0f - ((float)(currentNote.t - time)) / GameParameters.NoteAnticipationDelay;
			
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0.5f, 0.5f, 0.5f, f*1.0f);			
			shapeRenderer.rect(x * 64, y * 64, 64, 64 * f/2);
			shapeRenderer.rect(x * 64, (y + 1.0f - f/2) * 64, 64, 64 * f/2);
			shapeRenderer.end();
		}
		if(currentNote.t <= time && currentNote.t + GameParameters.ResultDelay > time)
		{
			float f = 1.0f - ((float)(time - currentNote.t)) / GameParameters.ResultDelay;
			
			shapeRenderer.begin(ShapeType.Filled);
			if(noteValidated)
				shapeRenderer.setColor(f*0.3f, f*0.8f, f*0.3f, 1.0f);
			else
				shapeRenderer.setColor(f*0.8f, f*0.3f, f*0.3f, f*1.0f);
			shapeRenderer.rect(x * 64, y * 64, 64, 64);
			shapeRenderer.end();
		}
	}

	@Override
	public void Update(long time)
	{
		if(currentNote == null)
			return;
		
		if(currentNote.t + currentNote.hold + GameParameters.TimeTolerance < time)
		{
			//if(!noteValidated)
				//Miss();
			
			if(noteIterator.hasNext())
				currentNote = noteIterator.next();
			else
				currentNote = null;
			
			noteValidated = false;
			assert(!noteTapped);
		}
		
	}
}
