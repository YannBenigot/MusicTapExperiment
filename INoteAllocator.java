public interface INoteAllocator
{
	void Add(int t, int hold, int freq);
	Iterable<Note> Alloc();
}
