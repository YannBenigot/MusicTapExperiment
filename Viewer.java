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

	public static Texture DataToTexture(ITransform in, int size) throws TextureCreationException, AudioReadException
	{
		double[] data = in.Next();
		int t = 0;
		Image image = new Image();
		image.create(size, data.length);

		do
		{
			for(int i=0; i<data.length; i++)
			{
				double a = data[i]/500;
				if(a > 1.0)
					a = 1.0;
				image.setPixel(t, data.length-i-1, new Color((int) (a*255), (int) (a*255), (int) (a*255)));
			}

			t++;
			data = in.Next();
		} while(t < size);

		Texture tex = new Texture();
		tex.loadFromImage(image);
		return tex;
	}

	public static void main(String[] args) throws AudioReadException, IOException, TextureCreationException
	{
		RenderWindow window = new RenderWindow(new VideoMode(800, 600), "Manger");
		AudioStream music = new AudioStream(new FileInputStream(new File(args[0])));
		RectangleShape rects[][] = new RectangleShape[4][4];

		IAudioData data = new AudioData(new WavAudioFile(args[0]), new JTransformsFFT(1024));
		ITrackGenerator generator = new FFTTrackGenerator();
		Track track = generator.GenerateTrack(data);
		Texture tex = DataToTexture(new PeakTransform(new FFTTransform(new WindowTransform(new KeepTransform(new WavAudioFile(args[0]), 8192, 8192-1024), new HannWindow(), 8192), new JTransformsFFT(8192)), 16), data.GetLength()/1024);

		Sprite s = new Sprite(tex);

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
		RectangleShape line = new RectangleShape(new Vector2f(1, 600));
		line.setPosition(400, 0);
		line.setFillColor(new Color(255, 255, 255, 128));
		s.setScale(1.0f, 1.0f);

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

			s.setPosition(-(t+3)*44100/60/1024 + 400, 600-4096);

			window.clear();
			window.draw(s);
			window.draw(line);
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

