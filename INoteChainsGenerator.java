public interface INoteChainsGenerator
{
	public Iterable<Iterable<ChainNoteData>> GenerateNoteChains(Iterable<ChainNoteData> notes);
}