import java.util.Vector;
import java.util.Collections;

public class SimpleFreqMapper implements IFreqMapper
{
	private Vector<Integer> freqs;
	private int[] thresholds;

	public SimpleFreqMapper()
	{
		freqs = new Vector<Integer>();
		thresholds = new int[16];
	}

	public void Learn(int freq)
	{
		freqs.add(new Integer(freq));
	}

	public void Process()
	{
		double freqsPerBucket = ((double) freqs.size())/16;
		Collections.sort(freqs);

		int count = 0;
		int bucket = 0;
		for(Integer freq: freqs)
		{
			count++;
			if(count > freqsPerBucket * (bucket+1))
			{
				thresholds[bucket] = freq.intValue();
				bucket++;
			}
		}
		thresholds[15] = freqs.lastElement();

		System.out.print("SimpleFreqMapper: ");
		for(int i=0; i<16; i++)
			System.out.format("%d ", thresholds[i]);
		System.out.println("");
	}

	public int Map(int f)
	{
		for(int i=0; i<16; i++)
		{
			if(f <= thresholds[i])
				return i;
		}

		return 15;
	}
}