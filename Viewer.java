import org.jsfml.graphics.*;
import org.jsfml.audio.*;
import org.jsfml.window.*;
import org.jsfml.system.*;
import java.nio.file.Paths;
import java.util.Iterator;
import java.io.*;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

class Viewer
{
	static int Time = 30;

	public static void main(String[] args) throws AudioReadException, IOException
	{
		RenderWindow window = new RenderWindow(new VideoMode(800, 600), "Manger");
		AudioStream music = new AudioStream(new FileInputStream(new File(args[0])));
		RectangleShape rects[][] = new RectangleShape[4][4];

		IAudioData data = new AudioData(new WavAudioFile(args[0]), new JTransformsFFT(1024));
		ITrackGenerator generator = new FFTTrackGenerator();
		Track track = generator.GenerateTrack(data);

		for(int x=0; x<4; x++)
			for(int y=0; y<4; y++)
			{
				rects[x][y] = new RectangleShape(new Vector2f(80, 80));
				rects[x][y].setPosition(new Vector2f(x*80, y*80));
				rects[x][y].setFillColor(new Color(0, 0, 0));
				rects[x][y].setOutlineColor(new Color(255, 255, 255));
				rects[x][y].setOutlineThickness(1.0f);
			}


		window.setFramerateLimit(60);
		int t = 0;
		Iterator<Note> nit = track.notes.iterator();
		Note note = nit.next();
		int ttx[][] = new int[4][4];
		Clock clock = new Clock();
		AudioPlayer.player.start(music);
		clock.restart();
		while(true)
		{
			for(int x=0; x<4; x++)
				for(int y=0; y<4; y++)
					if(ttx[x][y] > 0)
						ttx[x][y]--;

			do
			{
				if(note.t == t)
				{
					ttx[note.x][note.y] = Time;
				}
				else if(note.t > t)
				{
					break;
				}

				if(nit.hasNext())
					note = nit.next();
				else
					break;
			}
			while(true);

			for(int x=0; x<4; x++)
				for(int y=0; y<4; y++)
					rects[x][y].setFillColor(new Color(255 * ttx[x][y] / Time, 0, 0));

			window.clear();
			for(int x=0; x<4; x++)
				for(int y=0; y<4; y++)
					window.draw(rects[x][y]);
			window.display();
			t++;
			//System.out.format("%d - %d\n", 60*t, music.getPlayingOffset().asMilliseconds());
			if(1000*t/60 > clock.getElapsedTime().asMilliseconds())
				try{
				Thread.sleep(1000*t/60-clock.getElapsedTime().asMilliseconds());
				}
				catch(Exception e)
				{
				}
		}
	}
}

