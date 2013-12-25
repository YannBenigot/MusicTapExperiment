package org.musictap.trackgenerators.standard;
import org.musictap.interfaces.*;

public interface INoteChainAllocator
{
	Position Next(boolean up);
}