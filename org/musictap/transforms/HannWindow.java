package org.musictap.transforms;
import org.musictap.interfaces.*;

public class HannWindow implements IWindow
{
	public double window(double t)
	{
		return 0.5 - 0.5 * Math.cos(2 * Math.PI * t);
	}
}
