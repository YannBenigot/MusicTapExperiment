package org.musictap.trackgenerators.standard;
import org.musictap.interfaces.*;

public class BaseNoteMapper implements INoteMapper
{

	protected abstract int[][] getMapping();
}