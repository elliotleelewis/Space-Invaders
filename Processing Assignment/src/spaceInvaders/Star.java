package spaceInvaders;
import java.util.*;
import processing.core.*;
public class Star
{
	// TYPE:	Game Element
	// DESC:	Floating randomly moving stars that are shown in the background of the game.
	PApplet parent;
	Random random = new Random();
	float x, y, minDY, maxDY, dY;
	boolean big;
	public Star(PApplet parent, float minDY, float maxDY)
	{
		this.parent = parent;
		this.minDY = minDY;
		this.maxDY = maxDY;
		generate(true);
	}
	public void update()
	{
		move();
		render();
	}
	private void move()
	{
		if(y + dY > parent.height)
		{
			generate(false);
		}
		y += dY;
	}
	private void render()
	{
		if(big)
		{
			parent.rect(x, y, 6, 2);
			parent.rect(x, y, 2, 6);
		}
		else
		{
			parent.rect(x, y, 2, 2);
		}
	}
	private void generate(boolean firstTime)
	{
		float deltaDX = maxDY - minDY;
		dY = ((float) random.nextInt((int) (deltaDX * 100)) / 100f) + minDY;
		x = random.nextInt(parent.width);
		if(firstTime)
		{
			y = random.nextInt(parent.height);
		}
		else
		{
			y = 0;
		}
		if(random.nextInt(10) == 1)
		{
			big = true;
		}
		else
		{
			big = false;
		}
	}
}