interface ITrackGenerator
{
	public Track GenerateTrack(IAudioData data) throws AudioReadException;
}
