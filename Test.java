import org.jsfml.graphics.*;
import org.jsfml.window.*;
import java.nio.file.Paths;
import java.io.IOException;

class Test
{
	public static void main(String[] args) throws IOException
	{
		RenderWindow window = new RenderWindow(new VideoMode(800, 600), "Manger");
		Font font = new Font();
		font.loadFromFile(Paths.get("dejavu.ttf"));
		Text text = new Text("Manger", font, 50);
		text.setPosition(10, 10);

		while(true)
		{
		window.clear();
		window.draw(text);
		window.display();
		}
	}
}

