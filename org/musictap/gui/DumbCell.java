package org.musictap.gui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class DumbCell implements ICell 
{
	private int x, y;

	public DumbCell(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void OnTouchStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnTouchEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Draw() {
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.rect(x*128, y*128, 128, 128);
		shapeRenderer.end();
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub

	}

}
