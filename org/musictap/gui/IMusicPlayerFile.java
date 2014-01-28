package org.musictap.gui;

import org.musictap.interfaces.AudioReadException;

public interface IMusicPlayerFile
{
	int GetSampleRate();
	int GetChannelCount();
	int NextSamples(short[] buffer, int count) throws AudioReadException;
}
