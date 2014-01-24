package org.musictap.gui;

import org.musictap.interfaces.ITransform;

import com.badlogic.gdx.graphics.Texture;

public class DebugHeader implements IHeader
{
	private Texture texture;
	
	public DebugHeader(ITransform t)
	{
		try
		{
			texture = Toolbox.TransformToTexture(t, 1.0f, 1.0f, 1.0f);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void Render(long time)
	{
		Application.Batch.draw(texture, 0, 0, 8.0f, 2.0f);
		
	}

	@Override
	public void OnTouch(long time)
	{
		// TODO Auto-generated method stub
		
	}

}
