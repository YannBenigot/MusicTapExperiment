package org.musictap.gui;

import java.util.Iterator;
import java.util.Vector;

import org.musictap.interfaces.Note;
import org.musictap.interfaces.Track;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
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
		
		color = new Color(0.0f, 0.0f, 1.0f, 1.0f);
	}

	@Override
	public void OnTouchStart(long time) {
		color = new Color(0.0f, 0.3f, 0.0f, 1.0f);
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
	}

	@Override
	public void OnTouchEnd(long time)
	{
		color = new Color(0.0f, 0.0f, 1.0f, 1.0f);
		if(currentNote == null)
			return;
		
		noteTapped = false;
		
		if(currentNote.hold > 1 && currentNote.t + currentNote.hold - GameParameters.TimeTolerance < time)
		{
			//Success();
			noteValidated = true;
		}
	}

	static Texture tex;
	private static void rectRGBA(float x, float y, float w, float h, float r, float g, float b, float a)
	{
		if(tex == null)
		{
			Pixmap pix = new Pixmap(1, 1, Format.RGB888);
			pix.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			pix.drawPixel(0, 0);
			tex = new Texture(pix);
		}
		
		Application.Batch.setColor(r, g, b, a);
		Application.Batch.draw(tex, x, y, w, h);
	}
	
	@Override
	public void Draw(long time)
	{
		//color.g = (1.0f + (float)Math.sin(time/60))/2;
		rectRGBA(0, 0, 1, 1, color.r, color.g, color.b, color.a);
	
		if(currentNote == null)
			return;
		
		// Note is in the future
		if(time <= currentNote.t && time + GameParameters.NoteAnticipationDelay >= currentNote.t)
		{
			float f = 1.0f - ((float)(currentNote.t - time)) / GameParameters.NoteAnticipationDelay;
			
			rectRGBA(0, 0, 1, f/2, f*0.5f, f*0.5f, f*0.5f, 1.0f);
			rectRGBA(0, (1.0f - f/2), 1, f/2, f*0.5f, f*0.5f, f*0.5f, 1.0f);
		}
		
		if(currentNote.t < time && currentNote.t + currentNote.hold > time)
		{
			if(noteTapped)
				rectRGBA(0, 0, 1, 1, 0.0f, 0.5f, 0.5f, 1.0f);
			else
				rectRGBA(0, 0, 1, 1, 0.5f, 0.5f, 0.5f, 1.0f);
			
		}
		
		// Note is in the past
		if(currentNote.t + currentNote.hold <= time && currentNote.t + currentNote.hold + GameParameters.ResultDelay > time)
		{
			float f = 1.0f - ((float)(time - currentNote.t - currentNote.hold)) / GameParameters.ResultDelay;
			
			if(noteValidated)
				rectRGBA(0, 0, 1, 1, f*0.3f, f*0.8f, f*0.3f, 1.0f);
			else
				rectRGBA(0, 0, 1, 1, f*0.8f, f*0.3f, f*0.3f, 1.0f);
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
