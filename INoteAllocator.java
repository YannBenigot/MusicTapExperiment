public interface INoteAllocator
{
	void Add(int t, int freq);
	Iterable<Note> Alloc();
}
