package org.musictap.gui;

public interface ICell 
{
	void OnTouchStart(long time);
	void OnTouchEnd(long time);
	
	void Draw(long time);
	void Update(long time);
}