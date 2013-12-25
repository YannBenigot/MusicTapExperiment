package org.musictap.interfaces;

public interface ITrackGenerator
{
	Track GenerateTrack(IAudioFile data) throws AudioReadException;
}
