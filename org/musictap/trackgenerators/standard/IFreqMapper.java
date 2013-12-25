package org.musictap.trackgenerators.standard;
import org.musictap.interfaces.*;

public interface IFreqMapper
{
	void Learn(int freq);
	void Process();
	int Map(int freq);
}