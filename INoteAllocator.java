public interface INoteAllocator
{
	void Add(int t, int hold, int freq, double priority);
	Iterable<Note> Alloc();
}
