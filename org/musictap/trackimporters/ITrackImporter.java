package org.musictap.trackimporters;

import java.io.IOException;
import java.io.Reader;

import org.musictap.interfaces.Track;

public interface ITrackImporter
{
	Track ImportTrack(Reader stream) throws IOException;
}
