public interface ITransform
{
	double[] Next() throws AudioReadException;
	int BlockLength() throws AudioReadException;
	int StreamLength() throws AudioReadException;
	void Rewind() throws AudioReadException;
}

