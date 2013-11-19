public class Text
{
	public static void main(String[] args) throws Exception
	{
		ITransform in = new PeakTransform(new FFTTransform(new WindowTransform(new KeepTransform(new WavAudioFile(args[0]), 8192, 8192-1024), new HannWindow(), 8192), new JTransformsFFT(8192)), 2);

		double[] d1 = in.Next(), d2 = in.Next(), d3 = in.Next();

		for(int i=0; i<4096; i++)
		{
			System.out.format("%f %f %f\n", d1[i], d2[i], d3[i]);
		}
	}
}
