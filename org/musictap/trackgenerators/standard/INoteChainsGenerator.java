package org.musictap.trackgenerators.standard;
import org.musictap.interfaces.*;

public interface INoteChainsGenerator
{
	public Iterable<Iterable<ChainNoteData>> GenerateNoteChains(Iterable<ChainNoteData> notes);
}