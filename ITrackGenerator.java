interface ITrackGenerator
{
	Track GenerateTrack(IAudioFile data) throws AudioReadException;
}
