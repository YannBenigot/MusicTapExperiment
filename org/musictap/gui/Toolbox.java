package org.musictap.gui;

import java.awt.Color;

import org.musictap.interfaces.AudioReadException;
import org.musictap.interfaces.ITransform;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;

public class Toolbox
{
	final static int MaxTexSide = 2048;
	
	static int Min(int a, int b)
	{
		return (a > b ? b : a);
	}
	
	public static Texture TransformToTexture(ITransform transform, float r, float g, float b) throws AudioReadException
	{
		int width = Min(transform.StreamLength(), MaxTexSide);
		int height = Min(transform.BlockLength(), MaxTexSide);
		
		Pixmap p = new Pixmap(NP2(width), NP2(height), Format.RGBA8888);
		
		double maxVal = 0;
		for(int i=0; i<transform.StreamLength(); i++)
		{
			double[] data = transform.Next();

			for(int j=0; j<transform.BlockLength(); j++)
			{
				if(data[j] > maxVal)
				{
					maxVal = data[j];
				}
			}
		}
		
		transform.Rewind();

		for(int i=0; i<width; i++)
		{
			double[] data = transform.Next();

			for(int j=0; j<height; j++)
			{
				float f = (float) (Math.pow(data[j] / maxVal, 0.2));
				if(f == 0)
					continue;
				p.setColor(r * f, g * f, b * f, 1.0f);
				p.drawPixel(i, j);
			}
		}
		
		return new Texture(p);
	}
	
	public static int NP2(int n)
	{
		int ret = 1;
		while(ret < n && ret < MaxTexSide)
			ret *= 2;
		return ret;
	}
}
