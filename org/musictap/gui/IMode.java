package org.musictap.gui;

public interface IMode
{
	IHeader GetHeader();
	ICell[][] GetCells();
	
	void Render(long time);
}
