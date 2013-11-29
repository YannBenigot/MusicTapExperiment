public class TestAngle
{
	public static void main(String[] args)
	{
		INoteChainAllocator n = new AngleNoteChainAllocator(Integer.parseInt(args[0]), 1, 1);

		for(int i=0; i<16; i++)
		{
			Position p = n.Next(true);
			System.out.format("%d %d\n", p.X, p.Y);
		}

		System.out.println("Reverse");

		for(int i=0; i<16; i++)
		{
			Position p = n.Next(false);
			System.out.format("%d %d\n", p.X, p.Y);
		}
	}
}